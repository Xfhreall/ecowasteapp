package com.example.ecowasteapp.model

data class WasteItem(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val disposalInstruction: String,
    val recycleInfo: String,
    val binColorInfo: String
)
