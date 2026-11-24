package com.wordeep.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wordeep.app.model.AppScreen
import com.wordeep.app.ui.screens.*
import com.wordeep.app.ui.theme.WorDeepTheme
import com.wordeep.app.viewmodel.WordGameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorDeepTheme {
                val gameViewModel: WordGameViewModel = viewModel()
                Surface(modifier = Modifier.fillMaxSize()) {
                    when (gameViewModel.currentScreen) {
                        AppScreen.SPLASH -> SplashScreen(gameViewModel)
                        AppScreen.WELCOME -> WelcomeScreen(gameViewModel)
                        AppScreen.LEVEL_MAP -> LevelMapScreen(gameViewModel)
                        AppScreen.GAMEPLAY -> GameplayScreen(gameViewModel)
                        AppScreen.EXAM -> ExamScreen(gameViewModel)
                        else -> {
                            // Fallback for FAQ, Privacy, Terms, and How to Play screens
                            WelcomeScreen(gameViewModel)
                        }
                    }
                }
            }
        }
    }
}
