package com.example.wonderland.model.reponse

import com.example.wonderland.model.entity.Book
import com.google.gson.annotations.SerializedName

class GetFilterResultsResponse {
    @SerializedName("filterResults") lateinit var filterResults:ArrayList<Book>
}