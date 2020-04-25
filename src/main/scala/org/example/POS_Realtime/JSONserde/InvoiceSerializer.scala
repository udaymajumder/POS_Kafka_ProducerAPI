package org.example.POS_Realtime.JSONserde

import org.example.POS_Realtime.POJO.EntityMapper.Invoice
import org.example.POS_Realtime.POJO.EntityMapper.Product_Purchased
import org.example.POS_Realtime.POJO.EntityMapper.Location
import org.example.POS_Realtime.POJO.EntityMapper.Merchant
import org.example.POS_Realtime.POJO.EntityMapper.Product



object InvoiceSerializer {

  def serializeInvoice(invoice:Invoice):String={
    //Create Type Class
    trait JsonToString[T]{
      def JtoS(value:T):JSONValue
    }



    //Create Intermediate Types

    sealed trait JSONValue
    {
      def convertToString:String
    }

    final case class JSONnumber(value:Int) extends JSONValue
    {
      def convertToString =  value.toString
    }

    final case class JSONdouble(value:Double) extends JSONValue
    {
      def convertToString =  value.toString
    }

    final case class JSONstring(value:String) extends JSONValue
    {
      def convertToString = "\"" + value + "\""
    }

    final case class JSONlist(values:List[JSONValue]) extends JSONValue
    {
      def convertToString = values.map(value=>value.convertToString).mkString("[",",","]")
    }

    final case class JSONobject(value: Map[String,JSONValue]) extends JSONValue
    {
      def convertToString = value.map{ case(key,value1) => { "\"" + key + "\" : " + value1.convertToString }}.mkString("{" , "," , "}")
    }




    //Create Type Class Instance -> Type to JSON intermediate type
    implicit object IntSerializer extends JsonToString[Int]
    {
      def JtoS(value: Int): JSONValue = JSONnumber(value)
    }

    implicit object StringSerializer extends JsonToString[String]
    {
      def JtoS(value: String): JSONValue = JSONstring(value)
    }

    object MerchantSerializer extends JsonToString[Merchant]
    {
      def JtoS(value: Merchant): JSONValue = JSONobject(Map(
        ("MRCH_CD" -> JSONnumber(value.MRCH_CD)),
        ("MRCH_NM" -> JSONstring(value.MRCH_NM)),
        ("MRCH_CAT_CD" -> JSONnumber(value.MRCH_CAT_CD))
      ))
    }

    object LocationSerializer extends JsonToString[Location]
    {
      //case class Location(LOC_ID:Int,LOC_NM:String,LOC_PIN:Int,LOC_STATE:String,LOC_CTRY:String)
      def JtoS(value: Location): JSONValue = JSONobject(Map(
        ("LOC_ID" -> JSONnumber(value.LOC_ID)),
        ("LOC_NM" -> JSONstring(value.LOC_NM)),
        ("LOC_PIN" -> JSONnumber(value.LOC_PIN)),
        ("LOC_STATE" -> JSONstring(value.LOC_STATE)),
        ("LOC_CTRY" -> JSONstring(value.LOC_CTRY))
      ))
    }

    //case class Product(PRD_CD:Int,PRD_NM:String,PRD_CAT_CD:Int,PRICE:String)

    object productSerializer extends JsonToString[Product]
    {
      def JtoS(value:Product) = JSONobject(Map(
        ("PRD_CD" -> JSONnumber(value.PRD_CD)),
        ("PRD_NM" -> JSONstring(value.PRD_NM)),
        ("PRD_CAT_CD" -> JSONnumber(value.PRD_CAT_CD)),
        ("PRICE",JSONstring(value.PRICE))
      ))
    }

    object ProdPurchasedSerializer extends JsonToString[Product_Purchased]
    {
      //case class Product_Purchased(PRD_LIST:List[Product], BILL_AMT:Double)
      def JtoS(value:Product_Purchased):JSONValue={
        JSONobject(Map(
          ("PRD_LIST" -> JSONlist(value.PRD_LIST.map(product=>productSerializer.JtoS(product)))),
          ("BILL_AMT" -> JSONdouble(value.BILL_AMT))
        ))
      }
    }

    object InvoiceSerializer extends JsonToString[Invoice]
    {
      //case class Invoice(merchant:Merchant,location:Location,productPurchased:Product_Purchased)
      def JtoS(value:Invoice):JSONValue={
        JSONobject(Map(
          ("Invoice_Number" -> JSONnumber(value.invoiceNum)),
          ("Merchant_Detail" -> MerchantSerializer.JtoS(value.merchant)),
          ("Location_Detail" -> LocationSerializer.JtoS(value.location)),
          ("Billing_Detail" -> ProdPurchasedSerializer.JtoS(value.productPurchased))
        ))
      }
    }


    InvoiceSerializer.JtoS(invoice).convertToString

  }

}
