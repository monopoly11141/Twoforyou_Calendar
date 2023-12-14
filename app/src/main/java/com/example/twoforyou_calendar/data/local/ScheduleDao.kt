package com.example.twoforyou_calendar.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.twoforyou_calendar.data.model.Schedule
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedule_table")
    fun getSchedule(): Flow<List<Schedule>>

    @Query("SELECT * FROM schedule_table WHERE date=:date ORDER BY time ASC")
    fun getScheduleByDate(date : String): Flow<List<Schedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSchedule(schedule: Schedule)

    @Query("DELETE FROM schedule_table")
    suspend fun deleteAllSchedule()

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)

}