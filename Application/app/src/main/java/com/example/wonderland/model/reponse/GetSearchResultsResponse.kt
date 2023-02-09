package com.example.wonderland.model.reponse

import com.example.wonderland.model.entity.Book
import com.google.gson.annotations.SerializedName

class GetSearchResultsResponse {
    @SerializedName("searchResults") lateinit var searchResults:ArrayList<Book>
}