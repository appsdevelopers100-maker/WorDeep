package com.wordeep.app.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wordeep.app.data.EnglishDictionary
import com.wordeep.app.data.WordGameRepository
import com.wordeep.app.model.AppScreen
import com.wordeep.app.model.DailyMissionId
import com.wordeep.app.model.DailyMissionsManager
import com.wordeep.app.model.DailyMissionsState
import com.wordeep.app.model.ExamQuestion
import com.wordeep.app.model.ExtraWordFeedback
import com.wordeep.app.model.GameDifficulty
import com.wordeep.app.model.Level
import com.wordeep.app.model.TimeTrialScoring
import com.wordeep.app.model.WordItem
import com.wordeep.app.utils.SoundEffectsHelper
import com.wordeep.app.utils.TextToSpeechHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

class WordGameViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val PREFS_NAME = "wordig_game_prefs"
        private const val KEY_SCORE = "score"
        private const val KEY_UNLOCKED_LEVEL = "unlocked_level"
        private const val KEY_CURRENT_LEVEL_INDEX = "current_level_index"
        private const val KEY_DIFFICULTY = "difficulty"
        private const val KEY_TIME_TRIAL = "time_trial_enabled"
        private const val KEY_EXAM_BEST_PREFIX = "exam_best_"
        private const val KEY_REWARDED_TARGETS_PREFIX = "rewarded_targets_lvl_"
        private const val KEY_REWARDED_EXTRAS_PREFIX = "rewarded_extras_lvl_"
        private const val KEY_REWARDED_TIME_TRIAL_LEVELS = "rewarded_time_trial_levels"
        private const val KEY_DAILY_DATE = "daily_missions_date"
        private const val KEY_DAILY_LEVELS = "daily_missions_levels"
        private const val KEY_DAILY_EXTRAS = "daily_missions_extras"
        private const val KEY_DAILY_EXAMS = "daily_missions_exams"
        private const val KEY_DAILY_CLAIMED = "daily_missions_claimed"
        const val HINT_COST = 50
        const val EXTRA_WORD_POINTS = 5
        const val TIME_TRIAL_EXTRA_WORD_POINTS = 10
        const val EXAM_MAX_POINTS = 10
    }

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val ttsHelper = TextToSpeechHelper(application)
    private val soundEffects = SoundEffectsHelper()

    private val _difficulty = MutableStateFlow(GameDifficulty.MEDIUM)
    val difficulty: StateFlow<GameDifficulty> = _difficulty.asStateFlow()

    private val _timeTrialEnabled = MutableStateFlow(false)
    val timeTrialEnabled: StateFlow<Boolean> = _timeTrialEnabled.asStateFlow()

    private val _levels = MutableStateFlow<List<Level>>(
        WordGameRepository.getLevelsForDifficulty(GameDifficulty.MEDIUM)
    )
    val levels: StateFlow<List<Level>> = _levels.asStateFlow()

    private val _currentScreen = MutableStateFlow(AppScreen.SPLASH)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _score = MutableStateFlow(100)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _unlockedLevel = MutableStateFlow(1)
    val unlockedLevel: StateFlow<Int> = _unlockedLevel.asStateFlow()

    private val _currentLevelIndex = MutableStateFlow(0)
    val currentLevelIndex: StateFlow<Int> = _currentLevelIndex.asStateFlow()

    private val _selectedLetterIndices = MutableStateFlow<List<Int>>(emptyList())
    val selectedLetterIndices: StateFlow<List<Int>> = _selectedLetterIndices.asStateFlow()

    private val _currentExtractedWords = MutableStateFlow<List<WordItem>>(
        cloneExtractedWords(WordGameRepository.getLevelsForDifficulty(GameDifficulty.MEDIUM).first())
    )
    val currentExtractedWords: StateFlow<List<WordItem>> = _currentExtractedWords.asStateFlow()

    private val _discoveredExtraWords = MutableStateFlow<List<String>>(emptyList())
    val discoveredExtraWords: StateFlow<List<String>> = _discoveredExtraWords.asStateFlow()

    private val _extraWordFeedback = MutableStateFlow<ExtraWordFeedback?>(null)
    val extraWordFeedback: StateFlow<ExtraWordFeedback?> = _extraWordFeedback.asStateFlow()

    private val _canClaimFormedExtraWord = MutableStateFlow(false)
    val canClaimFormedExtraWord: StateFlow<Boolean> = _canClaimFormedExtraWord.asStateFlow()

    private val _isFormedExtraAlreadyClaimed = MutableStateFlow(false)
    val isFormedExtraAlreadyClaimed: StateFlow<Boolean> = _isFormedExtraAlreadyClaimed.asStateFlow()

    private val _isLevelComplete = MutableStateFlow(false)
    val isLevelComplete: StateFlow<Boolean> = _isLevelComplete.asStateFlow()

    private val rewardedTargetsByLevel = mutableMapOf<Int, MutableSet<String>>()
    private val rewardedExtrasByLevel = mutableMapOf<Int, MutableSet<String>>()
    private val rewardedTimeTrialLevels = mutableSetOf<Int>()

    private val _levelTimeLimitSeconds = MutableStateFlow(
        TimeTrialScoring.getLevelTimeLimitSeconds(
            WordGameRepository.getLevelsForDifficulty(GameDifficulty.MEDIUM)
                .first()
                .extractedWords
                .count { it.length >= 3 },
            GameDifficulty.MEDIUM
        )
    )
    val levelTimeLimitSeconds: StateFlow<Int> = _levelTimeLimitSeconds.asStateFlow()

    private val _timeRemainingSeconds = MutableStateFlow(_levelTimeLimitSeconds.value)
    val timeRemainingSeconds: StateFlow<Int> = _timeRemainingSeconds.asStateFlow()

    private val _isTimeUp = MutableStateFlow(false)
    val isTimeUp: StateFlow<Boolean> = _isTimeUp.asStateFlow()

    private val _lastSpeedBonus = MutableStateFlow(0)
    val lastSpeedBonus: StateFlow<Int> = _lastSpeedBonus.asStateFlow()

    private val _soundEnabled = MutableStateFlow(true)
    val soundEnabled: StateFlow<Boolean> = _soundEnabled.asStateFlow()

    private val _dailyMissions = MutableStateFlow(DailyMissionsState())
    val dailyMissions: StateFlow<DailyMissionsState> = _dailyMissions.asStateFlow()

    // Exam state (Exams 1..10, 10 points total per exam, retakable)
    private val _activeExamNumber = MutableStateFlow(1)
    val activeExamNumber: StateFlow<Int> = _activeExamNumber.asStateFlow()

    private val _activeExamQuestions = MutableStateFlow<List<ExamQuestion>>(
        WordGameRepository.getQuestionsForExam(1)
    )
    val activeExamQuestions: StateFlow<List<ExamQuestion>> = _activeExamQuestions.asStateFlow()

    private val _examBestScores = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val examBestScores: StateFlow<Map<Int, Int>> = _examBestScores.asStateFlow()

    private val _currentExamIndex = MutableStateFlow(0)
    val currentExamIndex: StateFlow<Int> = _currentExamIndex.asStateFlow()

    private val _examCorrectCount = MutableStateFlow(0)
    val examCorrectCount: StateFlow<Int> = _examCorrectCount.asStateFlow()

    private val _examScore = MutableStateFlow(0)
    val examScore: StateFlow<Int> = _examScore.asStateFlow()

    private val _examLastEarnedDelta = MutableStateFlow(0)
    val examLastEarnedDelta: StateFlow<Int> = _examLastEarnedDelta.asStateFlow()

    private val _examCompleted = MutableStateFlow(false)
    val examCompleted: StateFlow<Boolean> = _examCompleted.asStateFlow()

    private val _examEnteredFromMap = MutableStateFlow(false)
    val examEnteredFromMap: StateFlow<Boolean> = _examEnteredFromMap.asStateFlow()

    private var feedbackClearJob: Job? = null
    private var timerJob: Job? = null

    init {
        // Offload storage reads, dictionary indexing, and TTS warm-up from main thread so cold launch renders immediately
        viewModelScope.launch(Dispatchers.IO) {
            val savedScore = prefs.getInt(KEY_SCORE, 100)
            val savedUnlocked = prefs.getInt(KEY_UNLOCKED_LEVEL, 1).coerceIn(1, WordGameRepository.levels.size)
            val savedIndex = prefs.getInt(KEY_CURRENT_LEVEL_INDEX, 0).coerceIn(0, WordGameRepository.levels.lastIndex)
            val savedDiffName = prefs.getString(KEY_DIFFICULTY, GameDifficulty.MEDIUM.name)
            val savedDifficulty = GameDifficulty.entries.firstOrNull { it.name == savedDiffName } ?: GameDifficulty.MEDIUM
            val savedTimeTrial = prefs.getBoolean(KEY_TIME_TRIAL, false)
            val bestMap = mutableMapOf<Int, Int>()
            for (examNum in 1..10) {
                val best = prefs.getInt("$KEY_EXAM_BEST_PREFIX$examNum", -1)
                if (best in 0..EXAM_MAX_POINTS) {
                    bestMap[examNum] = best
                }
            }

            val adjustedLevels = WordGameRepository.getLevelsForDifficulty(savedDifficulty)
            val initialLevel = adjustedLevels[savedIndex]
            val initialLimit = TimeTrialScoring.getLevelTimeLimitSeconds(
                initialLevel.extractedWords.count { it.length >= 3 },
                savedDifficulty
            )

            val loadedTargets = mutableMapOf<Int, MutableSet<String>>()
            val loadedExtras = mutableMapOf<Int, MutableSet<String>>()
            for (lvl in WordGameRepository.levels) {
                val tSet = prefs.getStringSet("$KEY_REWARDED_TARGETS_PREFIX${lvl.id}", null)
                if (!tSet.isNullOrEmpty()) {
                    loadedTargets[lvl.id] = tSet.map { it.uppercase() }.toMutableSet()
                }
                val eSet = prefs.getStringSet("$KEY_REWARDED_EXTRAS_PREFIX${lvl.id}", null)
                if (!eSet.isNullOrEmpty()) {
                    loadedExtras[lvl.id] = eSet.map { it.uppercase() }.toMutableSet()
                }
            }
            val loadedTimeTrialLevels = prefs.getStringSet(KEY_REWARDED_TIME_TRIAL_LEVELS, null)
                ?.mapNotNull { it.toIntOrNull() }
                ?.toSet()
                ?: emptySet()

            synchronized(this@WordGameViewModel) {
                rewardedTargetsByLevel.clear()
                rewardedTargetsByLevel.putAll(loadedTargets)
                rewardedExtrasByLevel.clear()
                rewardedExtrasByLevel.putAll(loadedExtras)
                rewardedTimeTrialLevels.clear()
                rewardedTimeTrialLevels.addAll(loadedTimeTrialLevels)
            }

            val today = DailyMissionsManager.getTodayDateKey()
            val savedDailyDate = prefs.getString(KEY_DAILY_DATE, null)
            val loadedDaily = if (savedDailyDate == today) {
                val claimedNames = prefs.getStringSet(KEY_DAILY_CLAIMED, null) ?: emptySet()
                val claimedSet = claimedNames.mapNotNull { name ->
                    DailyMissionId.entries.firstOrNull { it.name == name }
                }.toSet()
                DailyMissionsState(
                    date = today,
                    levelsPassed = prefs.getInt(KEY_DAILY_LEVELS, 0).coerceAtLeast(0),
                    extraWordsFound = prefs.getInt(KEY_DAILY_EXTRAS, 0).coerceAtLeast(0),
                    examsCompleted = prefs.getInt(KEY_DAILY_EXAMS, 0).coerceAtLeast(0),
                    claimedIds = claimedSet
                )
            } else {
                DailyMissionsState(date = today)
            }

            _difficulty.value = savedDifficulty
            _timeTrialEnabled.value = savedTimeTrial
            _levels.value = adjustedLevels
            _score.value = savedScore
            _unlockedLevel.value = savedUnlocked
            _currentLevelIndex.value = savedIndex
            _examBestScores.value = bestMap
            _currentExtractedWords.value = cloneExtractedWords(initialLevel)
            _discoveredExtraWords.value = loadedExtras[initialLevel.id]?.sorted() ?: emptyList()
            _levelTimeLimitSeconds.value = initialLimit
            _timeRemainingSeconds.value = initialLimit
            _dailyMissions.value = loadedDaily

            if (savedDailyDate != today) {
                prefs.edit()
                    .putString(KEY_DAILY_DATE, today)
                    .putInt(KEY_DAILY_LEVELS, 0)
                    .putInt(KEY_DAILY_EXTRAS, 0)
                    .putInt(KEY_DAILY_EXAMS, 0)
                    .putStringSet(KEY_DAILY_CLAIMED, emptySet())
                    .apply()
            }

            EnglishDictionary.warmUp()
            ttsHelper.warmUp()
        }
    }

    private fun updateTimerJobState() {
        val shouldRun = _timeTrialEnabled.value &&
            _currentScreen.value == AppScreen.GAMEPLAY &&
            !_isLevelComplete.value &&
            !_isTimeUp.value

        if (!shouldRun) {
            timerJob?.cancel()
            timerJob = null
            return
        }

        if (timerJob?.isActive == true) return
        timerJob = viewModelScope.launch {
            while (isActive &&
                _timeTrialEnabled.value &&
                _currentScreen.value == AppScreen.GAMEPLAY &&
                !_isLevelComplete.value &&
                !_isTimeUp.value
            ) {
                delay(1000L)
                val next = (_timeRemainingSeconds.value - 1).coerceAtLeast(0)
                _timeRemainingSeconds.value = next
                if (next <= 0) {
                    _isTimeUp.value = true
                    break
                }
            }
        }
    }

    private fun cloneExtractedWords(level: Level): List<WordItem> {
        return level.extractedWords
            .filter { it.word.length >= 3 && it.length >= 3 }
            .map {
                it.copy(
                    isDiscovered = false,
                    revealedIndices = emptyList(),
                    wasAlreadyRewarded = false,
                    earnedPoints = 0
                )
            }
    }

    private fun saveProgressAsync(
        newScore: Int = _score.value,
        newUnlocked: Int = _unlockedLevel.value,
        newLevelIndex: Int = _currentLevelIndex.value,
        newDifficulty: GameDifficulty = _difficulty.value,
        newTimeTrial: Boolean = _timeTrialEnabled.value,
        bestScores: Map<Int, Int> = _examBestScores.value,
        dailyState: DailyMissionsState = _dailyMissions.value,
        persistLevelId: Int? = null
    ) {
        val targetSetCopy = persistLevelId?.let { lvlId ->
            synchronized(this) { rewardedTargetsByLevel[lvlId]?.toSet() }
        }
        val extraSetCopy = persistLevelId?.let { lvlId ->
            synchronized(this) { rewardedExtrasByLevel[lvlId]?.toSet() }
        }
        val timeTrialCopy = synchronized(this) {
            rewardedTimeTrialLevels.map { it.toString() }.toSet()
        }
        val claimedNames = dailyState.claimedIds.map { it.name }.toSet()

        viewModelScope.launch(Dispatchers.IO) {
            prefs.edit().apply {
                putInt(KEY_SCORE, newScore)
                putInt(KEY_UNLOCKED_LEVEL, newUnlocked)
                putInt(KEY_CURRENT_LEVEL_INDEX, newLevelIndex)
                putString(KEY_DIFFICULTY, newDifficulty.name)
                putBoolean(KEY_TIME_TRIAL, newTimeTrial)
                putString(KEY_DAILY_DATE, dailyState.date)
                putInt(KEY_DAILY_LEVELS, dailyState.levelsPassed)
                putInt(KEY_DAILY_EXTRAS, dailyState.extraWordsFound)
                putInt(KEY_DAILY_EXAMS, dailyState.examsCompleted)
                putStringSet(KEY_DAILY_CLAIMED, claimedNames)
                bestScores.forEach { (examNum, best) ->
                    putInt("$KEY_EXAM_BEST_PREFIX$examNum", best)
                }
                if (persistLevelId != null) {
                    if (targetSetCopy != null) {
                        putStringSet("$KEY_REWARDED_TARGETS_PREFIX$persistLevelId", targetSetCopy)
                    }
                    if (extraSetCopy != null) {
                        putStringSet("$KEY_REWARDED_EXTRAS_PREFIX$persistLevelId", extraSetCopy)
                    }
                }
                putStringSet(KEY_REWARDED_TIME_TRIAL_LEVELS, timeTrialCopy)
                apply()
            }
        }
    }

    private fun getBasePointsForLength(length: Int): Int = when {
        length == 3 -> 8
        length == 4 -> 12
        length == 5 -> 15
        length >= 6 -> 20
        else -> 0
    }

    private fun getPointsForLength(length: Int): Int {
        val base = getBasePointsForLength(length)
        return (base * _difficulty.value.multiplier).roundToInt()
    }

    fun getExtraWordPoints(): Int {
        return if (_timeTrialEnabled.value) TIME_TRIAL_EXTRA_WORD_POINTS else EXTRA_WORD_POINTS
    }

    fun setDifficulty(nextDifficulty: GameDifficulty) {
        val adjustedLevels = WordGameRepository.getLevelsForDifficulty(nextDifficulty)
        _difficulty.value = nextDifficulty
        _levels.value = adjustedLevels
        val safeIdx = _currentLevelIndex.value.coerceIn(0, adjustedLevels.lastIndex)
        loadLevelInternal(safeIdx)
        saveProgressAsync(newDifficulty = nextDifficulty)
    }

    fun setTimeTrialEnabled(enabled: Boolean) {
        _timeTrialEnabled.value = enabled
        val lvl = getCurrentLevel()
        val limit = TimeTrialScoring.getLevelTimeLimitSeconds(
            lvl.extractedWords.count { it.length >= 3 },
            _difficulty.value
        )
        _levelTimeLimitSeconds.value = limit
        _timeRemainingSeconds.value = limit
        _isTimeUp.value = false
        _lastSpeedBonus.value = 0
        saveProgressAsync(newTimeTrial = enabled)
        updateTimerJobState()
    }

    fun getCurrentLevel(): Level {
        val list = _levels.value
        val idx = _currentLevelIndex.value.coerceIn(0, list.lastIndex)
        return list[idx]
    }

    fun refreshDailyMissionsIfNewDay(): DailyMissionsState {
        val normalized = DailyMissionsManager.normalizeForToday(_dailyMissions.value)
        if (normalized != _dailyMissions.value) {
            _dailyMissions.value = normalized
            saveProgressAsync(dailyState = normalized)
        }
        return normalized
    }

    fun claimDailyMission(missionId: DailyMissionId) {
        val mission = DailyMissionsManager.missions.firstOrNull { it.id == missionId } ?: return
        val current = refreshDailyMissionsIfNewDay()
        val progress = DailyMissionsManager.getProgress(current, missionId)
        if (progress < mission.target || current.claimedIds.contains(missionId)) return

        val updatedDaily = current.copy(claimedIds = current.claimedIds + missionId)
        val updatedScore = _score.value + mission.rewardPoints
        _dailyMissions.value = updatedDaily
        _score.value = updatedScore
        if (_soundEnabled.value) soundEffects.playWordMatch()
        saveProgressAsync(newScore = updatedScore, dailyState = updatedDaily)
    }

    fun navigateTo(screen: AppScreen) {
        refreshDailyMissionsIfNewDay()
        _currentScreen.value = screen
        updateTimerJobState()
    }

    fun toggleSound() {
        _soundEnabled.value = !_soundEnabled.value
    }

    fun speakWord(word: String) {
        if (_soundEnabled.value) {
            ttsHelper.speak(word)
        }
    }

    fun selectLevel(index: Int) {
        val list = _levels.value
        if (index in list.indices) {
            _currentLevelIndex.value = index
            loadLevelInternal(index)
            saveProgressAsync(newLevelIndex = index)
            _currentScreen.value = AppScreen.GAMEPLAY
            updateTimerJobState()
        }
    }

    fun retryCurrentLevel() {
        val idx = _currentLevelIndex.value.coerceIn(0, _levels.value.lastIndex)
        loadLevelInternal(idx)
        updateTimerJobState()
    }

    private fun loadLevelInternal(index: Int) {
        val lvl = _levels.value[index]
        val limit = TimeTrialScoring.getLevelTimeLimitSeconds(
            lvl.extractedWords.count { it.length >= 3 },
            _difficulty.value
        )
        val persistedExtras = synchronized(this) {
            rewardedExtrasByLevel[lvl.id]?.sorted() ?: emptyList()
        }
        _currentExtractedWords.value = cloneExtractedWords(lvl)
        _discoveredExtraWords.value = persistedExtras
        _extraWordFeedback.value = null
        _selectedLetterIndices.value = emptyList()
        _canClaimFormedExtraWord.value = false
        _isFormedExtraAlreadyClaimed.value = false
        _isLevelComplete.value = false
        _isTimeUp.value = false
        _lastSpeedBonus.value = 0
        _levelTimeLimitSeconds.value = limit
        _timeRemainingSeconds.value = limit
    }

    private fun updateClaimableExtraFlag(selected: List<Int>, level: Level, extracted: List<WordItem>) {
        if (selected.size < 3) {
            _canClaimFormedExtraWord.value = false
            _isFormedExtraAlreadyClaimed.value = false
            return
        }
        val formed = buildString {
            for (i in selected) {
                append(level.mainWord.getOrNull(i) ?: "")
            }
        }.uppercase()
        val isTarget = extracted.any { it.word.equals(formed, ignoreCase = true) }
        val isMain = formed == level.mainWord.uppercase()
        val claimable =
            formed.length >= 3 && !isTarget && !isMain && EnglishDictionary.isValidEnglishWord(formed)
        _canClaimFormedExtraWord.value = claimable
        _isFormedExtraAlreadyClaimed.value = if (claimable) {
            synchronized(this) {
                rewardedExtrasByLevel[level.id]?.contains(formed) == true ||
                    _discoveredExtraWords.value.contains(formed)
            }
        } else {
            false
        }
    }

    private fun triggerWordFeedback(
        word: String,
        pointsAwarded: Int,
        isDuplicate: Boolean = false,
        isTargetWord: Boolean = false
    ) {
        feedbackClearJob?.cancel()
        val arabic = EnglishDictionary.getArabicTranslation(word)
        _extraWordFeedback.value = ExtraWordFeedback(
            word = word,
            arabic = arabic,
            points = pointsAwarded,
            isDuplicate = isDuplicate,
            isTargetWord = isTargetWord
        )
        feedbackClearJob = viewModelScope.launch {
            delay(2200L)
            _extraWordFeedback.value = null
        }
    }

    private fun awardExtraWord(upperWord: String) {
        if (upperWord.length < 3) return
        val levelId = getCurrentLevel().id
        val currentExtras = _discoveredExtraWords.value
        val alreadyRewarded = synchronized(this) {
            rewardedExtrasByLevel[levelId]?.contains(upperWord) == true ||
                currentExtras.contains(upperWord)
        }
        _selectedLetterIndices.value = emptyList()
        _canClaimFormedExtraWord.value = false
        _isFormedExtraAlreadyClaimed.value = false

        if (alreadyRewarded) {
            if (!currentExtras.contains(upperWord)) {
                _discoveredExtraWords.value = currentExtras + upperWord
            }
            if (_soundEnabled.value) soundEffects.playExtraWord(isDuplicate = true)
            triggerWordFeedback(upperWord, pointsAwarded = 0, isDuplicate = true, isTargetWord = false)
            return
        }

        val extraPoints = getExtraWordPoints()
        synchronized(this) {
            rewardedExtrasByLevel.getOrPut(levelId) { mutableSetOf() }.add(upperWord)
        }
        _discoveredExtraWords.value = if (currentExtras.contains(upperWord)) {
            currentExtras
        } else {
            currentExtras + upperWord
        }
        val newScore = _score.value + extraPoints
        _score.value = newScore
        val currentDaily = refreshDailyMissionsIfNewDay()
        val updatedDaily = currentDaily.copy(extraWordsFound = currentDaily.extraWordsFound + 1)
        _dailyMissions.value = updatedDaily
        saveProgressAsync(newScore = newScore, dailyState = updatedDaily, persistLevelId = levelId)
        if (_soundEnabled.value) soundEffects.playExtraWord(isDuplicate = false)
        speakWord(upperWord)
        triggerWordFeedback(upperWord, pointsAwarded = extraPoints, isDuplicate = false, isTargetWord = false)
    }

    fun onLetterClicked(index: Int) {
        if (_isLevelComplete.value || _isTimeUp.value) return
        val level = getCurrentLevel()
        if (index !in level.mainWord.indices) return

        val currentSelected = _selectedLetterIndices.value
        if (currentSelected.isNotEmpty()) {
            val lastIndex = currentSelected.last()
            if (index <= lastIndex) {
                if (index == lastIndex) {
                    val trimmed = currentSelected.dropLast(1)
                    if (_soundEnabled.value) soundEffects.playTileClick(trimmed.size, isDeselect = true)
                    _selectedLetterIndices.value = trimmed
                    updateClaimableExtraFlag(trimmed, level, _currentExtractedWords.value)
                }
                return
            }
        }

        val nextSelected = currentSelected + index
        val formedUpper = nextSelected.map { level.mainWord[it] }.joinToString("").uppercase()

        if (formedUpper.length < 3) {
            if (_soundEnabled.value) soundEffects.playTileClick(nextSelected.size, isDeselect = false)
            _selectedLetterIndices.value = nextSelected
            _canClaimFormedExtraWord.value = false
            return
        }

        val currentWords = _currentExtractedWords.value
        val matchIdx = currentWords.indexOfFirst {
            !it.isDiscovered && it.word.equals(formedUpper, ignoreCase = true)
        }

        if (matchIdx != -1) {
            val matched = currentWords[matchIdx]
            val matchedUpper = matched.word.uppercase()
            val wasAlreadyRewarded = synchronized(this) {
                rewardedTargetsByLevel[level.id]?.contains(matchedUpper) == true
            }
            val pts = if (wasAlreadyRewarded) 0 else getPointsForLength(matched.length)
            val allIndices = (0 until matched.length).toList()
            val updatedWords = currentWords.mapIndexed { idx, item ->
                if (idx == matchIdx) {
                    item.copy(
                        isDiscovered = true,
                        revealedIndices = allIndices,
                        wasAlreadyRewarded = wasAlreadyRewarded,
                        earnedPoints = pts
                    )
                } else {
                    item
                }
            }
            val completedAll = updatedWords.all { it.isDiscovered }
            val canAwardSpeedBonus = synchronized(this) {
                completedAll && _timeTrialEnabled.value && !rewardedTimeTrialLevels.contains(level.id)
            }
            val speedBonus = if (canAwardSpeedBonus) {
                TimeTrialScoring.calculateSpeedBonus(
                    _timeRemainingSeconds.value,
                    _levelTimeLimitSeconds.value,
                    _difficulty.value.multiplier
                )
            } else {
                0
            }

            if (!wasAlreadyRewarded) {
                synchronized(this) {
                    rewardedTargetsByLevel.getOrPut(level.id) { mutableSetOf() }.add(matchedUpper)
                }
            } else {
                triggerWordFeedback(matched.word, pointsAwarded = 0, isDuplicate = true, isTargetWord = true)
            }
            if (canAwardSpeedBonus && speedBonus > 0) {
                synchronized(this) {
                    rewardedTimeTrialLevels.add(level.id)
                }
            }

            val newScore = _score.value + pts + speedBonus
            _score.value = newScore
            _currentExtractedWords.value = updatedWords
            _selectedLetterIndices.value = emptyList()
            _canClaimFormedExtraWord.value = false
            _isFormedExtraAlreadyClaimed.value = false
            if (_soundEnabled.value) soundEffects.playWordMatch()
            speakWord(matched.word)

            var newUnlocked = _unlockedLevel.value
            var updatedDaily = refreshDailyMissionsIfNewDay()
            if (completedAll) {
                _lastSpeedBonus.value = speedBonus
                _isLevelComplete.value = true
                updateTimerJobState()
                updatedDaily = updatedDaily.copy(levelsPassed = updatedDaily.levelsPassed + 1)
                _dailyMissions.value = updatedDaily
                if (_currentLevelIndex.value + 1 >= newUnlocked && newUnlocked < _levels.value.size) {
                    newUnlocked = _currentLevelIndex.value + 2
                    _unlockedLevel.value = newUnlocked
                }
            }
            saveProgressAsync(
                newScore = newScore,
                newUnlocked = newUnlocked,
                dailyState = updatedDaily,
                persistLevelId = level.id
            )
            return
        }

        val isTargetWord = currentWords.any { it.word.equals(formedUpper, ignoreCase = true) }
        val isMainWord = formedUpper == level.mainWord.uppercase()

        if (!isTargetWord && !isMainWord && EnglishDictionary.isValidEnglishWord(formedUpper)) {
            val isPrefixOfUndiscoveredTarget = currentWords.any {
                !it.isDiscovered &&
                    it.word.uppercase().startsWith(formedUpper) &&
                    it.word.length > formedUpper.length
            }
            if (!isPrefixOfUndiscoveredTarget) {
                awardExtraWord(formedUpper)
                return
            }
        }

        if (_soundEnabled.value) soundEffects.playTileClick(nextSelected.size, isDeselect = false)
        _selectedLetterIndices.value = nextSelected
        updateClaimableExtraFlag(nextSelected, level, currentWords)
    }

    fun claimFormedExtraWord() {
        val level = getCurrentLevel()
        val formed = _selectedLetterIndices.value.map { level.mainWord[it] }.joinToString("").uppercase()
        if (formed.length >= 3 &&
            formed != level.mainWord.uppercase() &&
            _currentExtractedWords.value.none { it.word.equals(formed, ignoreCase = true) } &&
            EnglishDictionary.isValidEnglishWord(formed)
        ) {
            awardExtraWord(formed)
        }
    }

    fun clearSelection() {
        _selectedLetterIndices.value = emptyList()
        _canClaimFormedExtraWord.value = false
        _isFormedExtraAlreadyClaimed.value = false
    }

    fun useHint() {
        if (_score.value < HINT_COST || _isLevelComplete.value || _isTimeUp.value) return
        val level = getCurrentLevel()
        val currentWords = _currentExtractedWords.value
        val targetIdx = currentWords.indexOfFirst { !it.isDiscovered && it.revealedIndices.size < it.length }
        if (targetIdx == -1) return

        val target = currentWords[targetIdx]
        val nextRevealIndex = (0 until target.length).firstOrNull { !target.revealedIndices.contains(it) } ?: return
        val nextRevealed = target.revealedIndices + nextRevealIndex
        val completedByHint = nextRevealed.size >= target.length
        val wasAlreadyRewarded = completedByHint && synchronized(this) {
            rewardedTargetsByLevel[level.id]?.contains(target.word.uppercase()) == true
        }

        val updatedWords = currentWords.mapIndexed { idx, item ->
            if (idx == targetIdx) {
                item.copy(
                    revealedIndices = nextRevealed,
                    isDiscovered = completedByHint,
                    wasAlreadyRewarded = wasAlreadyRewarded,
                    earnedPoints = 0
                )
            } else {
                item
            }
        }

        val completedAll = completedByHint && updatedWords.all { it.isDiscovered }
        val canAwardSpeedBonus = synchronized(this) {
            completedAll && _timeTrialEnabled.value && !rewardedTimeTrialLevels.contains(level.id)
        }
        val speedBonus = if (canAwardSpeedBonus) {
            TimeTrialScoring.calculateSpeedBonus(
                _timeRemainingSeconds.value,
                _levelTimeLimitSeconds.value,
                _difficulty.value.multiplier
            )
        } else {
            0
        }
        if (canAwardSpeedBonus && speedBonus > 0) {
            synchronized(this) {
                rewardedTimeTrialLevels.add(level.id)
            }
        }

        val newScore = _score.value - HINT_COST + speedBonus
        _score.value = newScore
        _currentExtractedWords.value = updatedWords

        var newUnlocked = _unlockedLevel.value
        var updatedDaily = refreshDailyMissionsIfNewDay()
        if (completedByHint) {
            _selectedLetterIndices.value = emptyList()
            _canClaimFormedExtraWord.value = false
            _isFormedExtraAlreadyClaimed.value = false
            speakWord(target.word)
            if (completedAll) {
                _lastSpeedBonus.value = speedBonus
                _isLevelComplete.value = true
                updateTimerJobState()
                updatedDaily = updatedDaily.copy(levelsPassed = updatedDaily.levelsPassed + 1)
                _dailyMissions.value = updatedDaily
                if (_currentLevelIndex.value + 1 >= newUnlocked && newUnlocked < _levels.value.size) {
                    newUnlocked = _currentLevelIndex.value + 2
                    _unlockedLevel.value = newUnlocked
                }
            }
        }
        saveProgressAsync(
            newScore = newScore,
            newUnlocked = newUnlocked,
            dailyState = updatedDaily,
            persistLevelId = level.id
        )
    }

    fun nextLevel() {
        val list = _levels.value
        if (list.isEmpty()) return
        val completedLevelNumber = _currentLevelIndex.value + 1
        if (completedLevelNumber % 5 == 0) {
            startExam(completedLevelNumber / 5, fromMap = false)
        } else if (_currentLevelIndex.value + 1 < list.size) {
            val nextIdx = _currentLevelIndex.value + 1
            _currentLevelIndex.value = nextIdx
            loadLevelInternal(nextIdx)
            saveProgressAsync(newLevelIndex = nextIdx)
            _currentScreen.value = AppScreen.GAMEPLAY
            updateTimerJobState()
        } else {
            _currentScreen.value = AppScreen.LEVEL_MAP
            updateTimerJobState()
        }
    }

    fun startExam(examNumber: Int = 1, fromMap: Boolean = false) {
        val safeExam = examNumber.coerceIn(1, 10)
        _activeExamNumber.value = safeExam
        _activeExamQuestions.value = WordGameRepository.getQuestionsForExam(safeExam)
        _currentExamIndex.value = 0
        _examCorrectCount.value = 0
        _examScore.value = 0
        _examLastEarnedDelta.value = 0
        _examCompleted.value = false
        _examEnteredFromMap.value = fromMap
        _currentScreen.value = AppScreen.EXAM
        updateTimerJobState()
    }

    fun retakeExam() {
        _currentExamIndex.value = 0
        _examCorrectCount.value = 0
        _examScore.value = 0
        _examLastEarnedDelta.value = 0
        _examCompleted.value = false
    }

    fun answerExam(selectedOptionIndex: Int) {
        val questions = _activeExamQuestions.value
        val idx = _currentExamIndex.value
        if (idx >= questions.size) return

        val isCorrect = questions[idx].correctIndex == selectedOptionIndex
        val nextCorrect = _examCorrectCount.value + if (isCorrect) 1 else 0
        val nextExamScore = ((nextCorrect.toFloat() * EXAM_MAX_POINTS) / questions.size).roundToInt()

        _examCorrectCount.value = nextCorrect
        _examScore.value = nextExamScore

        if (idx + 1 < questions.size) {
            _currentExamIndex.value = idx + 1
        } else {
            val examNum = _activeExamNumber.value
            val currentBestMap = _examBestScores.value
            val previousBest = currentBestMap[examNum] ?: 0
            val newBest = max(previousBest, nextExamScore)
            val delta = newBest - previousBest

            val updatedScore = if (delta > 0) _score.value + delta else _score.value
            if (delta > 0) {
                _score.value = updatedScore
            }
            val updatedBestMap = currentBestMap.toMutableMap().apply {
                put(examNum, newBest)
            }
            val updatedDaily = refreshDailyMissionsIfNewDay().let { current ->
                current.copy(examsCompleted = current.examsCompleted + 1)
            }
            _examLastEarnedDelta.value = delta
            _examBestScores.value = updatedBestMap
            _dailyMissions.value = updatedDaily
            _examCompleted.value = true
            saveProgressAsync(
                newScore = updatedScore,
                bestScores = updatedBestMap,
                dailyState = updatedDaily
            )
        }
    }

    fun finishExam() {
        if (_examEnteredFromMap.value) {
            _currentScreen.value = AppScreen.LEVEL_MAP
            updateTimerJobState()
            return
        }
        val nextIdx = _activeExamNumber.value * 5
        if (nextIdx < _levels.value.size) {
            _currentLevelIndex.value = nextIdx
            loadLevelInternal(nextIdx)
            saveProgressAsync(newLevelIndex = nextIdx)
            _currentScreen.value = AppScreen.GAMEPLAY
            updateTimerJobState()
        } else {
            _currentScreen.value = AppScreen.LEVEL_MAP
            updateTimerJobState()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        ttsHelper.shutdown()
        soundEffects.shutdown()
    }
}
