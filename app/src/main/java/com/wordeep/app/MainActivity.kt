package com.wordeep.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wordeep.app.model.AppScreen
import com.wordeep.app.model.DailyMissionsManager
import com.wordeep.app.ui.screens.DailyMissionsDialog
import com.wordeep.app.ui.screens.ExamScreen
import com.wordeep.app.ui.screens.FAQScreen
import com.wordeep.app.ui.screens.GameplayScreen
import com.wordeep.app.ui.screens.HowToPlayScreen
import com.wordeep.app.ui.screens.LevelMapScreen
import com.wordeep.app.ui.screens.PrivacyScreen
import com.wordeep.app.ui.screens.SplashScreen
import com.wordeep.app.ui.screens.TermsScreen
import com.wordeep.app.ui.screens.WelcomeScreen
import com.wordeep.app.ui.theme.WorDeepTheme
import com.wordeep.app.viewmodel.WordGameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorDeepTheme {
                val viewModel: WordGameViewModel = viewModel()
                val currentScreen by viewModel.currentScreen.collectAsState()
                val difficulty by viewModel.difficulty.collectAsState()
                val timeTrialEnabled by viewModel.timeTrialEnabled.collectAsState()
                val levelTimeLimitSeconds by viewModel.levelTimeLimitSeconds.collectAsState()
                val timeRemainingSeconds by viewModel.timeRemainingSeconds.collectAsState()
                val isTimeUp by viewModel.isTimeUp.collectAsState()
                val lastSpeedBonus by viewModel.lastSpeedBonus.collectAsState()
                val score by viewModel.score.collectAsState()
                val unlockedLevel by viewModel.unlockedLevel.collectAsState()
                val currentLevelIndex by viewModel.currentLevelIndex.collectAsState()
                val levels by viewModel.levels.collectAsState()
                val selectedLetterIndices by viewModel.selectedLetterIndices.collectAsState()
                val currentExtractedWords by viewModel.currentExtractedWords.collectAsState()
                val discoveredExtraWords by viewModel.discoveredExtraWords.collectAsState()
                val extraWordFeedback by viewModel.extraWordFeedback.collectAsState()
                val canClaimFormedExtraWord by viewModel.canClaimFormedExtraWord.collectAsState()
                val isFormedExtraAlreadyClaimed by viewModel.isFormedExtraAlreadyClaimed.collectAsState()
                val isLevelComplete by viewModel.isLevelComplete.collectAsState()
                val soundEnabled by viewModel.soundEnabled.collectAsState()
                val dailyMissions by viewModel.dailyMissions.collectAsState()
                var showDailyMissions by remember { mutableStateOf(false) }

                val claimableMissionsCount = DailyMissionsManager.getClaimableCount(dailyMissions)

                val activeExamNumber by viewModel.activeExamNumber.collectAsState()
                val activeExamQuestions by viewModel.activeExamQuestions.collectAsState()
                val examBestScores by viewModel.examBestScores.collectAsState()
                val currentExamIndex by viewModel.currentExamIndex.collectAsState()
                val examScore by viewModel.examScore.collectAsState()
                val examLastEarnedDelta by viewModel.examLastEarnedDelta.collectAsState()
                val examCompleted by viewModel.examCompleted.collectAsState()
                val examEnteredFromMap by viewModel.examEnteredFromMap.collectAsState()

                val openMissionsDialog: () -> Unit = {
                    viewModel.refreshDailyMissionsIfNewDay()
                    showDailyMissions = true
                }

                when (currentScreen) {
                    AppScreen.SPLASH -> SplashScreen(
                        onTimeout = { viewModel.navigateTo(AppScreen.WELCOME) }
                    )

                    AppScreen.WELCOME -> WelcomeScreen(
                        difficulty = difficulty,
                        timeTrialEnabled = timeTrialEnabled,
                        claimableMissionsCount = claimableMissionsCount,
                        onSelectDifficulty = { selected -> viewModel.setDifficulty(selected) },
                        onToggleTimeTrial = { enabled -> viewModel.setTimeTrialEnabled(enabled) },
                        onOpenDailyMissions = openMissionsDialog,
                        onNavigate = { screen -> viewModel.navigateTo(screen) }
                    )

                    AppScreen.LEVEL_MAP -> LevelMapScreen(
                        levels = levels,
                        unlockedLevel = unlockedLevel,
                        score = score,
                        examBestScores = examBestScores,
                        claimableMissionsCount = claimableMissionsCount,
                        onBack = { viewModel.navigateTo(AppScreen.WELCOME) },
                        onSelectLevel = { index -> viewModel.selectLevel(index) },
                        onStartExam = { examNum -> viewModel.startExam(examNum, fromMap = true) },
                        onOpenDailyMissions = openMissionsDialog
                    )

                    AppScreen.GAMEPLAY -> {
                        val currentLevel = levels.getOrElse(currentLevelIndex) { levels.first() }
                        GameplayScreen(
                            level = currentLevel,
                            levelNumber = currentLevelIndex + 1,
                            totalLevels = levels.size,
                            difficulty = difficulty,
                            timeTrialEnabled = timeTrialEnabled,
                            levelTimeLimitSeconds = levelTimeLimitSeconds,
                            timeRemainingSeconds = timeRemainingSeconds,
                            isTimeUp = isTimeUp,
                            lastSpeedBonus = lastSpeedBonus,
                            extraWordPoints = if (timeTrialEnabled) {
                                WordGameViewModel.TIME_TRIAL_EXTRA_WORD_POINTS
                            } else {
                                WordGameViewModel.EXTRA_WORD_POINTS
                            },
                            score = score,
                            selectedLetterIndices = selectedLetterIndices,
                            extractedWords = currentExtractedWords,
                            discoveredExtraWords = discoveredExtraWords,
                            extraWordFeedback = extraWordFeedback,
                            canClaimFormedExtraWord = canClaimFormedExtraWord,
                            isFormedExtraAlreadyClaimed = isFormedExtraAlreadyClaimed,
                            isLevelComplete = isLevelComplete,
                            soundEnabled = soundEnabled,
                            onBack = { viewModel.navigateTo(AppScreen.LEVEL_MAP) },
                            onToggleSound = { viewModel.toggleSound() },
                            onSpeakWord = { word -> viewModel.speakWord(word) },
                            onLetterClick = { idx -> viewModel.onLetterClicked(idx) },
                            onClaimExtraWord = { viewModel.claimFormedExtraWord() },
                            onClearSelection = { viewModel.clearSelection() },
                            onUseHint = { viewModel.useHint() },
                            onRetryLevel = { viewModel.retryCurrentLevel() },
                            onNextLevel = { viewModel.nextLevel() }
                        )
                    }

                    AppScreen.EXAM -> ExamScreen(
                        examNumber = activeExamNumber,
                        questions = activeExamQuestions,
                        currentExamIndex = currentExamIndex,
                        examScore = examScore,
                        bestScore = examBestScores[activeExamNumber] ?: 0,
                        earnedDelta = examLastEarnedDelta,
                        examCompleted = examCompleted,
                        enteredFromMap = examEnteredFromMap,
                        onBackToMap = { viewModel.navigateTo(AppScreen.LEVEL_MAP) },
                        onAnswerExam = { optionIdx -> viewModel.answerExam(optionIdx) },
                        onRetakeExam = { viewModel.retakeExam() },
                        onFinishExam = { viewModel.finishExam() }
                    )

                    AppScreen.FAQ -> FAQScreen(
                        onBack = { viewModel.navigateTo(AppScreen.WELCOME) }
                    )

                    AppScreen.PRIVACY -> PrivacyScreen(
                        onBack = { viewModel.navigateTo(AppScreen.WELCOME) }
                    )

                    AppScreen.TERMS -> TermsScreen(
                        onBack = { viewModel.navigateTo(AppScreen.WELCOME) }
                    )

                    AppScreen.HOW_TO_PLAY -> HowToPlayScreen(
                        onBack = { viewModel.navigateTo(AppScreen.WELCOME) }
                    )
                }

                if (showDailyMissions) {
                    DailyMissionsDialog(
                        missionsState = dailyMissions,
                        onClaimMission = { missionId -> viewModel.claimDailyMission(missionId) },
                        onDismiss = { showDailyMissions = false }
                    )
                }
            }
        }
    }
}
