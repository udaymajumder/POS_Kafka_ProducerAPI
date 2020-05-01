package org.example.POS_Realtime.JSONserde

import java.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.kafka.common.serialization.Serializer
import org.example.POS_Realtime.POJO.EntityMapper.Invoice

class InvoiceSerializer extends Serializer[Invoice]
{

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = super.configure(configs, isKey)

  override def serialize(topic: String, data: Invoice): Array[Byte] = {
    if(data==null)
      null
    else
    {
      val mapper = new ObjectMapper with ScalaObjectMapper
      mapper.registerModule(DefaultScalaModule)
      mapper.writeValueAsString(data).getBytes
    }
  }

  override def close(): Unit = super.close()
}
