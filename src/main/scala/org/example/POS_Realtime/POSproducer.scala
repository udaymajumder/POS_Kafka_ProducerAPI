package org.example.POS_Realtime

import org.apache.kafka.clients.producer.{Producer, ProducerRecord}
import org.apache.logging.log4j.{LogManager, Logger}
import org.example.POS_Realtime.AppUtil.logPrefix
import org.example.POS_Realtime.POJO.EntityMapper._
import org.example.POS_Realtime.DataGenerator.InvoiceGenerator
import org.example.POS_Realtime.JSONserde.InvoiceSerializer



class POSproducer(val id:Int, val producer:Producer[Int,String ],val topicName:String,val produceSpeed:Int) extends Runnable{

  val logger: Logger = LogManager.getLogger(this.getClass)

  logger.info(s"${logPrefix(this.getClass.getName)} - Kafka Producer : ${this.id} Started" )

  def run(): Unit =
  {
      while(true)
      {
        logger.info(s"${logPrefix(this.getClass.getName)} - Fetching Invoice Details from Kafka Producer : ${this.id}" )
        val invoice: Invoice = InvoiceGenerator.invoiceGenerator()
        val invoiceSerialized: String = InvoiceSerializer.serializeInvoice(invoice)
        println(s"Generated Invoice: ${invoice.toString}")
        producer.send(new ProducerRecord [Int,String](topicName, invoice.invoiceNum, invoiceSerialized))
        println(s"Sent Record : ${invoiceSerialized}")
        Thread.sleep(produceSpeed)
      }

  }


  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
    override def run(): Unit = logger.info(s"${logPrefix(this.getClass.getName)} - Kafka Producer ShutDown" )
  }))


}
