package org.example.POS_Realtime

import org.apache.kafka.clients.producer.{Producer, ProducerRecord}
import org.example.POS_Realtime.POJO.EntityMapper._
import org.example.POS_Realtime.DataGenerator.InvoiceGenerator
import org.example.POS_Realtime.JSONserde.InvoiceSerializer



class POSproducer(val id:Int, val producer:Producer[Int,String ],val topicName:String,val produceSpeed:Int) extends Runnable{

  def run(): Unit =
  {


      while(true)
      {
        val invoice: Invoice = InvoiceGenerator.invoiceGenerator()
        val invoiceSerialized: String = InvoiceSerializer.serializeInvoice(invoice)
        println(s"Generated Invoice: ${invoice.toString}")
        producer.send(new ProducerRecord [Int,String](topicName, invoice.invoiceNum, invoiceSerialized))
        println(s"Sent Record : ${invoiceSerialized}")
        Thread.sleep(produceSpeed)
      }

  }


  Runtime.getRuntime.addShutdownHook(new Thread(new Runnable {
    override def run(): Unit = println(s"Shutting Down Producer!!")
  }))


}
