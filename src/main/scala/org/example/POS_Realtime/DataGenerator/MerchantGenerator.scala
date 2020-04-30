package org.example.POS_Realtime.DataGenerator

import org.example.POS_Realtime.POJO.EntityMapper.Merchant
import org.apache.logging.log4j.{LogManager, Logger}
import org.example.POS_Realtime.AppUtil.logPrefix
import org.example.POS_Realtime.DataGenerator.DataLoader._

object MerchantGenerator {

  val logger: Logger = LogManager.getLogger(this.getClass)

  logger.info(s"${logPrefix(this.getClass.getName)} - Initialized" )

  def merchantGenerator = {

        this.synchronized {
          val REC_PICKED = scala.util.Random.nextInt(1000)
          val MRCH = MRCH_MAP.get(REC_PICKED).fold(Merchant(0, "NA", 0))(x => x)
          MRCH
        }
  }

}
