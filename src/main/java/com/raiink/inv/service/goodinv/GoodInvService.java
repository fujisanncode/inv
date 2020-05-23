package com.raiink.inv.service.goodinv;

import com.raiink.inv.domain.GoodInvDo;
import java.util.Optional;

/**
 * @description: 商品库存服务的接口层
 * @author: hulei
 * @create: 2020-05-16 17:29:45
 */
public interface GoodInvService {
  Iterable<GoodInvDo> findAllGoodInv();

  GoodInvDo insertGoodInv(GoodInvDo goodInvDo);

  /**
   * @description 根据库存冻结数量实际扣除库存
   * @param goodInvDo
   * @return com.raiink.inv.domain.GoodInvDo
   * @author hulei
   * @date 2020-05-18 15:04:20
   */
  GoodInvDo updateGoodInv(GoodInvDo goodInvDo);

  /**
   * @description 锁定库存资源，预扣除库存
   * @param goodInvDo
   * @return com.raiink.inv.domain.GoodInvDo
   * @author hulei
   * @date 2020-05-18 15:03:19
   */
  Boolean preDeductInv(GoodInvDo goodInvDo);

  Optional<GoodInvDo> findGoodInvByGoodNo(String goodNo);

  void testUpdateInv(String goodNo, Integer count);
}
