package com.example.wonderland.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BooksInCart : Serializable {
    @SerializedName("_id") val _id:String=""
    @SerializedName("name") val name:String=""
    @SerializedName("price") val price:Int=0
    @SerializedName("img") val img:String=""
    @SerializedName("quantity")var quantity:Int=0
    @SerializedName("seller") val seller :String=""

    override fun toString(): String {
        return name+"-"+price
    }
}