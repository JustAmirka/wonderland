package com.example.wonderland.model.entity

class CustomerHistory(
    val _id: String,
    val email: String,
    val product: ArrayList<CustomerBookHistory>,
    val status: String,
    val date: String,
    val total: Int,
    val phone:String,
    val address:String,
    val receiver:String
) {
    @Override
    override fun toString():String{
        return _id+" "+email+ " "+product.toString()+" "+total+" "+phone+" "+address+" "+receiver
    }
}