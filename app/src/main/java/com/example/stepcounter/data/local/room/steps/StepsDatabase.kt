package com.example.stepcounter.data.local.room.steps

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.stepcounter.domain.models.DateConverter
import com.example.stepcounter.domain.models.Day
import com.example.stepcounter.domain.models.ListIntConverter

@Database(entities = [Day::class], version = 1)
@TypeConverters(DateConverter::class, ListIntConverter::class)
abstract class DaysDatabase : RoomDatabase() {
    abstract fun daysDao(): DaysDao
}
