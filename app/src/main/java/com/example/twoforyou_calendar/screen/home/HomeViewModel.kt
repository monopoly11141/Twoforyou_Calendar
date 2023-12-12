package com.example.twoforyou_calendar.screen.home

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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ScheduleRepository
) : ViewModel() {

    private val _scheduleList = MutableStateFlow<List<Schedule>>(emptyList())
    val scheduleList = _scheduleList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSchedule().distinctUntilChanged()
                .collect { scheduleList ->
                    _scheduleList.value = scheduleList
                }
        }
    }

    fun getSchedule() = repository.getSchedule()

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

}