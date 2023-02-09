package com.example.wonderland.model.entity

class Category (
    val name: String,
    val image: String,
) {

    override fun toString(): String {
        return "${name},${image}"
    }
}