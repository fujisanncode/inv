package com.raiink.inv.configure;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @description: 获取bean工具类
 * @author: hulei
 * @create: 2020-05-16 18:56:41
 */
@Component
public class SpringUtil implements ApplicationContextAware {
  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringUtil.applicationContext = applicationContext;
  }

  /**
   * @description 获取springContext
   * @return org.springframework.context.ApplicationContext
   * @author hulei
   * @date 2020-05-16 19:05:50
   */
  public static ApplicationContext getApplicationContext() {
    return SpringUtil.applicationContext;
  }

  /**
   * @description 生成默认名称的bean
   * @param clazz 需要生成bean的class
   * @return T
   * @author hulei
   * @date 2020-05-16 19:03:17
   */
  public static <T> T getBean(Class<T> clazz) {
    return SpringUtil.applicationContext.getBean(clazz);
  }
}
