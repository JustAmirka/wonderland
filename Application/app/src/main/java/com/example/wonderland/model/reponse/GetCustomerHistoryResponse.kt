package com.example.wonderland.model.reponse

import com.example.wonderland.model.entity.CustomerHistory
import com.google.gson.annotations.SerializedName

class GetCustomerHistoryResponse {
    @SerializedName("orders") val arrHistory: ArrayList<CustomerHistory>? = null
}