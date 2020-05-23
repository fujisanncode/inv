package com.raiink.inv.factory;

/**
 * @description: 简单工厂模式
 * @author: hulei
 * @create: 2020-05-22 13:02:55
 */
public class SimpleCarFactory {
  public Car createCar(String name) {
    switch (name) {
      case "奥迪":
        return new AudiCar();
      case "奔驰":
        return new BenziCar();
      default:
        throw new RuntimeException("汽车类型错误");
    }
  }
}
