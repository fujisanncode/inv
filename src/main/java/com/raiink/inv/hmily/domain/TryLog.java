package com.raiink.inv.hmily.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * @description: tcc-try日志
 * @author: hulei
 * @create: 2020-05-18 18:10:52
 */
@Data
@Entity
@Table(name = "try_log_t")
@DynamicInsert
@DynamicUpdate
public class TryLog {
  @Id
  @GenericGenerator(name = "try_log_id", strategy = "uuid")
  @GeneratedValue(generator = "try_log_id")
  private String id;

  @Column(columnDefinition = "varchar(64) unique comment '事务编号'")
  private String txNo;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(columnDefinition = "timestamp default now() comment '写日志时间'")
  private Date createTime;
}
