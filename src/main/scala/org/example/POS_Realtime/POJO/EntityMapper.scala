package org.example.POS_Realtime.POJO

object EntityMapper {

  case class Consumer(CON_ID:Int,NAME:String,GENDER:String,PHONE:String,PRM_IND:Boolean)
  case class ConsumerAddress(CON_ID:Int,ADDR_LINE:String,PIN:Int,STATE:String)
  case class ConsumerInvoiceDetail(CON_ID:Int,NAME:String,GENDER:String,PHONE:String,ADDR_LINE:String,PIN:Int,STATE:String,PRM_IND:Boolean)
  case class Product(PRD_CD:Int,PRD_NM:String,PRD_CAT_CD:Int,PRICE:String)
  case class Product_Cart(product:Product,OTY:Int=0)
  case class Product_Purchased(PRD_LIST:List[Product_Cart], BILL_AMT:Double)
  case class Merchant(MRCH_CD:Int,MRCH_NM:String,MRCH_CAT_CD:Int)
  case class Location(LOC_ID:Int,LOC_NM:String,LOC_PIN:Int,LOC_STATE:String,LOC_CTRY:String)
  case class Invoice(invoiceNum:Int,consumerDetails:ConsumerInvoiceDetail,merchant:Merchant,location:Location,productPurchased:Product_Purchased)

}
