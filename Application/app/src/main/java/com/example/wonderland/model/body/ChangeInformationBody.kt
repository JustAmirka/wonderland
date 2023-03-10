package com.example.wonderland.model.body

import com.google.gson.annotations.SerializedName

class ChangeInformationBody (
    @SerializedName("email") val email: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String
)