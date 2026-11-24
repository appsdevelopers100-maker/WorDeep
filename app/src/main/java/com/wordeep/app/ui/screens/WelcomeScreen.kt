package com.wordeep.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wordeep.app.R
import com.wordeep.app.model.AppScreen
import com.wordeep.app.viewmodel.WordGameViewModel

@Composable
fun WelcomeScreen(viewModel: WordGameViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        // 1. Full-Screen Custom Artwork Background (launchpage.png)
        Image(
            painter = painterResource(id = R.drawable.launchpage),
            contentDescription = "Welcome Artwork Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // 2. MAIN "START PLAYING" BUTTON (Positioned directly over the orange puzzle piece)
        Box(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(75.dp)
                .align(Alignment.Center)
                .offset(y = 20.dp) // Aligns with the center orange puzzle piece on the artwork
                .clickable { viewModel.navigateTo(AppScreen.LEVEL_MAP) }
        )

        // 3. "HOW TO PLAY" & "FAQ" BUTTONS (Positioned just below Start Playing)
        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.Center)
                .offset(y = 125.dp), // Aligns with the middle-lower buttons
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(55.dp)
                    .clickable { viewModel.navigateTo(AppScreen.HOW_TO_PLAY) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(55.dp)
                    .clickable { viewModel.navigateTo(AppScreen.FAQ) }
            )
        }

        // 4. FOOTER LINKS ("Privacy Policy" & "Terms") (Positioned at the bottom white puzzle footer)
        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp), // Aligns with the bottom white puzzle footer
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp)
                    .clickable { viewModel.navigateTo(AppScreen.PRIVACY) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp)
                    .clickable { viewModel.navigateTo(AppScreen.TERMS) }
            )
        }
    }
}