package com.example.twoforyou_calendar.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_table")
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val key : Int = 0,
    var isDone : Boolean,
    var date: String,
    var time : String,
    var content: String = ""
)
