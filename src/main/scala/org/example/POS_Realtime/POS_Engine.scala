package org.example.POS_Realtime

import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.LogManager

import org.example.POS_Realtime.AppUtil._

import java.util
import java.util.Properties
import java.util.concurrent.Executors

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.{IntegerSerializer, StringSerializer}




object POS_Engine {

  def main(args: Array[String]): Unit = {

    val logger: Logger = LogManager.getLogger(this.getClass)

    logger.info(s"${logPrefix(this.getClass.getName)} - POS Invoice Application Started" )

    val topicName = AppConfig.topicName
    val noOfProducers = AppConfig.noOfProducers
    val produceSpeed = AppConfig.produceSpeed

    logger.info(s"${logPrefix(this.getClass.getName)} - Initializing Kafka Producer" )
    val properties = new Properties
    properties.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfig.applicationID)
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.bootstrapServers)
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[IntegerSerializer])
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])


    val kafkaProducer = new KafkaProducer[Int,String](properties)
    val executor = Executors.newFixedThreadPool(noOfProducers)
    val runnableProducers = new util.ArrayList[POSproducer]

    for (i <- 0 until noOfProducers) {
      logger.info(s"${logPrefix(this.getClass.getName)} - Starting kafka Producer : ${i}" )
      val runnableProducer = new POSproducer(i, kafkaProducer, topicName, produceSpeed)
      runnableProducers.add(runnableProducer)
      executor.submit(runnableProducer)

    }

    Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
      override def run(): Unit = executor.shutdown()
      logger.info(s"${logPrefix(this.getClass.getName)} - POS Invoice Application Shut Down" )
    }))

  }

}
