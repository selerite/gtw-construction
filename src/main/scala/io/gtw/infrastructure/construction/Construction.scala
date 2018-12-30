package io.gtw.infrastructure.construction

import java.util.concurrent.{CountDownLatch, ExecutorService, Executors}

import com.typesafe.scalalogging.Logger
import io.gtw.infrastructure.construction.processor.{ItemProcessor, PropertyProcessor, RelationshipProcessor}
import scopt.OptionParser

object Construction {
  val logger = Logger(this.getClass)
  def main(args: Array[String]): Unit = {
    val parser = new OptionParser[Param]("scopt") {
      head("Build Module")
      opt[String]('b', "kafkaBrokers").action((x, c) => c.copy(kafkaBrokers = Some(x))).text("kafka brokers to input")
      opt[String]('e', "kafkaTopicItem").action((x, c) => c.copy(kafkaTopicItem = Some(x))).text("kafka topic item to input")
      opt[String]('h', "kafkaTopicRelationship").action((x, c) => c.copy(kafkaTopicRelationship = Some(x))).text("kafka topic relationship to input")
      opt[String]('s', "kafkaTopicProperty").action((x, c) => c.copy(kafkaTopicProperty = Some(x))).text("kafka topic properties to input")
      opt[Int]('i', "kafkaItemConsumerThreadNum").action((x, c) => c.copy(kafkaItemConsumerThreadNum = x)).text("kafka item consumer thread num")
      opt[Int]('p', "kafkaPropertyConsumerThreadNum").action((x, c) => c.copy(kafkaPropertyConsumerThreadNum = x)).text("kafka property consumer thread num")
      opt[Int]('r', "kafkaRelationshipConsumerThreadNum").action((x, c) => c.copy(kafkaRelationshipConsumerThreadNum = x)).text("kafka relationship consumer thread num")
      opt[String]('t', "kafkaItemConsumerGroupId").action((x, c) => c.copy(kafkaTopicItem = Some(x))).text("kafka topic item consumer group id")
      opt[String]('n', "kafkaRelationshipConsumerGroupId").action((x, c) => c.copy(kafkaTopicItem = Some(x))).text("kafka topic relationship consumer group id")
      opt[String]('y', "kafkaPropertyConsumerGroupId").action((x, c) => c.copy(kafkaTopicItem = Some(x))).text("kafka topic property consumer group id")

    }
    parser.parse(args, Param()) match {
      case Some(param) =>
        run(param)
      case None =>
        parser.showUsage()
        logger.error("parser is error")
    }
  }

  def run(param: Param) {
    val kafkaBrokers: Option[String] = param.kafkaBrokers
    val kafkaTopicItem: Option[String] = param.kafkaTopicItem
    val kafkaTopicRelationship: Option[String] = param.kafkaTopicRelationship
    val kafkaTopicProperty: Option[String] = param.kafkaTopicProperty
    val kafkaItemConsumerThreadNum: Int = param.kafkaItemConsumerThreadNum
    val kafkaRelationshipConsumerThreadNum: Int = param.kafkaRelationshipConsumerThreadNum
    val kafkaPropertyConsumerThreadNum: Int = param.kafkaPropertyConsumerThreadNum
    val kafkaItemConsumerGroupId: Option[String] = param.kafkaItemConsumerGroupId
    val kafkaRelationshipConsumerGroupId: Option[String] = param.kafkaRelationshipConsumerGroupId
    val kafkaPropertyConsumerGroupId: Option[String] = param.kafkaPropertyConsumerGroupId
    val kafkaBrokersStr: String = kafkaBrokers match {
      case Some(x) => x
      case None => throw new IllegalArgumentException(s"kafka broker not set")
    }
    val kafkaTopicItemStr: String = kafkaTopicItem match {
      case Some(x) => x
      case None => throw new IllegalArgumentException(s"kafka topic item not set")
    }
    val kafkaTopicRelationshipStr: String = kafkaTopicRelationship match {
      case Some(x) => x
      case None => throw new IllegalArgumentException(s"kafka topic relationship not set")
    }
    val kafkaTopicPropertyStr: String = kafkaTopicProperty match {
      case Some(x) => x
      case None => throw new IllegalArgumentException(s"kafka topic property not set")
    }
    val kafkaItemConsumerGroupIdStr = kafkaItemConsumerGroupId match {
      case Some(x) => x
      case None => throw new IllegalArgumentException(s"kafka topic item consumer group id not set")
    }
    val kafkaRelationshipConsumerGroupIdStr = kafkaRelationshipConsumerGroupId match {
      case Some(x) => x
      case None => throw new IllegalArgumentException(s"kafka topic relationship consumer group id not set")
    }
    val kafkaPropertyConsumerGroupIdStr = kafkaPropertyConsumerGroupId match {
      case Some(x) => x
      case None => throw new IllegalArgumentException(s"kafka topic property consumer group id not set")
    }

    /* -- read items from kafka -- */
    val itemCountDownLatch: CountDownLatch = new CountDownLatch(kafkaItemConsumerThreadNum)
    val itemExecutorService: ExecutorService = Executors.newFixedThreadPool(kafkaItemConsumerThreadNum)

    for (index <- 0 until kafkaItemConsumerThreadNum) {
      logger.info(index.toString + "item job submitted")
      itemExecutorService.submit(new ItemProcessor(kafkaBrokersStr, kafkaTopicItemStr, kafkaItemConsumerGroupIdStr, itemCountDownLatch))
    }
    itemCountDownLatch.await()
    itemExecutorService.shutdown()

    /* -- read relationships from kafka -- */
    val relationshipCountDownLatch: CountDownLatch = new CountDownLatch(kafkaRelationshipConsumerThreadNum)
    val relationshipExecutorService: ExecutorService = Executors.newFixedThreadPool(kafkaRelationshipConsumerThreadNum)
    for (index <- 0 until kafkaItemConsumerThreadNum) {
      logger.info(index.toString + "relationship job submitted")
      relationshipExecutorService.submit(new RelationshipProcessor(kafkaBrokersStr, kafkaTopicRelationshipStr, kafkaRelationshipConsumerGroupIdStr, relationshipCountDownLatch))
    }
    relationshipCountDownLatch.await()
    relationshipExecutorService.shutdown()


    /* -- read properties from kafka -- */
    val propertyCountDownLatch: CountDownLatch = new CountDownLatch(kafkaPropertyConsumerThreadNum)
    val propertyExecutorService: ExecutorService = Executors.newFixedThreadPool(kafkaPropertyConsumerThreadNum)

    for (index <- 0 until kafkaPropertyConsumerThreadNum) {
      logger.info( index.toString + " property job submitted")
      propertyExecutorService.submit(new PropertyProcessor(kafkaBrokersStr, kafkaTopicPropertyStr, kafkaPropertyConsumerGroupIdStr, propertyCountDownLatch))
    }
    propertyCountDownLatch.await()
    propertyExecutorService.shutdown()
  }
}
