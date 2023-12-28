package com.example.twoforyou_calendar.screen.home

import android.widget.CalendarView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.twoforyou_calendar.data.model.Schedule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scheduleListByDate = viewModel.scheduleListByDate.collectAsState().value

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val todayDate = LocalDateTime.now().format(formatter)!!

    val date = remember { mutableStateOf(todayDate) }

    val isAddSchedule = remember { mutableStateOf(false) }

    val isUpdateSchedule = remember { mutableStateOf(false) }

    val schedule = remember {
        mutableStateOf(
            Schedule(
                0,
                isDone = false,
                date = "",
                time = ""
            )
        )
    }

    var hour by remember { mutableIntStateOf(0) }
    var minute by remember { mutableIntStateOf(0) }

    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute,
        is24Hour = true
    )

    Column(

    ) {

        HorizontalCalendar(
            date,
            viewModel
        )

        Divider()

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            TodayDateText(date)

            AddScheduleButton(isAddSchedule)
        }

        ScheduleList(
            schedule,
            scheduleListByDate,
            isUpdateSchedule,
            viewModel
        )

        //DeleteButton
    }

    if (isAddSchedule.value or isUpdateSchedule.value) {
        CalendarDialog(
            isAddSchedule,
            isUpdateSchedule,
            schedule,
            timePickerState,
            date,
            viewModel
        )
    }


}

@Composable
fun ScheduleList(
    schedule: MutableState<Schedule>,
    scheduleListByDate: List<Schedule>,
    isUpdateSchedule: MutableState<Boolean>,
    viewModel: HomeViewModel
) {
    LazyColumn() {
        //TODO : Change this format
        items(scheduleListByDate.size) {
            schedule.value = scheduleListByDate[it]
            ScheduleItem(
                schedule,
                isUpdateSchedule,
                viewModel
            )
        }
    }
}

@Composable
fun ScheduleItem(
    schedule: MutableState<Schedule>,
    isUpdateSchedule: MutableState<Boolean>,
    viewModel: HomeViewModel,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = schedule.value.isDone,
            onCheckedChange = {
                viewModel.updateSchedule(
                    Schedule(
                        key = schedule.value.key,
                        isDone = !(schedule.value.isDone),
                        date = schedule.value.date,
                        time = schedule.value.time,
                        content = schedule.value.content
                    )
                )
            }
        )

        Text(schedule.value.time)

        Text(schedule.value.content)

        IconButton(onClick = {
            isUpdateSchedule.value = true
        }) {
            Icon(
                Icons.Filled.Edit,
                "Edit Schedule"
            )
        }

        IconButton(onClick = {
            viewModel.deleteSchedule(schedule.value)
        }) {
            Icon(
                Icons.Filled.Clear,
                "Remove Schedule"
            )
        }
    }
}

@Composable
fun HorizontalCalendar(
    date: MutableState<String>,
    viewModel: HomeViewModel
) {
    Column(
    ) {
        AndroidView(
            factory = {
                CalendarView(it)
            },
            modifier = Modifier
                .fillMaxWidth()
        ) { calendarView ->
            calendarView.setOnDateChangeListener { _, year, month, day ->

                date.value = "$year-${(month + 1)}-$day"

                viewModel.getScheduleByDate(date.value)
            }

        }

    }
}

@Composable
fun TodayDateText(
    date: MutableState<String>
) {
    Text(text = date.value)
}

@Composable
fun AddScheduleButton(
    isAddSchedule: MutableState<Boolean>
) {
    IconButton(onClick = {
        isAddSchedule.value = true
    }) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add button"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    isAddSchedule: MutableState<Boolean>,
    isUpdateSchedule: MutableState<Boolean>,
    schedule: MutableState<Schedule>,
    timePickerState: TimePickerState,
    date: MutableState<String>,
    viewModel: HomeViewModel,
) {
    var scheduleContent by remember { mutableStateOf(schedule.value.content) }

    if (isAddSchedule.value or isUpdateSchedule.value) {
        AlertDialog(
            onDismissRequest = {
                isAddSchedule.value = false
                isUpdateSchedule.value = false
            },
        ) {
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp.dp
            val screenWidth = configuration.screenWidthDp.dp

            val scaleBy = 0.7f

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .size(width = screenWidth * scaleBy, height = screenHeight * scaleBy)
                    .requiredSize(width = screenWidth, height = screenHeight)
                    .scale(scaleBy)
            ) {
                if (isAddSchedule.value) {
                    Text(text = "일정 추가")
                } else {
                    Text(text = "일정 수정")
                }

                TimePicker(
                    state = timePickerState,
                )

                TextField(
                    value = scheduleContent,
                    onValueChange = {
                        scheduleContent = it
                    },
                    placeholder = {
                        Text("일정")
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(onClick = {
                        isAddSchedule.value = false
                        isUpdateSchedule.value = false
                    }) {
                        Text("취소하기")
                    }

                    if (isAddSchedule.value) {
                        Button(onClick = {
                            viewModel.insertSchedule(
                                Schedule(
                                    0,
                                    false,
                                    date.value,
                                    formatTime(timePickerState.hour, timePickerState.minute),
                                    scheduleContent
                                )
                            )
                            isAddSchedule.value = false
                        }) {
                            Text("추가하기")
                        }
                    } else {
                        Button(onClick = {
                            schedule.value.time = formatTime(timePickerState.hour, timePickerState.minute)
                            schedule.value.content = scheduleContent
                            schedule.value.date = date.value
                            viewModel.updateSchedule(
                                schedule.value
                            )
                            isUpdateSchedule.value = false
                        }) {
                            Text("수정하기")
                        }
                    }

                }
            }
        }
    }

}

fun formatTime(hour : Int, minute : Int) : String {
    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}