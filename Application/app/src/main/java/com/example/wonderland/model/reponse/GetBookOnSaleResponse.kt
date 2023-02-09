package com.example.wonderland.model.reponse

import com.example.wonderland.model.entity.Book
import com.google.gson.annotations.SerializedName

class GetBookOnSaleResponse {
    @SerializedName("arrBookOnSale") val arrBookOnSale: ArrayList<Book> = ArrayList()
}