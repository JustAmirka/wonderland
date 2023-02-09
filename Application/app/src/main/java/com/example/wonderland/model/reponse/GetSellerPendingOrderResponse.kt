package com.example.wonderland.model.reponse

import com.example.wonderland.model.entity.SellerPendingOrder
import com.google.gson.annotations.SerializedName

class GetSellerPendingOrderResponse {
    @SerializedName("orders") lateinit var orders:ArrayList<SellerPendingOrder>
}