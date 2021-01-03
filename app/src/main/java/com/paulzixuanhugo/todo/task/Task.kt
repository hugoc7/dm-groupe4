package com.paulzixuanhugo.todo.task


import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class Task(
        @SerialName("id")
        val id: String,
        @SerialName("title")
        val title: String = "no title",
        @SerialName("description")
        val description: String = "no desc",
        @SerialName("due_date")
        @Serializable(with = DateSerializer::class)
        val dueDate: Date
) : java.io.Serializable

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
        private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US)

        override fun serialize(encoder: Encoder, value: Date) =
                encoder.encodeString(formatter.format(value))

        override fun deserialize(decoder: Decoder): Date =
                formatter.parse(decoder.decodeString())
}