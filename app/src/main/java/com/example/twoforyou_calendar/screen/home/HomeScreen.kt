package com.example.twoforyou_calendar.screen.home

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.CalendarView
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController
) {
    HorizontalCalendar()
}

@Composable
fun HorizontalCalendar(){
    AndroidView(
        factory = {
            CalendarView(it)
        }
    ) {calendarView ->
//        calendarView.setOnDateChangeListener{
//
//        }
    }
}