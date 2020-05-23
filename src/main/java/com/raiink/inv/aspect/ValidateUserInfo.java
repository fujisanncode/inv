package com.raiink.inv.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 被切面捕捉的注解
 * @author: hulei
 * @create: 2020-05-21 23:26:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ValidateUserInfo {}
