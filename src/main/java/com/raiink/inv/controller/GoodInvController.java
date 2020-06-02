package com.raiink.inv.controller;

import com.raiink.inv.configure.CommonException;
import com.raiink.inv.domain.GoodInvDo;
import com.raiink.inv.service.goodinv.GoodInvService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 接口访问层
 * @author: hulei
 * @create: 2020-05-15 19:17:36
 */
@Slf4j
@Api(value = "good inv", tags = "商品库存服务")
@RestController // controller + responseBody
@RequestMapping("/good-inv")
public class GoodInvController {

  @Autowired private GoodInvService goodInvService;

  @Transactional
  @ApiOperation(value = "add good", notes = "新增商品")
  @PostMapping("/add-good")
  public GoodInvDo addGood(@RequestBody GoodInvDo goodInvDo) {
    return goodInvService.insertGoodInv(goodInvDo);
  }

  @ApiOperation(value = "find all good", notes = "查找所有的商品")
  @GetMapping("/find-all-good")
  public Iterable<GoodInvDo> findGoodByNo() {
    StopWatch watch = new StopWatch("findAllGood");
    watch.start("step 0");
    Iterable<GoodInvDo> resultGoods = goodInvService.findAllGoodInv();
    watch.stop();
    log.info("findAllGood cost {} ms", watch.getTotalTimeMillis());
    return resultGoods;
  }

  @ApiOperation(value = "find good by no", notes = "按照商品编码查找商品")
  @GetMapping("/find-good/{goodNo}")
  public GoodInvDo findGoodByNo(@PathVariable String goodNo) {
    StopWatch stopWatch = new StopWatch("findGoodByNo");
    stopWatch.start();
    GoodInvDo result =
        goodInvService
            .findGoodInvByGoodNo(goodNo)
            .orElseThrow(() -> new CommonException("not found good"));
    stopWatch.stop();
    log.info("findGoodByNo cost {} ms", stopWatch.getTotalTimeMillis());
    return result;
  }

  @ApiImplicitParams({
    @ApiImplicitParam(name = "goodNo", value = "商品编号", required = true),
    @ApiImplicitParam(name = "count", value = "剩余库存数量", required = true)
  })
  @ApiOperation(value = "test-update-inv", notes = "测试-按照剩余库存数量修改商品库存")
  @Transactional
  @GetMapping("/test-update-inv/{goodNo}/{count}")
  public void testUpdateInv(@PathVariable String goodNo, @PathVariable Integer count) {
    goodInvService.testUpdateInv(goodNo, count);
  }

  @KafkaListener(
      topicPartitions = {
        @TopicPartition(
            topic = "test-topic",
            partitions = {"0"})
      })
  public void consumerTestTopic(ConsumerRecord<?, ?> record) {
    log.info(
        "topic: {}, partition: {}, offset: {}, key: {}, value: {}, message: {}",
        record.topic(),
        record.partition(),
        record.offset(),
        record.key(),
        record.value());
  }

  @KafkaListener(
      containerFactory = "ackListenerContainer", // 指定监听容器工厂(包含消费者)
      topicPartitions = {
        @TopicPartition(
            topic = "test-topic",
            partitions = {"1"})
      })
  public void consumerTestTopicAck(
      // @Payload String data,
      // @Header(KafkaHeaders.MESSAGE_KEY) String key,
      // @Header(KafkaHeaders.TOPIC) String topic,
      // @Header(KafkaHeaders.OFFSET) String offset,
      // @Header(KafkaHeaders.PARTITION_ID) String partitionId
      ConsumerRecord consumerRecord
      // Acknowledgment ack, Consumer consumer
      ) {
    // log.info(
    //     "topic: {}, partition: {}, offset: {}, key: {}, value: {}, message: {}",
    //     topic,
    //     partitionId,
    //     offset,
    //     key,
    //     data);
    // ack.acknowledge(); // 提交，如果不提交，消息的位移不会改变并提交服务器，等于消息没有被消费
  }
}
