package org.example.POS_Realtime.POJO

object EntityMapper {

  case class Product(PRD_CD:Int,PRD_NM:String,PRD_CAT_CD:Int,PRICE:String)
  case class Product_Purchased(PRD_LIST:List[Product], BILL_AMT:Double)
  case class Merchant(MRCH_CD:Int,MRCH_NM:String,MRCH_CAT_CD:Int)
  case class Location(LOC_ID:Int,LOC_NM:String,LOC_PIN:Int,LOC_STATE:String,LOC_CTRY:String)
  case class Invoice(invoiceNum:Int,merchant:Merchant,location:Location,productPurchased:Product_Purchased)

}
