package org.example.POS_Realtime.DataGenerator

import org.apache.logging.log4j.{LogManager, Logger}
import org.example.POS_Realtime.AppUtil.logPrefix
import org.example.POS_Realtime.POJO.EntityMapper.Invoice
import org.example.POS_Realtime.JSONserde.InvoiceSerializer

object InvoiceGenerator {

  val logger: Logger = LogManager.getLogger(this.getClass)

  logger.info(s"${logPrefix(this.getClass.getName)} - InvoiceGenerator Started" )

  def invoiceGenerator()={

    this.synchronized{
      val invoiceNum = scala.util.Random.nextInt(99999999)
      val invoice: Invoice = Invoice(invoiceNum,ConsumerDetailsGenerator.generateConsumerDetails,MerchantGenerator.merchantGenerator,LocationGenerator.locationGenerator,ProductGenerator.productGenerator)

      invoice
    //println(InvoiceSerializer.serializeInvoice(invoice))
    }
  }
}
