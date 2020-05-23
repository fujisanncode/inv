package com.raiink.inv.service.thread;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @description: 单线程的箱子发运
 * @author: hulei
 * @create: 2020-05-23 00:03:02
 */
public class TrunkShipmentSingleThread implements TrunkShipment {

  @Override
  public String getSfFreight() {
    try {
      Thread.currentThread().sleep(3 * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "SF"
        .concat(String.valueOf(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli()));
  }

  @Override
  public String getOrderInfo() {
    try {
      Thread.currentThread().sleep(3 * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return "new order";
  }
}
