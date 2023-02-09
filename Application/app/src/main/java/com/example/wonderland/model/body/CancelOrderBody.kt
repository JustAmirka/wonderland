package com.example.wonderland.model.body

import com.google.gson.annotations.SerializedName

class CancelOrderBody (
    @SerializedName("orderId") val orderId: String,
)