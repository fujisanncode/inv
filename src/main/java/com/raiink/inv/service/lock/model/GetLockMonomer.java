package com.raiink.inv.service.lock.model;

import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: hulei
 * @create: 2020-05-22 21:56:49
 */
@Slf4j
public class GetLockMonomer implements GetLock {

  @Override
  public void getLockName() {
    log.info("单体应用加锁");
  }
}
