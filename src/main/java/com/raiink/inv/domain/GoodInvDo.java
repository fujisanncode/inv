package com.raiink.inv.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * @description: 商品库存数量，数据持久化对象
 * @author: hulei
 * @create: 2020-05-15 18:57:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "good_inv_t",
    indexes = {@Index(name = "good_name_idx", columnList = "goodName")})
@DynamicInsert // 新增的时候忽略null字段, 不会忽略空字符串
@DynamicUpdate // 从数据库查询出来的字段，更新时仅更新变化了的字段
@Component
public class GoodInvDo extends BaseInfoDo {

  /**
   * @description 商品库存表主键
   * @author hulei
   * @date 2020-05-16 19:55:43
   */
  @Id
  @GeneratedValue(generator = "goodInvGene")
  @GenericGenerator(name = "goodInvGene", strategy = "uuid")
  @ApiModelProperty(hidden = true)
  private String id;

  @Column(columnDefinition = "varchar(50) not null default 'S000' comment '生产商编码'")
  private String factoryCode;

  @Column(columnDefinition = "varchar(32) unique comment'商品编码'")
  private String goodNo;

  @Column(columnDefinition = "varchar(100) not null comment '商品名称'")
  private String goodName;

  @Column(columnDefinition = "int default 0 comment '库存数量'")
  private Integer goodCount;

  @Column(columnDefinition = "int default 0 comment '冻结库存数量'")
  private Integer goodFrozenCount;

  @ApiModelProperty(hidden = true)
  @Column(columnDefinition = "varchar(10) not null default 'kg' comment '计量单位'")
  private String unit;

  @Column(columnDefinition = "double default 0 comment '商品进价'")
  private Double goodInitPrice;

  @Column(columnDefinition = "double default 0 comment '商品出售价格'")
  private Double goodSalePrice;

  @ApiModelProperty(hidden = true)
  @Column(columnDefinition = "varchar(10) not null default 'RMB' comment '币种'")
  private String currency;
}
