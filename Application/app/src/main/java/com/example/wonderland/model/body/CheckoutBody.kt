package com.example.wonderland.model.body
import com.example.wonderland.model.entity.BooksInCart
import com.google.gson.annotations.SerializedName

class CheckoutBody(
    @SerializedName("arrBooks")val arrBooks:ArrayList<BooksInCart>,
    @SerializedName("email")val email:String="",
    @SerializedName("address")val address:String="",
    @SerializedName("phone")val phone:String=""
)