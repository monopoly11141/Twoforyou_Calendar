package com.example.twoforyou_calendar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_table")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val key : Int,
    val date: String,
    val content: String = ""
)
