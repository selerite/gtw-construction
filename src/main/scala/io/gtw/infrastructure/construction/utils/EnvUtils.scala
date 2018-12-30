package io.gtw.infrastructure.construction.utils

import scala.util.Properties

object EnvUtils {
  def kafkaBrokersFromEnv: Option[String] = Properties.envOrSome("CONSTRUCTION_KAFKA_BROKERS", Some("10.131.40.191:9092"))
  def kafkaTopicItemFromEnv: Option[String] = Properties.envOrSome("CONSTRUCTION_KAFKA_TOPIC_ITEM", Some("items"))
  def kafkaTopicRelationshipFromEnv: Option[String] = Properties.envOrSome("CONSTRUCTION_KAFKA_TOPIC_RELATIONSHIP", Some("relationships"))
  def kafkaTopicPropertyFromEnv: Option[String] = Properties.envOrSome("CONSTRUCTION_KAFKA_TOPIC_PROPERTY", Some("properties"))
  def kafkaItemConsumerThreadNumFromEnv: Int = Properties.envOrElse("CONSTRUCTION_KAFKA_ITEM_CONSUMER_THREAD_NUM", "5").toInt
  def kafkaRelationshipConsumerThreadNumFromEnv: Int = Properties.envOrElse("CONSTRUCTION_KAFKA_RELATIONSHIP_CONSUMER_THREAD_NUM", "5").toInt
  def kafkaPropertyConsumerThreadNumFromEnv: Int = Properties.envOrElse("CONSTRUCTION_KAFKA_PROPERTY_CONSUMER_THREAD_NUM", "1").toInt
  def kafkaItemConsumerGroupIdFromEnv: Option[String] = Properties.envOrSome("CONSTRUCTION_KAFKA_ITEM_CONSUMER_GROUP_ID", Some("itemConsumer"))
  def kafkaRelationshipConsumerGroupIdFromEnv: Option[String] = Properties.envOrSome("CONSTRUCTION_KAFKA_RELATIONSHIP_CONSUMER_GROUP_ID", Some("relationshipConsumer"))
  def kafkaPropertyConsumerGroupIdFromEnv: Option[String] = Properties.envOrSome("CONSTRUCTION_KAFKA_PROPERTY_CONSUMER_GROUP_ID", Some("propertyConsumer"))
}
