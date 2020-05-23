package com.raiink.inv.aspect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: jdk动态代理对象
 * @author: hulei
 * @create: 2020-05-21 22:27:09
 */
@Slf4j
public class JdkProxyHandler implements InvocationHandler {
  private IUser target;

  public JdkProxyHandler(IUser target) {
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    log.info("jdk动态代理对象执行逻辑");
    return method.invoke(this.target, args); // 调用被代理对象的逻辑
  }
}
