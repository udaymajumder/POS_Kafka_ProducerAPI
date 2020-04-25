package org.example.POS_Realtime

import java.util
import java.util.Properties
import java.util.concurrent.Executors

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.{IntegerSerializer, StringSerializer}
import org.example.POS_Realtime.JSONserde.InvoiceSerializer



object POS_Engine {

  def main(args: Array[String]): Unit = {

  println(s"Hello Scala!!")

    val topicName = "invoice"
    val noOfProducers = 2
    val produceSpeed = 10000


    val properties = new Properties
    properties.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfig.applicationID)
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.bootstrapServers)
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[IntegerSerializer])
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])


    val kafkaProducer = new KafkaProducer[Int,String](properties)
    val executor = Executors.newFixedThreadPool(noOfProducers)
    val runnableProducers = new util.ArrayList[POSproducer]

    for (i <- 0 until noOfProducers) {
      val runnableProducer = new POSproducer(i, kafkaProducer, topicName, produceSpeed)
      runnableProducers.add(runnableProducer)
      executor.submit(runnableProducer)
    }

    Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
      override def run(): Unit = executor.shutdown()
    }))

  }

}
