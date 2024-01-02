package com.example.twoforyou_calendar.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.twoforyou_calendar.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
) {

    var splashText by remember { mutableStateOf("") }
    var splashFinishText = "쉬운 일정앱"

    Box(
        modifier = Modifier
            .padding(10.dp)
            .background(
                Color.Gray,
                shape = CircleShape
            )
            .aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = splashText,
            textAlign = TextAlign.Center,
            color = Color.White
        )

    }

    LaunchedEffect(key1 = true) {
        delay(500L)
        navController.navigate(route = Screen.HomeScreen.route)
    }

    LaunchedEffect(true) {
        while (splashFinishText.isNotEmpty()) {
            splashText += splashFinishText.first()
            splashFinishText = splashFinishText.drop(1)
            delay(100L)
        }
    }

}