package org.example.POS_Realtime

object AppConfig {

  val applicationID = "PosSimulator"
  val bootstrapServers = "localhost:9092"

  val topicName = "invoice"
  val noOfProducers = 2
  val produceSpeed = 20000

}
