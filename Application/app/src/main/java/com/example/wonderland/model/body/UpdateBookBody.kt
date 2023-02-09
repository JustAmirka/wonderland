package com.example.wonderland.model.body

class UpdateBookBody(
    val id:String,
    val author:String,
    val description:String,
    val distributor:String,
    val saleprice:Int=0,
    val quantity:Int=0,
    val price:Int=0
) {

}