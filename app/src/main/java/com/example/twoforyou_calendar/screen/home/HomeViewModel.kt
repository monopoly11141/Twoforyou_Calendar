package com.example.twoforyou_calendar.screen.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twoforyou_calendar.data.model.Schedule
import com.example.twoforyou_calendar.repository.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel() {

    private val _scheduleList = MutableStateFlow<List<Schedule>>(emptyList())
    val scheduleList = _scheduleList.asStateFlow()

    private val _scheduleListByDate = MutableStateFlow<List<Schedule>>(emptyList())
    val scheduleListByDate = _scheduleListByDate.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSchedule().distinctUntilChanged()
                .collect { scheduleList ->
                    _scheduleList.value = scheduleList
                }
        }
        viewModelScope.launch(Dispatchers.IO) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val currentDate = LocalDateTime.now().format(formatter)
            Log.d(TAG, "HomeViewModel: ${currentDate}")
            repository.getScheduleByDate(currentDate)
                .collect { scheduleList ->
                    Log.d(TAG, "HomeViewModel: ${scheduleList}")
                    _scheduleListByDate.value = scheduleList
                }
        }


        //getScheduleByDate(currentDate)

    }

    fun getSchedule() = repository.getSchedule()

    fun getScheduleByDate(date : String) = viewModelScope.launch(Dispatchers.IO) {
        repository.getScheduleByDate(date).distinctUntilChanged()
            .collect { scheduleList ->
                _scheduleListByDate.value = scheduleList
            }
    }

    fun insertSchedule(schedule: Schedule) = viewModelScope.launch {
        repository.insertSchedule(schedule)
    }

    fun updateSchedule(schedule: Schedule) = viewModelScope.launch() {
        repository.updateSchedule(schedule)
    }

    fun deleteAllSchedule() = viewModelScope.launch {
        repository.deleteAllSchedule()
    }

    fun deleteSchedule(schedule: Schedule) = viewModelScope.launch {
        repository.deleteSchedule(schedule)
    }

    fun upsertSchedule(schedule: Schedule) = viewModelScope.launch {
        repository.upsertSchedule(schedule)
    }

}