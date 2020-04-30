package org.example.POS_Realtime.DataGenerator

import org.apache.logging.log4j.{LogManager, Logger}
import org.example.POS_Realtime.AppUtil.logPrefix
import org.example.POS_Realtime.POJO.EntityMapper.{Consumer, ConsumerAddress, ConsumerInvoiceDetail}
import org.example.POS_Realtime.DataGenerator.DataLoader._
object ConsumerDetailsGenerator {


  val logger: Logger = LogManager.getLogger(this.getClass)

  logger.info(s"${logPrefix(this.getClass.getName)} - Initialized" )

  def generateConsumerDetails()={

    this.synchronized {
      var r_consumer = 0
      var consumerInvoiceDetails: ConsumerInvoiceDetail = null
      while (r_consumer == 0) {
        r_consumer = scala.util.Random.nextInt(1000)
        val consumer = CONSUMER_MAP.getOrElse(r_consumer, Consumer(0, "NA", "NA", "NA", false))
        if (consumer.CON_ID != 0) {
          val consumerAddr = CONSUMER_ADDR_MAP.getOrElse(consumer.CON_ID, null)
          consumerInvoiceDetails = ConsumerInvoiceDetail(consumer.CON_ID, consumer.NAME, consumer.GENDER, consumer.PHONE, consumerAddr.ADDR_LINE, consumerAddr.PIN, consumerAddr.STATE, consumer.PRM_IND)
        }
      }
      println(consumerInvoiceDetails.toString)
      consumerInvoiceDetails
    }

  }

}
