package com.example.todoleloup.list

import kotlinx.serialization.Serializable

@Serializable
data class Task (
    val id: String,
    val content: String,
    val description: String = "No description"
) : java.io.Serializable

