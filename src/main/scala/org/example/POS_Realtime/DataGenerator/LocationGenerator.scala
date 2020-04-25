package org.example.POS_Realtime.DataGenerator

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.example.POS_Realtime.POJO.EntityMapper.Location

object LocationGenerator {

  var LOC_MAP=scala.collection.mutable.Map[Int,Location]()

  def locationGenerator ={

    if(LOC_MAP.isEmpty)
    {

      /**
       * Registering Jackson ObjectMapper
       */
      val objectMapper: ObjectMapper with ScalaObjectMapper = new ObjectMapper() with ScalaObjectMapper
      objectMapper.registerModule(DefaultScalaModule)

      val rPath = getClass.getResourceAsStream("/Datasets/Location.json")

      import scala.io._

      Source.fromInputStream(rPath).getLines()
        .toList.map(line=>{val REC=objectMapper.readValue[Location](line);REC})
        .foreach(REC => LOC_MAP += (REC.LOC_ID -> REC))
    }

    val REC_PICKED = scala.util.Random.nextInt(1000)
    val LOCVAL = LOC_MAP.get(REC_PICKED).fold(Location(0,"NA",0,"NA","NA"))(x=>x)

    LOCVAL
  }

}
