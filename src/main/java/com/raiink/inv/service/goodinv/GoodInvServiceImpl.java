package com.raiink.inv.service.goodinv;

import com.raiink.inv.configure.CommonException;
import com.raiink.inv.configure.RedisUtil;
import com.raiink.inv.domain.GoodInvDo;
import com.raiink.inv.hmily.domain.CancelLog;
import com.raiink.inv.hmily.domain.ConfirmLog;
import com.raiink.inv.hmily.domain.TryLog;
import com.raiink.inv.hmily.repository.CancelRepository;
import com.raiink.inv.hmily.repository.ConfirmRepository;
import com.raiink.inv.hmily.repository.TryRepository;
import com.raiink.inv.repository.InvRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.core.concurrent.threadlocal.HmilyTransactionContextLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: 商品库存的服务
 * @author: hulei
 * @create: 2020-05-16 17:21:48
 */
@Slf4j
@Service
public class GoodInvServiceImpl implements GoodInvService {

  @Autowired private InvRepository invRepository;
  @Autowired private TryRepository tryRepository;
  @Autowired private ConfirmRepository confirmRepository;
  @Autowired private CancelRepository cancelRepository;

  /**
   * @description 如果redis中找不到key，从数据库查询然后写入redis；否则直接从redis中取
   * @return java.lang.Iterable<com.raiink.inv.domain.GoodInvDo>
   * @author hulei
   * @date 2020-05-16 18:34:07
   */
  @Cacheable(value = "invCacheAll", key = "#root.methodName")
  @Override
  public Iterable<GoodInvDo> findAllGoodInv() {
    return invRepository.findAll();
  }

  @Cacheable(value = "invCache", key = "'GoodInvDo:'.concat(#goodNo)")
  @Override
  public Optional<GoodInvDo> findGoodInvByGoodNo(String goodNo) {
    return invRepository.findGoodInvDoByGoodNo(goodNo);
  }

  /**
   * @description 写数据，先写数据库，然后写redis
   * @param goodInvDo
   * @return com.raiink.inv.domain.GoodInvDo
   * @author hulei
   * @date 2020-05-16 19:50:09
   */
  @Override
  public GoodInvDo insertGoodInv(GoodInvDo goodInvDo) {
    GoodInvDo saveResult = invRepository.save(goodInvDo);
    RedisUtil.set("invCache::GoodInvDo:" + goodInvDo.getGoodNo(), goodInvDo);
    return saveResult;
  }

  /**
   * @description 更新库存，先删除缓存，然后更新数据库，然后保存redis；cachePut在执行前不检查缓存，执行后一定将结果保存redis；
   *     cacheEvict在执行前或者执行方法后，删除缓存
   * @return com.raiink.inv.domain.GoodInvDo
   * @author hulei
   * @date 2020-05-17 09:53:55
   */
  @Caching(
      put = {@CachePut(value = "invCache", key = "'GoodInvDo:'.concat(#goodInvDo.goodNo)")},
      evict = {
        @CacheEvict(
            value = "invCache",
            key = "'GoodInvDo:'.concat(#goodInvDo.goodNo)",
            beforeInvocation = true)
      })
  @Override
  public GoodInvDo updateGoodInv(GoodInvDo goodInvDo) {
    invRepository.invToFrozen(goodInvDo);
    return this.findGoodInvByGoodNo(goodInvDo.getGoodNo())
        .orElseThrow(() -> new CommonException("find good inv exception"));
  }

  @Transactional
  @Hmily(confirmMethod = "confirmPreDeduct", cancelMethod = "cancelPreDeduct")
  @Override
  public Boolean preDeductInv(GoodInvDo goodInvDo) {
    String tranId = HmilyTransactionContextLocal.getInstance().get().getTransId();
    // step1-1: 验证接口幂等
    if (tryRepository.existsByTxNo(tranId)) {
      log.info("try接口验证幂等，如果try存在，返回true不重复执行后面逻辑, {}", tranId);
      return true;
    }
    // step1-2：处理try悬挂
    if (confirmRepository.existsByTxNo(tranId) || cancelRepository.existsByTxNo(tranId)) {
      log.info("处理try悬挂，如果confirm或者cancel存在，则try悬挂，返回true不重复后面的逻辑, {}", tranId);
      return true;
    }
    // step2-1: 库存减少，预扣增加，即尝试冻结资源
    if (invRepository.invToFrozen(goodInvDo) <= 0) {
      return false;
    }
    // step3-1: 写try日志
    TryLog tryLog = new TryLog();
    tryLog.setTxNo(tranId);
    tryRepository.save(tryLog);
    return true;
  }

  @Transactional
  public void confirmPreDeduct(GoodInvDo goodInvDo) {
    String tranId = HmilyTransactionContextLocal.getInstance().get().getTransId();
    // step1-1: 接口验证幂等
    if (confirmRepository.existsByTxNo(tranId)) {
      log.info("confirm接口验证幂等，{}", tranId);
      return;
    }
    // step2-1：预扣减少库存不变，表示冻结的资源被消耗
    invRepository.consumerFrozen(goodInvDo);
    // step3-1: 写confirm日志
    ConfirmLog confirmLog = new ConfirmLog();
    confirmLog.setTxNo(tranId);
    confirmRepository.save(confirmLog);
  }

  @Transactional
  public void cancelPreDeduct(GoodInvDo goodInvDo) {
    String tranId = HmilyTransactionContextLocal.getInstance().get().getTransId();
    // step1-1：接口验证幂等
    if (cancelRepository.existsByTxNo(tranId)) {
      log.info("cancel接口验证幂等, {}", tranId);
      return;
    }
    // step1-2：处理空回滚
    if (!tryRepository.existsByTxNo(tranId)) {
      log.info("try不存在，此处为空回滚，直接返回不执行逻辑, {}", tranId);
      return;
    }
    // step2-1: 预扣减少库存增加，即释放被冻结的资源
    invRepository.releaseFrozen(goodInvDo);
    // step3-1: 写回滚日志
    CancelLog cancelLog = new CancelLog();
    cancelLog.setTxNo(tranId);
    cancelRepository.save(cancelLog);
  }

  @Override
  public void testUpdateInv(String goodNo, Integer count) {
    switch (goodNo) {
      case "S001G0007":
        Integer resultInteger = invRepository.testUpdateInvInteger(goodNo, count);
        log.info("resultInteger: {}", resultInteger);
      case "S001G0005":
        Boolean resultBoolean = invRepository.testUpdateInvBoolean(goodNo, count);
        log.info("resultInteger: {}", resultBoolean);
        break;
      default:
        log.info("没有找到goodNo");
    }
  }
}
