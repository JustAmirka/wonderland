package com.example.wonderland.model.reponse

import com.google.gson.annotations.SerializedName

class LoginResponse(
    @SerializedName("exitcode") val exitcode: Int,
    @SerializedName("token") val token: String,
)
