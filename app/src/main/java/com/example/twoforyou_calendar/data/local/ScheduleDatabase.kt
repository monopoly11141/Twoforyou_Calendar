package com.example.twoforyou_calendar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.twoforyou_calendar.data.model.Schedule

@Database(
    entities = [Schedule::class],
    version = 1,
    exportSchema = false
)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun ScheduleDao(): ScheduleDao

}