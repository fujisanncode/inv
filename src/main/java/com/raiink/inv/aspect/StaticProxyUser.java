package com.raiink.inv.aspect;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 静态代理类
 * @author: hulei
 * @create: 2020-05-21 21:50:55
 */
@Slf4j
public class StaticProxyUser implements IUser {
  IUser target;

  public StaticProxyUser(IUser target) {
    this.target = target;
  }

  @Override
  public void save() {
    log.info("调用静态代理对象");
    target.save();
  }
}
