package io.gtw.infrastructure.construction.processor

import java.util.concurrent.CountDownLatch

import com.typesafe.scalalogging.Logger
import io.gtw.infrastructure.construction.utils.KafkaUtils
import org.apache.kafka.clients.consumer.{ConsumerRecord, ConsumerRecords, KafkaConsumer}

import scala.collection.JavaConverters._

abstract class Processor (kafkaBrokers: String, kafkaTopic: String, kafkaConsumerGroupId: String, countDownLatch: CountDownLatch ) extends Runnable {
  val logger = Logger(this.getClass)
  override def run(): Unit = {
    val kafkaConsumer: KafkaConsumer[String, String] = KafkaUtils.createKafkaConsumer(kafkaBrokers, kafkaConsumerGroupId)
    kafkaConsumer.subscribe(java.util.Collections.singletonList(kafkaTopic))

    try {
      var recordEmpty: Boolean = true
      do {
        val records: ConsumerRecords[String, String] = kafkaConsumer.poll(1000)
        recordEmpty = records.asScala.isEmpty
        if (!records.isEmpty) {
          records.asScala.foreach((record: ConsumerRecord[String, String]) => process(record.value()))
        }
      } while (!recordEmpty)
    }
    catch {
      case x: Throwable =>
        x.printStackTrace()
        logger.info(s"exception")
    } finally {
      countDownLatch.countDown()
      kafkaConsumer.close()
      logger.info("processor finished")
    }
  }

  def process(record: String)
}
