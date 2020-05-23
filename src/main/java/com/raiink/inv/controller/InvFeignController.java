package com.raiink.inv.controller;

import com.raiink.feignapi.api.InvApi;
import com.raiink.inv.configure.CommonException;
import com.raiink.inv.domain.GoodInvDo;
import com.raiink.inv.service.goodinv.GoodInvService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 库存服务的feign接口实现类
 * @author: hulei
 * @create: 2020-05-19 23:46:00
 */
@Slf4j
@Api(value = "inv feign", tags = "库存服务feign接口")
@RestController // controller + responseBody
@RequestMapping(InvApi.inv_feign)
public class InvFeignController implements InvApi {

  @Autowired private GoodInvService goodInvService;

  @Transactional
  @ApiOperation(value = "updateCountById", notes = "根据商品id更新库存")
  @GetMapping(InvApi.update_count_by_id_url)
  @Override
  public Integer updateCountById(@PathVariable String goodNo) {
    GoodInvDo goodInvDo = new GoodInvDo();
    goodInvDo.setGoodNo(goodNo);
    GoodInvDo result = goodInvService.updateGoodInv(goodInvDo);
    if (result.getGoodCount() < 0) {
      throw new CommonException("库存不足");
    }
    return result.getGoodCount();
  }

  @ApiOperation(value = "pre-deduct-inv", notes = "预扣库存")
  @Transactional
  @GetMapping(InvApi.pre_deduct_inv_url)
  @Override
  public boolean preDeductInv(@PathVariable String goodNo, @PathVariable Integer preDeductCount) {
    GoodInvDo goodInvDo = new GoodInvDo();
    goodInvDo.setGoodNo(goodNo);
    goodInvDo.setGoodFrozenCount(preDeductCount);
    return goodInvService.preDeductInv(goodInvDo);
  }
}
