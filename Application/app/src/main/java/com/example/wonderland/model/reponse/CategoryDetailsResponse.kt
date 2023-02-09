package com.example.wonderland.model.reponse

import com.example.wonderland.model.entity.Book
import com.google.gson.annotations.SerializedName

class CategoryDetailsResponse {
    @SerializedName("books") private lateinit var books:ArrayList<Book>
}