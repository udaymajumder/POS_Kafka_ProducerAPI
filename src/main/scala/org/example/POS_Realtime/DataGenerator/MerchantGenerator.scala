package org.example.POS_Realtime.DataGenerator

import org.example.POS_Realtime.POJO.EntityMapper.Merchant
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.logging.log4j.{LogManager, Logger}
import org.example.POS_Realtime.AppUtil.logPrefix

object MerchantGenerator {

  val logger: Logger = LogManager.getLogger(this.getClass)

  logger.info(s"${logPrefix(this.getClass.getName)} - MerchantGenerator Started" )

  private var MRCH_MAP=scala.collection.mutable.Map[Int,Merchant]()

  def merchantGenerator ={



    if(MRCH_MAP.isEmpty)
    {

      /**
       * Registering Jackson ObjectMapper
       */
      val objectMapper: ObjectMapper with ScalaObjectMapper = new ObjectMapper() with ScalaObjectMapper
      objectMapper.registerModule(DefaultScalaModule)

      val rPath = getClass.getResourceAsStream("/Datasets/Merchant.json")

      import scala.io._

      Source.fromInputStream(rPath).getLines()
        .toList.map(line=>{val REC=objectMapper.readValue[Merchant](line);REC})
        .foreach(REC => MRCH_MAP += (REC.MRCH_CD -> REC))
    }

    this.synchronized {
      val REC_PICKED = scala.util.Random.nextInt(1000)
      val MRCHVAL = MRCH_MAP.get(REC_PICKED).fold(Merchant(0, "NA", 0))(x => x)

      MRCHVAL
    }
  }

}
