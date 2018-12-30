package io.gtw.infrastructure.construction.processor

import java.util.concurrent.CountDownLatch


class ItemProcessor (kafkaBrokers: String, kafkaTopic: String, kafkaConsumerGroupId: String, countDownLatch: CountDownLatch)
  extends Processor(kafkaBrokers = kafkaBrokers, kafkaTopic = kafkaTopic, kafkaConsumerGroupId = kafkaConsumerGroupId, countDownLatch = countDownLatch) {
  override def process(record: String): Unit = {
    logger.info(record)
  }
}
