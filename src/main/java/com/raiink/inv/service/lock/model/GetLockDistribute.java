package com.raiink.inv.service.lock.model;

import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: hulei
 * @create: 2020-05-22 22:05:51
 */
@Slf4j
public class GetLockDistribute implements GetLock {

  @Override
  public void getLockName() {
    log.info("获取分布式锁");
  }
}
