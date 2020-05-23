package com.raiink.inv.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @description: 用户注解的切面
 * @author: hulei
 * @create: 2020-05-21 23:23:13
 */
@Component // 切面必须被扫描到
@Slf4j
@Aspect
public class UserAspect {
  @Pointcut("@annotation(com.raiink.inv.aspect.ValidateUserInfo)")
  private void userPointCut() {}

  @Before("userPointCut()")
  private void validBefore(JoinPoint joinPoint) {
    log.info("方法执行前校验用户信息");
    Object target = joinPoint.getTarget();
    log.info("before切面: {}", target.toString());
  }
}
