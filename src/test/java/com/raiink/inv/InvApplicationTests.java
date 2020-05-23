package com.raiink.inv;

import com.raiink.inv.aspect.CglibProxyUser;
import com.raiink.inv.aspect.IUser;
import com.raiink.inv.aspect.JdkProxyHandler;
import com.raiink.inv.aspect.StaticProxyUser;
import com.raiink.inv.aspect.User;
import com.raiink.inv.factory.AudiFactory;
import com.raiink.inv.factory.Car;
import com.raiink.inv.factory.InterfaceCarFactory;
import com.raiink.inv.factory.SimpleCarFactory;
import com.raiink.inv.service.Single.ConcurrentCreateSingleDo;
import com.raiink.inv.service.lock.LockFactory;
import com.raiink.inv.service.lock.LockFactoryDistribute;
import com.raiink.inv.service.lock.LockFactoryMonomer;
import com.raiink.inv.service.lock.model.GetLock;
import com.raiink.inv.service.lock.model.ReleaseLock;
import com.raiink.inv.service.thread.TrunkShipment;
import com.raiink.inv.service.thread.TrunkShipmentSingleThread;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StopWatch;

@Slf4j
// @SpringBootTest // 不需要测试spring依赖的时候注释掉
class InvApplicationTests {
  @Autowired private ApplicationContext applicationContext;

  @Test
  void contextLoads() {
    // 静态代理
    log.info("=======================测试静态代理");
    StaticProxyUser staticProxyUser = new StaticProxyUser(new User());
    staticProxyUser.save();
    log.info("=======================测试jdk动态代理");
  }

  public void testJdkProxy() {
    // jdk动态代理，被代理对象必须实现接口
    IUser iUser = new User();
    IUser jdkProxyUser =
        (IUser)
            Proxy.newProxyInstance(
                iUser.getClass().getClassLoader(),
                iUser.getClass().getInterfaces(),
                new JdkProxyHandler(iUser)); // 代理对象在运行时产生
    jdkProxyUser.save();
    log.info("=======================测试cglib动态代理");
  }

  public void testCglibProxy() {
    // cglib 动态代理
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(User.class);
    enhancer.setCallback(new CglibProxyUser());
    User user = (User) enhancer.create();
    user.save();
  }

  @Autowired private User bob;

  @Test
  public void testAspect() {
    // 通过注解标记aop的切点位置
    IUser tom = new User();
    // tom.setName("tome");
    tom.save();
    // 只有注入容器中的对象，或者获取容器中的实例对象，才会触发切面
    // User bob = (User) applicationContext.getBean(User.class);
    bob.setName("boob");
    bob.save();
  }

  @Test
  // 简单工厂模式，违反开闭原则，工厂增加种类需要修改代码
  public void testSimpleFactory() {
    SimpleCarFactory simpleCarFactory = new SimpleCarFactory();
    Car audiCar = simpleCarFactory.createCar("奥迪");
    audiCar.logName();
    Car benziCar = simpleCarFactory.createCar("奔驰");
    benziCar.logName();
  }

  @Test
  // 工厂方法模式，工厂实现同一个接口，接口中定义方法，所有的工厂实例需要实现接口工厂
  public void testInterfaceFactory() {
    InterfaceCarFactory interfaceCarFactory = new AudiFactory();
    Car car = interfaceCarFactory.newInstance();
    car.logName();
    // 需要增加新的类型，只需要重新实现工厂接口即可
  }

  @Test
  // 工厂方法模式，通过具体的工厂实现类生成不同的对象(锁对像)实例
  // 抽象工厂模式，通过具体的工厂实现类生成不同的对象组成部分(加锁对象和释放锁对象)的实例
  public void testAbstractFactory() {
    // 单体应用的锁
    LockFactory lockFactoryMonomer = new LockFactoryMonomer();
    GetLock getLockMonomer = lockFactoryMonomer.newGetLock();
    getLockMonomer.getLockName();
    ReleaseLock releaseLockMonomer = lockFactoryMonomer.newReleaseLock();
    releaseLockMonomer.releaseLockName();
    // 分布式应用的锁
    LockFactory lockFactoryDistribute = new LockFactoryDistribute();
    GetLock getLockDistribute = lockFactoryDistribute.newGetLock();
    getLockDistribute.getLockName();
    ReleaseLock releaseLockDistribute = lockFactoryDistribute.newReleaseLock();
    releaseLockDistribute.releaseLockName();
  }

  @Test
  // 测试单例模式
  public void testSingle() {
    ConcurrentCreateSingleDo createSingleDo = new ConcurrentCreateSingleDo();
    Thread tmp = new Thread(createSingleDo);
    Thread tmp2 = new Thread(createSingleDo);
    tmp.start();
    tmp2.start();
  }

  // 通过多线程，并行io时间长的步骤，减少整体执行时长
  public void testThreadCase() throws InterruptedException {
    StopWatch stopwatch = new StopWatch("testThreadCase");
    stopwatch.start("single thread trunk");
    TrunkShipment singleThread = new TrunkShipmentSingleThread();
    singleThread.getSfFreight();
    singleThread.getOrderInfo();
    stopwatch.stop();
    stopwatch.start("multi thread trunk");
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    singleThread.getOrderInfo();
    stopwatch.stop();
  }
}
