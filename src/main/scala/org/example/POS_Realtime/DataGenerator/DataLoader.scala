package org.example.POS_Realtime.DataGenerator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.logging.log4j.{LogManager, Logger}
import org.example.POS_Realtime.AppUtil.logPrefix
import org.example.POS_Realtime.POJO.EntityMapper.{Consumer, ConsumerAddress, Location, Merchant, Product}

import scala.io._

object DataLoader {

  val logger: Logger = LogManager.getLogger(this.getClass)

  logger.info(s"${logPrefix(this.getClass.getName)} - Initialized" )

  var CONSUMER_MAP = scala.collection.mutable.Map[Int,Consumer]()
  var CONSUMER_ADDR_MAP = scala.collection.mutable.Map[Int,ConsumerAddress]()
  var LOC_MAP=scala.collection.mutable.Map[Int,Location]()
  var PRD_MAP=scala.collection.mutable.Map[Int,Product]()
  var MRCH_MAP=scala.collection.mutable.Map[Int,Merchant]()

  val objectMapper: ObjectMapper with ScalaObjectMapper = new ObjectMapper() with ScalaObjectMapper
  objectMapper.registerModule(DefaultScalaModule);

  def dataLoader(): Unit =
  {

    if(CONSUMER_MAP.isEmpty) {

      val rPath = getClass.getResourceAsStream("/Datasets/Consumer.json")

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

      Source.fromInputStream(rPath).getLines()
        .toList.map(line => {
        val REC = objectMapper.readValue[ConsumerAddress](line);
        REC
      })
        .foreach(REC => CONSUMER_ADDR_MAP += (REC.CON_ID -> REC))
    }

    if(PRD_MAP.isEmpty)
    {
      val rPath = getClass.getResourceAsStream("/Datasets/Product.json")

      Source.fromInputStream(rPath).getLines()
        .toList.map(line=>{val REC: Product = objectMapper.readValue[Product](line);REC})
        .foreach(REC => PRD_MAP += (REC.PRD_CD -> REC))
    }

    if(MRCH_MAP.isEmpty)
    {

      val rPath = getClass.getResourceAsStream("/Datasets/Merchant.json")

      Source.fromInputStream(rPath).getLines()
        .toList.map(line=>{val REC=objectMapper.readValue[Merchant](line);REC})
        .foreach(REC => MRCH_MAP += (REC.MRCH_CD -> REC))
    }

    if(LOC_MAP.isEmpty)
    {
      val rPath = getClass.getResourceAsStream("/Datasets/Location.json")

      Source.fromInputStream(rPath).getLines()
        .toList.map(line=>{val REC=objectMapper.readValue[Location](line);REC})
        .foreach(REC => LOC_MAP += (REC.LOC_ID -> REC))
    }

  }

  logger.info(s"${logPrefix(this.getClass.getName)} - Finished" )

}
