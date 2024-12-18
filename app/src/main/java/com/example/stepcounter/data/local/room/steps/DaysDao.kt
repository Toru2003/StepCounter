package com.example.stepcounter.data.local.room.steps

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.stepcounter.domain.models.Day
import com.example.stepcounter.utils.Utils.createDefaultDate
import java.util.Date


@Dao
interface DaysDao {
    @Query("SELECT * FROM Days")
    suspend fun getAll(): List<Day>

    @Insert
    suspend fun insert(day: Day)

    @Delete
    suspend fun delete(day: Day)

    @Update
    suspend fun update(day: Day)

    @Query("SELECT COUNT(*) FROM Days")
    suspend fun getCount(): Int

    @Query("SELECT * FROM Days ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun getItems(offset: Int, limit: Int): List<Day>?

    @Query("SELECT * FROM Days WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Day?

    @Query("SELECT * FROM Days WHERE date = :date LIMIT 1")
    suspend fun getDay(date: Date): Day?
}
