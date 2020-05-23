package com.raiink.inv.factory;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 奔驰汽车
 * @author: hulei
 * @create: 2020-05-22 13:06:41
 */
@Slf4j
public class BenziCar implements Car {

  @Override
  public void logName() {
    log.info("汽车是：{}", "奔驰");
  }
}
