package com.example.bd.models

data class studentList(
    val title: String,
    val address: String,
    val rent: Float,
    val shared: Boolean,
    val smoke: Boolean,
    val accessible: Boolean,
    val sex: Int,
    val rooms: Int
) {
}