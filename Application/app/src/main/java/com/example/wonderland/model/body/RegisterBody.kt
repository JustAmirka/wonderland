package com.example.wonderland.model.body

import com.google.gson.annotations.SerializedName

class RegisterBody (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String
)