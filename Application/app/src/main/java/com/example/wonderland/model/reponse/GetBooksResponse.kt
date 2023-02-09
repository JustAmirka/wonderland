package com.example.wonderland.model.reponse

import com.example.wonderland.model.entity.Book
import com.google.gson.annotations.SerializedName

class GetBooksResponse {
    @SerializedName("arrBook") val arrBook: ArrayList<Book>? = null
}