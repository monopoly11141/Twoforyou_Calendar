package com.example.twoforyou_calendar.repository

import com.example.twoforyou_calendar.data.local.ScheduleDao
import com.example.twoforyou_calendar.data.model.Schedule
import javax.inject.Inject

class ScheduleRepository @Inject constructor(
    private val scheduleDao : ScheduleDao
) {

    fun getSchedule() =
        scheduleDao.getSchedule()

    fun getScheduleByDate(date : String) =
        scheduleDao.getScheduleByDate(date)

    suspend fun insertSchedule(schedule: Schedule) =
        scheduleDao.insertSchedule(schedule)

    suspend fun updateSchedule(schedule: Schedule) =
        scheduleDao.updateSchedule(schedule)

    suspend fun deleteAllSchedule() =
        scheduleDao.deleteAllSchedule()

    suspend fun deleteSchedule(schedule: Schedule) =
        scheduleDao.deleteSchedule(schedule)

    suspend fun upsertSchedule(schedule: Schedule) =
        scheduleDao.upsertSchedule(schedule)
}
