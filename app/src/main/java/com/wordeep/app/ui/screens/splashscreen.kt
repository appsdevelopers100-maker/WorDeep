package com.wordeep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordeep.app.model.AppScreen
import com.wordeep.app.viewmodel.WordGameViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: WordGameViewModel) {
    LaunchedEffect(Unit) {
        delay(3500)
        viewModel.navigateTo(AppScreen.WELCOME)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "WorDig",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Learn & Play",
                color = Color(0xFF38BDF8),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}