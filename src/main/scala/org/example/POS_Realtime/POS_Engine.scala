package org.example.POS_Realtime

import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.LogManager
import org.example.POS_Realtime.AppUtil._
import java.util.Properties


import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.{IntegerSerializer, StringSerializer}

import scala.concurrent.Promise
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
import scala.util.{Failure, Success}

object POS_Engine {

  def submitFuture() = {

    seq_Future = for (i <- 1 to noOfProducers) yield
      new Thread(new POSproducer(i, kafkaProducer, topicName, produceSpeed, promise))

    seq_Future.foreach(_.start())
  }

  val logger: Logger = LogManager.getLogger(this.getClass)
  logger.info(s"${logPrefix(this.getClass.getName)} - POS Invoice Application Started" )
  val properties = new Properties

  val topicName = AppConfig.topicName
  val noOfProducers = AppConfig.noOfProducers
  val produceSpeed = AppConfig.produceSpeed

  private var kafkaProducer:KafkaProducer[Int,String] = null
  val promise = Promise[Exception]
  private var seq_Future:Seq[Thread]= null


  def main(args: Array[String]): Unit = {

    logger.info(s"${logPrefix(this.getClass.getName)} - Initializing Kafka Producer" )

    properties.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfig.applicationID)
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.bootstrapServers)
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[IntegerSerializer])
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])

    kafkaProducer = new KafkaProducer[Int,String](properties)

    submitFuture()

    promise.future.onFailure{case e:Exception => e.getStackTrace}

    Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
      override def run(): Unit = /* Cleanup jobs here If any*/
      logger.info(s"${logPrefix(this.getClass.getName)} - POS Invoice Application Shut Down" )
    }))

  }

}
