package com.paulzixuanhugo.todo.task

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
        @SerialName("id")
        val id: String,
        @SerialName("title")
        val title: String = "no title",
        @SerialName("description")
        val description: String = "no desc"
) : java.io.Serializable