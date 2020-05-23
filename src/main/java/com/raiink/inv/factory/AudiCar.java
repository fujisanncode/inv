package com.raiink.inv.factory;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 奥迪汽车
 * @author: hulei
 * @create: 2020-05-22 13:04:47
 */
@Slf4j
public class AudiCar implements Car {

  @Override
  public void logName() {
    log.info("汽车是: {}", "奥迪");
  }
}
