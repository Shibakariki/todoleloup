package com.example.todoleloup.list

import java.io.Serializable

data class Task (
    val id: String,
    val content: String,
    val description: String = "No description"
) : Serializable

