package io.gtw.infrastructure.construction.utils

import java.util.Properties

import org.apache.kafka.clients.consumer.KafkaConsumer

object KafkaUtils {
  def createKafkaConsumer(kafkaBrokers: String, KafkaItemConsumerGroupId: String): KafkaConsumer[String, String] = {
    val properties: Properties = new Properties()
    properties.setProperty("bootstrap.servers", kafkaBrokers)
    properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    properties.setProperty("enable.auto.commit", "true")
    properties.setProperty("auto.commit.interval.ms", "1000")
    properties.setProperty("max.partition.fetch.bytes", "10485760")
    properties.setProperty("auto.offset.reset", "earliest")
    properties.setProperty("group.id", KafkaItemConsumerGroupId)
    new KafkaConsumer[String, String](properties)
  }
}
