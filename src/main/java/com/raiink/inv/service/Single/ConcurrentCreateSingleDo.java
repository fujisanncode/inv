package com.raiink.inv.service.Single;

/**
 * @description: 并发创建单例
 * @author: hulei
 * @create: 2020-05-22 22:55:43
 */
public class ConcurrentCreateSingleDo implements Runnable {

  @Override
  public void run() {
    SingleDo.newInstance();
  }
}
