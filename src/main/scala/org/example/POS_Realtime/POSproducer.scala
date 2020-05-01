package org.example.POS_Realtime

import org.apache.kafka.clients.producer.{Producer, ProducerRecord}
import org.apache.logging.log4j.{LogManager, Logger}
import org.example.POS_Realtime.AppUtil.logPrefix
import org.example.POS_Realtime.POJO.EntityMapper.Invoice
import org.example.POS_Realtime.DataGenerator.{DataLoader, InvoiceGenerator}

import scala.concurrent.Promise
import scala.util.Try


class POSproducer(val id:Int, val producer:Producer[Int,Invoice],val topicName:String,val produceSpeed:Int,promise:Promise[Exception]) extends Runnable{

  val logger: Logger = LogManager.getLogger(this.getClass)

  logger.info(s"${logPrefix(this.getClass.getName)} - Kafka Producer : ${this.id} Started" )

  def run(): Unit =
  {
      while(true)
      {
        logger.info(s"${logPrefix(this.getClass.getName)} - Fetching Invoice Details from Kafka Producer : ${this.id}" )
        DataLoader.dataLoader()
        val invoice: Invoice = InvoiceGenerator.invoiceGenerator()
        //val invoiceSerialized: String = InvoiceSerializer.serializeInvoice(invoice)
        println(s"Generated Invoice: ${invoice.toString}")
        producer.send(new ProducerRecord [Int,Invoice](topicName, invoice.invoiceNum, invoice))
        Thread.sleep(produceSpeed)
      }
    promise.failure(new Exception)
  }

  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
    override def run(): Unit = logger.info(s"${logPrefix(this.getClass.getName)} - Kafka Producer ShutDown" )
  }))


}
