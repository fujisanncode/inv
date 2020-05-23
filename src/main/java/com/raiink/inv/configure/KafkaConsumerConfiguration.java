package com.raiink.inv.configure;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;

/**
 * @description: 消费者配置类
 * @author: hulei
 * @create: 2020-05-21 18:02:20
 */
@Configuration
public class KafkaConsumerConfiguration {
  @Autowired private Environment environment;

  // 消费者配置
  public Map ackConsumerConfig() {
    Map config =
        new HashMap() {
          {
            put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("spring.kafka.bootstrap-servers"));
            put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                environment.getProperty("spring.kafka.consumer.key-deserializer"));
            put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                environment.getProperty("spring.kafka.consumer.value-deserializer"));
            put(
                ConsumerConfig.GROUP_ID_CONFIG,
                environment.getProperty("spring.kafka.consumer.group-id"));
            put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);
            put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // 不允许自动提交
          }
        };
    return config;
  }

  // 消费者工厂
  @Bean("ackConsumerFactory")
  public ConsumerFactory ackConsumer() {
    DefaultKafkaConsumerFactory consumer = new DefaultKafkaConsumerFactory(ackConsumerConfig());
    return consumer;
  }

  // 监听容器工厂
  @Bean("ackListenerContainer")
  public ConcurrentKafkaListenerContainerFactory ackListener(
      @Qualifier("ackConsumerFactory") ConsumerFactory consumerFactory) {
    ConcurrentKafkaListenerContainerFactory container =
        new ConcurrentKafkaListenerContainerFactory();
    container.setConsumerFactory(consumerFactory);
    container.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE); // 手动ack确认
    container.setBatchListener(true); // 设置批量监听
    container.setConcurrency(5); // 并发监听数需要小于分区数（一个线程只能消费一个分区）
    return container;
  }
}
