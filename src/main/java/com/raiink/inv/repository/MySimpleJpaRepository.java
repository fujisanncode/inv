package com.raiink.inv.repository;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @description: 自定义repository，null字段不修改
 * @author: hulei
 * @create: 2020-05-16 09:23:23
 */
public class MySimpleJpaRepository<T, ID> extends SimpleJpaRepository<T, ID> {
  private JpaEntityInformation<T, ID> entityInformation;
  private EntityManager entityManager;

  public MySimpleJpaRepository(
      JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityInformation = entityInformation;
    this.entityManager = entityManager;
  }

  @Override
  @Transactional
  public <S extends T> S save(S entity) {
    ID entityID = entityInformation.getId(entity);
    Optional<T> optionalT;
    // 没有传入主键，使用空Optional
    if (entityID == null) {
      optionalT = Optional.empty();
    } else {
      optionalT = findById(entityID);
    }
    // 空则插入数据
    if (!optionalT.isPresent()) {
      entityManager.persist(entity);
      return entity;
    } else {
      T target = optionalT.get();
      // 将source中非空属性拷贝到target中
      BeanUtils.copyProperties(entity, target, getNullProperties(entity));
      entityManager.merge(target);
      return entity;
    }
  }

  /**
   * @description Object对象中null字段名用数组返回
   * @param source 判断的对象
   * @return java.lang.String[]
   * @author hulei
   * @date 2020-05-16 12:24:04
   */
  private String[] getNullProperties(Object source) {
    BeanWrapper beanWrapper = new BeanWrapperImpl(source);
    PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
    Set<String> propertyNames = new HashSet<>();
    Arrays.stream(propertyDescriptors)
        .forEach(
            propertyDescriptor -> {
              String name = propertyDescriptor.getName();
              Object value = beanWrapper.getPropertyValue(name);
              if (value == null) {
                propertyNames.add(name);
              }
            });
    return propertyNames.toArray(new String[propertyNames.size()]);
  }
}
