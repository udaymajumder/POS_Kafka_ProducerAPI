package org.example.POS_Realtime.DataGenerator

import java.util.concurrent.atomic.AtomicInteger

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.logging.log4j.{LogManager, Logger}
import org.example.POS_Realtime.AppUtil.logPrefix
import org.example.POS_Realtime.POJO.EntityMapper.{Consumer, ConsumerAddress, ConsumerInvoiceDetail}

import scala.util.control.Breaks._

object ConsumerDetailsGenerator {


  val logger: Logger = LogManager.getLogger(this.getClass)

  logger.info(s"${logPrefix(this.getClass.getName)} - ConsumerDetailsGenerator Started" )

  private var CONSUMER_MAP = scala.collection.mutable.Map[Int,Consumer]()
  private var CONSUMER_ADDR_MAP = scala.collection.mutable.Map[Int,ConsumerAddress]()


  def generateConsumerDetails()={

      if(CONSUMER_MAP.isEmpty || CONSUMER_ADDR_MAP.isEmpty)
        {
          val objectMapper: ObjectMapper with ScalaObjectMapper = new ObjectMapper() with ScalaObjectMapper
          objectMapper.registerModule(DefaultScalaModule)

          if(CONSUMER_MAP.isEmpty) {


            val rPath = getClass.getResourceAsStream("/Datasets/Consumer.json")

            import scala.io._

            Source.fromInputStream(rPath).getLines()
              .toList.map(line => {
              val REC = objectMapper.readValue[Consumer](line);
              REC
            })
              .foreach(REC => CONSUMER_MAP += (REC.CON_ID -> REC))
          }

          if(CONSUMER_ADDR_MAP.isEmpty)
            {
              val rPath = getClass.getResourceAsStream("/Datasets/ConsumerAddr.json")

              import scala.io._

              Source.fromInputStream(rPath).getLines()
                .toList.map(line => {
                val REC = objectMapper.readValue[ConsumerAddress](line);
                REC
              })
                .foreach(REC => CONSUMER_ADDR_MAP += (REC.CON_ID -> REC))
            }
        }

    this.synchronized {
      var r_consumer = 0
      var consumerInvoiceDetails:ConsumerInvoiceDetail = null
      while (r_consumer == 0) {
        r_consumer = scala.util.Random.nextInt(1000)
        val consumer = CONSUMER_MAP.getOrElse(r_consumer, Consumer(0, "NA", "NA", "NA", false))
        if (consumer.CON_ID != 0) {
          val consumerAddr = CONSUMER_ADDR_MAP.getOrElse(consumer.CON_ID, null)
          consumerInvoiceDetails = ConsumerInvoiceDetail(consumer.CON_ID, consumer.NAME, consumer.GENDER, consumer.PHONE, consumerAddr.ADDR_LINE, consumerAddr.PIN, consumerAddr.STATE, consumer.PRM_IND)
        }
      }
      consumerInvoiceDetails
    }
  }

}
