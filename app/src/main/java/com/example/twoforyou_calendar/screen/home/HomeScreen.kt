package com.example.twoforyou_calendar.screen.home

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.CalendarView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
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
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val currentTime = LocalDateTime.now().format(formatter)

    val currentTimeArray = currentTime.split("-")

    var yearString = remember { mutableStateOf(currentTimeArray[0]) }
    var monthString = remember { mutableStateOf(currentTimeArray[1]) }
    var dayString = remember { mutableStateOf(currentTimeArray[2]) }

    var isOpenDialog by remember { mutableStateOf(false) }

    var dialogContent by remember { mutableStateOf("") }

    var hour by remember { mutableIntStateOf(0) }
    var minute by remember { mutableIntStateOf(0) }

    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute,
        is24Hour = true
    )

    val scheduleListByDate = viewModel.scheduleListByDate.collectAsState().value

    Column(

    ) {
        HorizontalCalendar(
            viewModel,
            yearString,
            monthString,
            dayString
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text("${yearString.value}-${monthString.value}-${dayString.value}")

            IconButton(onClick = {
                Log.d(TAG, "onClick: clicked")
                isOpenDialog = true
            }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add button"
                )
            }

            if (isOpenDialog) {

                AlertDialog(
                    title = {
                        Text(text = "일정 추가하기")
                    },
                    text = {
                        Column() {
                            TextField(
                                value = dialogContent,
                                onValueChange = { value ->
                                    dialogContent = value
                                },
                            )
                            TimePicker(
                                state = timePickerState,
                                modifier = Modifier
                                    .scale(0.8f)
                            )

                        }

                    },
                    onDismissRequest = {
                        isOpenDialog = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val schedule = Schedule(
                                    key = 0,
                                    date = "${yearString.value}-${monthString.value}-${dayString.value}",
                                    time = "${timePickerState.hour}:${timePickerState.minute}",
                                    content = dialogContent
                                )
                                viewModel.insertSchedule(schedule)

                                isOpenDialog = false
                            }
                        ) {
                            Text("추가")
                        }

                    }
                )

            }
        }


        LazyColumn() {
            //TODO : Change this format
            items(scheduleListByDate.size) {
                val schedule = scheduleListByDate[it]
                ScheduleBeautify(schedule)
            }
        }

        Button(onClick = { viewModel.deleteAllSchedule() }) {
            Text("Delete All")
        }
    }
}


@Composable
fun HorizontalCalendar(
    viewModel: HomeViewModel,
    yearString: MutableState<String>,
    monthString: MutableState<String>,
    dayString: MutableState<String>,
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
                yearString.value = year.toString()
                monthString.value = (month + 1).toString()
                dayString.value = day.toString()

                val date = "${yearString.value}-${monthString.value}-${dayString.value}"

                viewModel.getScheduleByDate(date)
            }

        }

    }
}

@Composable
fun ScheduleBeautify(schedule: Schedule) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(schedule.time)

        Text(schedule.content)

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                Icons.Filled.Edit,
                "Edit Schedule"
            )
        }

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                Icons.Filled.Clear,
                "Remove Schedule"
            )
        }
    }
}