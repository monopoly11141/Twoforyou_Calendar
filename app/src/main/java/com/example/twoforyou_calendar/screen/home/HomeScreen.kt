package com.example.twoforyou_calendar.screen.home

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.CalendarView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
                        TextField(
                            value = dialogContent,
                            onValueChange = { value ->
                                dialogContent = value
                            }
                        )

                    },
                    onDismissRequest = {
                        isOpenDialog = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                //TODO : viewmodel로 넘겨서 room에 저장.
                                isOpenDialog = false
                            }
                        ) {
                            Text("추가")
                        }

                    }
                )

            }


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
            }

        }


    }
}