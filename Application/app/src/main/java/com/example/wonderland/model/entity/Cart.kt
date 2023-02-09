package com.example.wonderland.model.entity

import com.example.wonderland.model.reponse.GetCartResponse

class Cart(
    val email:String,
    val arrBooks:ArrayList<BooksInCart>?=null
) {
    constructor(response: GetCartResponse) : this(
        response.email,
        response.arrBooks
    )
}