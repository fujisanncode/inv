package com.raiink.inv.service.lock.model;

import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: hulei
 * @create: 2020-05-22 22:01:07
 */
@Slf4j
public class ReleaseLockMonomer implements ReleaseLock {

  @Override
  public void releaseLockName() {
    log.info("单体应用释放锁");
  }
}
