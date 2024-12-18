package com.example.stepcounter.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverter
import com.example.stepcounter.utils.Utils.createDefaultDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "Days")
data class Day(
    @androidx.room.PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "MaxSteps") var maxSteps: Int = 10000,
    @ColumnInfo(name = "StepsByHour") var stepsByHour: List<Int> =  List(24) { 0 },
    @ColumnInfo(name = "Date") var date: Date = createDefaultDate(),
){
    fun getSteps() = stepsByHour.sum()
}


class DateConverter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @TypeConverter
    fun stringToDate(dateString: String): Date? {
        return try {
            dateFormat.parse(dateString)
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun dateToString(date: Date): String {
        return dateFormat.format(date)
    }
}


class ListIntConverter {
    @TypeConverter
    fun fromString(value: String?): List<Int>? {
        return value?.split(",")?.map { it.toInt() }
    }

    @TypeConverter
    fun fromList(list: List<Int>?): String? {
        return list?.joinToString(",")
    }
}