package com.raiink.inv.aspect;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: 用户实现类
 * @author: hulei
 * @create: 2020-05-21 21:21:00
 */
@Component
@Data
@Slf4j
public class User implements IUser {
  private String name;

  @ValidateUserInfo
  @Override
  public void save() {
    log.info("被代理对象执行");
  }
}
