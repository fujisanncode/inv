package com.raiink.inv.service.Single;

import lombok.extern.slf4j.Slf4j;

/**
 * @description: 单例模式
 * @author: hulei
 * @create: 2020-05-22 22:33:53
 */
@Slf4j
public class SingleDo {
  // 类加载的时候不进行初始化，调用方法时实例化,volatile禁止对象创建指令重排序
  private static volatile SingleDo singleDo = null;

  // 私有无参数构造
  private SingleDo() {}

  // 单例模式延迟加载，双重检查单例模式
  public static SingleDo newInstance() {
    String curName = Thread.currentThread().getName();
    // 减少不必要的synchronize逻辑
    log.info("{} 准备构建单实例", curName);
    if (singleDo == null) {
      log.info("{} 构建单实例的第一步检查ok", curName);
      synchronized (SingleDo.class) {
        log.info("{} 获取了同步锁，当前没有实例准备创建", curName);
        // 两个线程同时synchronize之前，第一个生成实例对象，第二个进入后判断了则不生成实例对象
        if (singleDo == null) {
          log.info("{} 再次判断实例不存在，即刻创建实例对象", curName);
          singleDo = new SingleDo();
        }
      }
    }
    return singleDo;
  }
}
