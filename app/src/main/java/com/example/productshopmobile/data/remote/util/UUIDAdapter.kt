package com.example.productshopmobile.data.remote.util

import com.squareup.moshi.*
import java.util.*

class UUIDAdapter : JsonAdapter<UUID>() {
    @FromJson
    override fun fromJson(reader: JsonReader): UUID? {
        return reader.nextString()?.let { UUID.fromString(it) }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: UUID?) {
        writer.value(value?.toString())
    }
}