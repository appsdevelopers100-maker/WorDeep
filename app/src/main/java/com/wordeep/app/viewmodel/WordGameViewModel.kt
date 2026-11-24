package com.wordeep.app.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wordeep.app.data.WordGameRepository
import com.wordeep.app.model.AppScreen
import com.wordeep.app.model.Level
import com.wordeep.app.model.WordItem
import com.wordeep.app.utils.TextToSpeechHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WordGameViewModel(application: Application) : AndroidViewModel(application) {
    private val ttsHelper = try {
        TextToSpeechHelper(application)
    } catch (e: Exception) {
        null
    }

    private val prefs = try {
        application.getSharedPreferences("wordig_game_prefs", Context.MODE_PRIVATE)
    } catch (e: Exception) {
        null
    }

    var currentScreen by mutableStateOf(AppScreen.SPLASH)
        private set

    var score by mutableStateOf(
        try { prefs?.getInt("key_score", 100) ?: 100 } catch (e: Exception) { 100 }
    )
        private set

    var unlockedLevel by mutableStateOf(
        try { prefs?.getInt("key_unlocked_level", 1) ?: 1 } catch (e: Exception) { 1 }
    )
        private set

    var currentLevelIndex by mutableStateOf(
        try { prefs?.getInt("key_current_level", 0) ?: 0 } catch (e: Exception) { 0 }
    )
        private set

    // Levels start empty and load asynchronously so startup is instant
    var levels by mutableStateOf<List<Level>>(emptyList())
        private set

    val currentLevel: Level
        get() = if (levels.isNotEmpty()) {
            levels.getOrElse(currentLevelIndex) { levels.first() }
        } else {
            Level(1, "LOADING", "تحميل", "General", emptyList())
        }

    // Gameplay state for current level
    var selectedLetterIndices = mutableStateOf<List<Int>>(emptyList())
        private set

    var currentExtractedWords = mutableStateOf<List<WordItem>>(emptyList())
        private set

    var discoveredExtraWords = mutableStateOf<Set<String>>(emptySet())
        private set

    var isLevelComplete by mutableStateOf(false)
        private set

    var soundEnabled by mutableStateOf(true)
        private set

    // Exam state
    var currentExamIndex by mutableStateOf(0)
        private set
    var examScore by mutableStateOf(0)
        private set
    var examCompleted by mutableStateOf(false)
        private set

    init {
        // Load levels in the background on IO thread so startup is 100% instant
        viewModelScope.launch(Dispatchers.IO) {
            val loadedLevels = WordGameRepository.levels
            withContext(Dispatchers.Main) {
                levels = loadedLevels
                try {
                    loadLevel(currentLevelIndex)
                } catch (e: Exception) {
                    loadLevel(0)
                }
            }
        }
    }

    private fun saveProgress() {
        try {
            prefs?.edit()?.apply {
                putInt("key_score", score)
                putInt("key_unlocked_level", unlockedLevel)
                putInt("key_current_level", currentLevelIndex)
                apply()
            }
        } catch (e: Exception) {
            // ignore
        }
    }

    fun navigateTo(screen: AppScreen) {
        currentScreen = screen
    }

    fun toggleSound() {
        soundEnabled = !soundEnabled
    }

    fun speakWord(word: String) {
        if (soundEnabled) {
            try {
                ttsHelper?.speak(word)
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    fun selectLevel(index: Int) {
        if (levels.isNotEmpty() && index < levels.size) {
            currentLevelIndex = index
            saveProgress()
            loadLevel(index)
            currentScreen = AppScreen.GAMEPLAY
        }
    }

    private fun loadLevel(index: Int) {
        if (levels.isEmpty()) return
        val lvl = levels.getOrElse(index) { levels.first() }
        currentExtractedWords.value = lvl.extractedWords.map { it.copy(isDiscovered = false) }
        discoveredExtraWords.value = emptySet()
        selectedLetterIndices.value = emptyList()
        isLevelComplete = false
    }

    fun onLetterClicked(index: Int) {
        val currentSelected = selectedLetterIndices.value.toMutableList()
        if (currentSelected.isNotEmpty()) {
            val lastIndex = currentSelected.last()
            if (index <= lastIndex) {
                if (index == lastIndex) {
                    currentSelected.removeAt(currentSelected.size - 1)
                    selectedLetterIndices.value = currentSelected
                }
                return
            }
        }
        currentSelected.add(index)
        selectedLetterIndices.value = currentSelected

        checkFormedWord()
    }

    fun clearSelection() {
        selectedLetterIndices.value = emptyList()
    }

    private fun getPointsForLength(length: Int): Int {
        return when (length) {
            2 -> 5
            3 -> 8
            4 -> 12
            5 -> 15
            else -> 20
        }
    }

    private fun checkFormedWord() {
        if (levels.isEmpty()) return
        val indices = selectedLetterIndices.value
        val formedString = indices.map { currentLevel.mainWord[it] }.joinToString("")

        val updatedWords = currentExtractedWords.value.toMutableList()
        var foundMatch = false

        for (i in updatedWords.indices) {
            val wordItem = updatedWords[i]
            if (!wordItem.isDiscovered && wordItem.word.equals(formedString, ignoreCase = true)) {
                updatedWords[i] = wordItem.copy(isDiscovered = true)
                score += getPointsForLength(wordItem.length)
                saveProgress()
                foundMatch = true
                speakWord(wordItem.word)
                break
            }
        }

        if (foundMatch) {
            currentExtractedWords.value = updatedWords
            selectedLetterIndices.value = emptyList()

            if (updatedWords.all { it.isDiscovered }) {
                isLevelComplete = true
                if (currentLevelIndex + 1 >= unlockedLevel && unlockedLevel < levels.size) {
                    unlockedLevel = currentLevelIndex + 2
                }
                saveProgress()
            }
        }
    }

    fun useHint() {
        if (score < 50 || levels.isEmpty()) return
        val undiscovered = currentExtractedWords.value.firstOrNull { !it.isDiscovered }
        if (undiscovered != null) {
            score -= 50
            score += getPointsForLength(undiscovered.length)
            saveProgress()
            val updatedWords = currentExtractedWords.value.toMutableList()
            val index = updatedWords.indexOf(undiscovered)
            if (index != -1) {
                updatedWords[index] = undiscovered.copy(isDiscovered = true)
                currentExtractedWords.value = updatedWords
                speakWord(undiscovered.word)

                if (updatedWords.all { it.isDiscovered }) {
                    isLevelComplete = true
                    if (currentLevelIndex + 1 >= unlockedLevel && unlockedLevel < levels.size) {
                        unlockedLevel = currentLevelIndex + 2
                    }
                    saveProgress()
                }
            }
        }
    }

    fun nextLevel() {
        if (levels.isEmpty()) return
        if (currentLevelIndex + 1 < levels.size) {
            if ((currentLevelIndex + 1) % 5 == 0) {
                startExam()
            } else {
                currentLevelIndex++
                saveProgress()
                loadLevel(currentLevelIndex)
                currentScreen = AppScreen.GAMEPLAY
            }
        } else {
            currentScreen = AppScreen.LEVEL_MAP
        }
    }

    fun startExam() {
        currentExamIndex = 0
        examScore = 0
        examCompleted = false
        currentScreen = AppScreen.EXAM
    }

    fun answerExam(selectedOptionIndex: Int) {
        val questions = WordGameRepository.examQuestions
        if (currentExamIndex < questions.size) {
            if (questions[currentExamIndex].correctIndex == selectedOptionIndex) {
                examScore += 25
                score += 20
                saveProgress()
            }
            if (currentExamIndex + 1 < questions.size) {
                currentExamIndex++
            } else {
                examCompleted = true
            }
        }
    }

    fun finishExam() {
        currentLevelIndex++
        saveProgress()
        if (currentLevelIndex < levels.size) {
            loadLevel(currentLevelIndex)
            currentScreen = AppScreen.GAMEPLAY
        } else {
            currentScreen = AppScreen.LEVEL_MAP
        }
    }
}