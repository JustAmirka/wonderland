package com.example.wonderland.model.reponse

import com.example.wonderland.model.entity.Category
import com.google.gson.annotations.SerializedName

class GetCategoryResponse {
    @SerializedName("arrCategory") val arrCategory: ArrayList<Category> = ArrayList()
}