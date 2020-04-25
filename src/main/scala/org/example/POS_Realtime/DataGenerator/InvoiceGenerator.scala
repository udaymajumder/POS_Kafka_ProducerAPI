package org.example.POS_Realtime.DataGenerator

import org.example.POS_Realtime.POJO.EntityMapper.Invoice
import org.example.POS_Realtime.JSONserde.InvoiceSerializer

object InvoiceGenerator {

  def invoiceGenerator()={

    val invoiceNum = scala.util.Random.nextInt(99999999)
    val invoice: Invoice = Invoice(invoiceNum,MerchantGenerator.merchantGenerator,LocationGenerator.locationGenerator,ProductGenerator.productGenerator)

    invoice
    //println(InvoiceSerializer.serializeInvoice(invoice))


  }

}
