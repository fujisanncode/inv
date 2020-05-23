package com.raiink.inv.service.thread;

import java.util.concurrent.Callable;

/**
 * @description: 异步获取顺丰单号
 * @author: hulei
 * @create: 2020-05-23 00:16:04
 */
public class TrunkShipmentSfThread implements Callable<String> {

  @Override
  public String call() throws Exception {
    String sfFreightNo = new TrunkShipmentSingleThread().getSfFreight();
    return sfFreightNo;
  }
}
