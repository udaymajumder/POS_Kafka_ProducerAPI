package org.example.POS_Realtime.DataGenerator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.example.POS_Realtime.POJO.EntityMapper.Product
import org.example.POS_Realtime.POJO.EntityMapper.Product_Purchased

object ProductGenerator {

  var PRD_MAP=scala.collection.mutable.Map[Int,Product]()



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
        //.toList.map(line=>{val parsed_line: JValue =JsonParser.parse(line); val REC = parsed_line.extract[Product];REC})
        .foreach(REC => PRD_MAP += (REC.PRD_CD -> REC))
    }

    val REC_PICKED = scala.util.Random
    var PRD_CART = scala.collection.mutable.Buffer[Product]()
    var TOT_PRICE = 0.00
    for( count <- 1 to REC_PICKED.nextInt(5))
    {
      var p_cd = scala.util.Random.nextInt(1000)
      PRD_CART += PRD_MAP.get(p_cd).fold(Product(0,"NA",0,"$0.00"))(x=>x)
      TOT_PRICE += PRD_MAP.get(p_cd).fold(0.00){x => x.PRICE.substring(1).toDouble}
    }

    val PRD_PURCHASED:Product_Purchased = Product_Purchased(PRD_CART.toList,"%.2f".format(TOT_PRICE).toDouble)

    /**
    implicit val formats = DefaultFormats
    val jsonStr=net.liftweb.json.Serialization.write(PRD_PURCHASED)
    println(jsonStr)
     */

    PRD_PURCHASED
  }





}
