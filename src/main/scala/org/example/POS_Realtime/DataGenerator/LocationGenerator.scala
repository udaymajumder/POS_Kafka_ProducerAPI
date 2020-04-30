package org.example.POS_Realtime.DataGenerator

import org.apache.logging.log4j.{LogManager, Logger}
import org.example.POS_Realtime.AppUtil.logPrefix
import org.example.POS_Realtime.POJO.EntityMapper.Location

import org.example.POS_Realtime.DataGenerator.DataLoader._

object LocationGenerator {

  val logger: Logger = LogManager.getLogger(this.getClass)

  logger.info(s"${logPrefix(this.getClass.getName)} - Initialized" )

  def locationGenerator ={

      this.synchronized {
        val REC_PICKED = scala.util.Random.nextInt(1000)
        val LOC = LOC_MAP.get(REC_PICKED).fold(Location(0, "NA", 0, "NA", "NA"))(x => x)
        LOC
      }
  }

}
