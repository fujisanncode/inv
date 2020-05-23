package com.raiink.inv.aspect;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @description: cglib动态代理
 * @author: hulei
 * @create: 2020-05-21 23:10:03
 */
@Slf4j
public class CglibProxyUser implements MethodInterceptor {

  @Override
  public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
      throws Throwable {
    log.info("进入cglib的动态代理类");
    return methodProxy.invokeSuper(o, objects); // 调用目前对象的方法;
  }
}
