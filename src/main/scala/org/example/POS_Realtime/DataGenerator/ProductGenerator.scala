package org.example.POS_Realtime.DataGenerator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.example.POS_Realtime.POJO.EntityMapper.Product
import org.example.POS_Realtime.POJO.EntityMapper.Product_Purchased
import org.example.POS_Realtime.POJO.EntityMapper.Product_Cart

import org.apache.logging.log4j.{LogManager, Logger}
import org.example.POS_Realtime.AppUtil.logPrefix

import scala.util.control.Breaks._

object ProductGenerator {

  val logger: Logger = LogManager.getLogger(this.getClass)

  logger.info(s"${logPrefix(this.getClass.getName)} - ProductGenerator Started" )

  private var PRD_MAP=scala.collection.mutable.Map[Int,Product]()


  def productGenerator ={




    if(PRD_MAP.isEmpty)
    {

      /**
       * Registering Jackson ObjectMapper
       */
      val objectMapper: ObjectMapper with ScalaObjectMapper = new ObjectMapper() with ScalaObjectMapper
      objectMapper.registerModule(DefaultScalaModule)

      val rPath = getClass.getResourceAsStream("/Datasets/Product.json")

      import scala.io._
      /**
      import net.liftweb.json._
      import net.liftweb.json.DefaultFormats
      implicit val formats = DefaultFormats
      */
      Source.fromInputStream(rPath).getLines()
        .toList.map(line=>{val REC: Product = objectMapper.readValue[Product](line);REC})
        .foreach(REC => PRD_MAP += (REC.PRD_CD -> REC))
    }

    this.synchronized {

      var TOT_PRICE = 0.00
      var limit = 0
      var PRD_CRT=scala.collection.mutable.Buffer[Product_Cart]()
      var PRD_PURCHASED:Product_Purchased = null

      while (limit == 0) {
        limit = scala.util.Random.nextInt(5)
        if (limit != 0) {
          for (count <- 1 to limit) {
            val p_cd = scala.util.Random.nextInt(1000)
            val PRD = PRD_MAP.get(p_cd).fold(Product(0, "NA", 0, "$0.00"))(x => x)
            if (PRD.PRD_CD != 0) {
              val qty = scala.util.Random.nextInt(4)
              if (qty != 0) {
                PRD_CRT += Product_Cart(PRD, qty)
                TOT_PRICE += ((PRD.PRICE.substring(1).toDouble) * qty)
              }
            }
          }
        }
      }
      PRD_PURCHASED = Product_Purchased(PRD_CRT.toList, "%.2f".format(TOT_PRICE).toDouble)

      PRD_PURCHASED
    }
  }
}
