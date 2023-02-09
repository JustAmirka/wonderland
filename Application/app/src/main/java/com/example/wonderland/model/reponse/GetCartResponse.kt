package com.example.wonderland.model.reponse

import com.example.wonderland.model.entity.BooksInCart
import com.google.gson.annotations.SerializedName

class GetCartResponse {
    @SerializedName("email") val email:String=""
    @SerializedName("arrBooks") val arrBooks: ArrayList<BooksInCart>? = null
}