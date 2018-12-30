package io.gtw.infrastructure.construction

import io.gtw.infrastructure.construction.utils.EnvUtils

case class Param (
                 kafkaBrokers: Option[String] = EnvUtils.kafkaBrokersFromEnv,
                 kafkaTopicItem: Option[String] = EnvUtils.kafkaTopicItemFromEnv,
                 kafkaTopicRelationship: Option[String] = EnvUtils.kafkaTopicRelationshipFromEnv,
                 kafkaTopicProperty: Option[String] = EnvUtils.kafkaTopicPropertyFromEnv,
                 kafkaItemConsumerThreadNum: Int = EnvUtils.kafkaItemConsumerThreadNumFromEnv,
                 kafkaRelationshipConsumerThreadNum: Int = EnvUtils.kafkaRelationshipConsumerThreadNumFromEnv,
                 kafkaPropertyConsumerThreadNum: Int = EnvUtils.kafkaPropertyConsumerThreadNumFromEnv,
                 kafkaItemConsumerGroupId: Option[String] = EnvUtils.kafkaItemConsumerGroupIdFromEnv,
                 kafkaRelationshipConsumerGroupId: Option[String] = EnvUtils.kafkaRelationshipConsumerGroupIdFromEnv,
                 kafkaPropertyConsumerGroupId: Option[String] = EnvUtils.kafkaPropertyConsumerGroupIdFromEnv
                 )
