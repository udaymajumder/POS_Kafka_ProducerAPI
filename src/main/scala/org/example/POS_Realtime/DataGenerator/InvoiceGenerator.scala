package org.example.POS_Realtime.DataGenerator

import org.apache.logging.log4j.{LogManager, Logger}
import org.example.POS_Realtime.AppUtil.logPrefix
import org.example.POS_Realtime.POJO.EntityMapper.Invoice
import org.example.POS_Realtime.JSONserde.InvoiceSerializer
import org.example.POS_Realtime.POJO.EntityMapper._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
object InvoiceGenerator {

  val logger: Logger = LogManager.getLogger(this.getClass)

  logger.info(s"${logPrefix(this.getClass.getName)} - Initialized" )

  def invoiceGenerator()={

    var consumerDetails  = ConsumerDetailsGenerator.generateConsumerDetails
    var merchantDetails = MerchantGenerator.merchantGenerator
    var locationDetails = LocationGenerator.locationGenerator
    var productDetails = ProductGenerator.productGenerator


    this.synchronized{
      val invoiceNum = scala.util.Random.nextInt(99999999)
      val invoice: Invoice = Invoice(invoiceNum,consumerDetails,merchantDetails,locationDetails,productDetails)

      invoice
    }
  }
}
