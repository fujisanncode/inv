package com.raiink.inv.factory;

/**
 * @description: 奔驰工厂
 * @author: hulei
 * @create: 2020-05-22 13:24:25
 */
public class BenziFactory implements InterfaceCarFactory {

  @Override
  public Car newInstance() {
    return new BenziCar();
  }
}
