package com.raiink.inv.service.lock.model;

import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: hulei
 * @create: 2020-05-22 22:07:54
 */
@Slf4j
public class ReleaseLockDistribute implements ReleaseLock {

  @Override
  public void releaseLockName() {
    log.info("分布式应用释放锁");
  }
}
