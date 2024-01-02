package com.example.twoforyou_calendar.screen.home

import android.widget.CalendarView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Surface
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
import com.example.twoforyou_calendar.ui.theme.mainColor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val todayDate = LocalDateTime.now().format(formatter)!!

    val scheduleListByDate = viewModel.scheduleListByDate.collectAsState().value


    val date = remember { mutableStateOf(todayDate) }

    val isAddSchedule = remember { mutableStateOf(false) }

    var hour by remember { mutableIntStateOf(0) }
    var minute by remember { mutableIntStateOf(0) }

    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute,
        is24Hour = true
    )

    if (isAddSchedule.value) {
        CalendarDialog(
            "add",
            null,
            isAddSchedule,
            timePickerState,
            date.value,
            viewModel
        )
    }

    Surface(
        color = mainColor
    ) {
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
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TodayDateText(date)

                IconButton(onClick = {
                    isAddSchedule.value = true
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add button"
                    )
                }
            }

            ScheduleList(
                scheduleListByDate,
                timePickerState,
                viewModel
            )

        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleList(
    scheduleListByDate: List<Schedule>,
    timePickerState: TimePickerState,
    viewModel: HomeViewModel
) {
    LazyColumn() {
        items(scheduleListByDate.size) {
            ScheduleItem(
                scheduleListByDate[it],
                timePickerState,
                viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleItem(
    schedule: Schedule,
    timePickerState: TimePickerState,
    viewModel: HomeViewModel,
) {

    var openUpdateCalendarDialog by remember { mutableStateOf(false) }
    if (openUpdateCalendarDialog) {
        CalendarDialog(
            "update",
            schedule,
            null,
            timePickerState,
            schedule.date,
            viewModel
        )
    }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = schedule.isDone,
            onCheckedChange = {
                viewModel.updateSchedule(
                    Schedule(
                        key = schedule.key,
                        isDone = !(schedule.isDone),
                        date = schedule.date,
                        time = schedule.time,
                        content = schedule.content
                    )
                )
            }
        )

        Text(schedule.time)

        Text(schedule.content)

        IconButton(onClick = {
            //TODO : open edit schedule dialog
            openUpdateCalendarDialog = true
        }) {
            Icon(
                Icons.Filled.Edit,
                "Edit Schedule"
            )
        }

        IconButton(onClick = {
            viewModel.deleteSchedule(schedule)
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
    Text(
        text = date.value,
        modifier =
        Modifier
            .padding(10.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDialog(
    isAddOrUpdateSchedule: String,
    schedule: Schedule?,
    isAddSchedule: MutableState<Boolean>?,
    timePickerState: TimePickerState,
    date: String,
    viewModel: HomeViewModel,
) {
    var scheduleContent by remember { mutableStateOf(schedule?.content ?: "") }

    val closeDialog = remember { mutableStateOf(false) }
    if (closeDialog.value) {
        return
    }

    AlertDialog(
        onDismissRequest = {
            closeDialog.value = true
            isAddSchedule?.value = false
        }
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
            when (isAddOrUpdateSchedule) {
                "add" -> {
                    Text(text = "일정 추가")
                }

                "update" -> {
                    Text(text = "일정 수정")
                }

                else -> {

                }
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
                    closeDialog.value = true
                    isAddSchedule?.value = false
                }) {
                    Text("취소하기")
                }

                when (isAddOrUpdateSchedule) {
                    "add" -> {
                        Button(onClick = {
                            viewModel.insertSchedule(
                                Schedule(
                                    0,
                                    false,
                                    date,
                                    formatTime(timePickerState.hour, timePickerState.minute),
                                    scheduleContent
                                )
                            )

                            isAddSchedule?.value = false
                            closeDialog.value = true
                        }) {
                            Text("추가하기")
                        }
                    }

                    "update" -> {
                        Button(onClick = {

                            viewModel.updateSchedule(
                                Schedule(
                                    key = schedule!!.key,
                                    isDone = schedule.isDone,
                                    date = date,
                                    time = formatTime(timePickerState.hour, timePickerState.minute),
                                    content = scheduleContent
                                )
                            )

                            closeDialog.value = true
                        }) {
                            Text("수정하기")
                        }
                    }

                    else -> {

                    }
                }

            }

        }
    }

}

fun formatTime(hour: Int, minute: Int): String {
    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
}