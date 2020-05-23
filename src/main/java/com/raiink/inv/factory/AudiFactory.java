package com.raiink.inv.factory;

/**
 * @description: 奥迪工厂
 * @author: hulei
 * @create: 2020-05-22 13:22:24
 */
public class AudiFactory implements InterfaceCarFactory {

  @Override
  public Car newInstance() {
    return new AudiCar();
  }
}
