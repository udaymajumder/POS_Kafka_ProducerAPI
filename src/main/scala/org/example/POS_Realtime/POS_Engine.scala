package org.example.POS_Realtime

import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.LogManager
import org.example.POS_Realtime.AppUtil._
import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.IntegerSerializer
import org.example.POS_Realtime.JSONserde.InvoiceSerializer
import org.example.POS_Realtime.POJO.EntityMapper._
import scala.concurrent.Promise
//import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._
//import scala.util.{Failure, Success}

object POS_Engine {

  def submitFuture():Unit = {

    seq_Future = for (i <- 1 to noOfProducers) yield
      new Thread(new POSproducer(i, kafkaProducer, topicName, produceSpeed, promise))

    seq_Future.foreach(_.start())
  }

  val logger: Logger = LogManager.getLogger(this.getClass)
  logger.info(s"${logPrefix(this.getClass.getName)} - POS Invoice Application Started" )
  val properties = new Properties

  private var topicName:String = _
  private var noOfProducers = 0
  private var produceSpeed = 0

  private var kafkaProducer:KafkaProducer[Int,Invoice] = _
  private var promise: Promise[Exception] = _
  private var seq_Future:Seq[Thread]= _


  def main(args: Array[String]): Unit = {


    logger.info(s"${logPrefix(this.getClass.getName)} - Initializing Kafka Producer" )

    topicName = AppConfig.topicName
    noOfProducers = AppConfig.noOfProducers
    produceSpeed = AppConfig.produceSpeed

    properties.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfig.applicationID)
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.bootstrapServers)
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[IntegerSerializer])
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[InvoiceSerializer])

    kafkaProducer = new KafkaProducer[Int,Invoice](properties)

    promise = Promise[Exception]

    submitFuture()

    promise.future.onFailure{case e:Exception => e.getStackTrace}

    Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
      override def run(): Unit = /* Cleanup jobs here If any*/
      logger.info(s"${logPrefix(this.getClass.getName)} - POS Invoice Application Shut Down" )
    }))

  }

}
