package com.music.mp3.spotify.mealcount.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromMap(value: Map<Int, Pair<Int, Int>>?): String {
        val gson = Gson()
        val type = object : TypeToken<Map<Int, Pair<Int, Int>>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toMap(value: String?): Map<Int, Pair<Int, Int>> {
        val gson = Gson()
        val type = object : TypeToken<Map<Int, Pair<Int, Int>>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromList(value: List<String>): String {
        val gson = Gson()
        val type = object : TypeToken<List<String>>(){}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toList(value: String?): List<String> {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}