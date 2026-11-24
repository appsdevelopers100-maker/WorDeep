package com.wordeep.app.model

data class WordItem(
    val word: String,
    val arabic: String,
    val length: Int,
    val isDiscovered: Boolean = false,
    val revealedIndices: List<Int> = emptyList(),
    val wasAlreadyRewarded: Boolean = false,
    val earnedPoints: Int = 0
)

data class Level(
    val id: Int,
    val mainWord: String,
    val arabic: String,
    val category: String? = null,
    val extractedWords: List<WordItem>
)

data class ExamQuestion(
    val id: Int,
    val question: String,
    val arabic: String,
    val options: List<String>,
    val correctIndex: Int
)

data class FaqItem(
    val question: String,
    val answer: String
)

data class ExtraWordFeedback(
    val word: String,
    val arabic: String? = null,
    val points: Int = 5,
    val isDuplicate: Boolean = false,
    val isTargetWord: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

enum class GameDifficulty(
    val label: String,
    val arabic: String,
    val multiplier: Float,
    val multiplierLabel: String,
    val secondsPerWord: Int,
    val description: String
) {
    EASY(
        label = "Easy",
        arabic = "سهل",
        multiplier = 0.75f,
        multiplierLabel = "0.75x",
        secondsPerWord = 12,
        description = "Simpler words · 0.75x pts · 12s/word"
    ),
    MEDIUM(
        label = "Medium",
        arabic = "متوسط",
        multiplier = 1.0f,
        multiplierLabel = "1.0x",
        secondsPerWord = 10,
        description = "Balanced mix · 1.0x pts · 10s/word"
    ),
    HARD(
        label = "Hard",
        arabic = "صعب",
        multiplier = 1.25f,
        multiplierLabel = "1.25x",
        secondsPerWord = 8,
        description = "Complex words · 1.25x pts · 8s/word"
    )
}

enum class AppScreen {
    SPLASH,
    WELCOME,
    LEVEL_MAP,
    GAMEPLAY,
    EXAM,
    FAQ,
    PRIVACY,
    TERMS,
    HOW_TO_PLAY
}

object TimeTrialScoring {
    fun getLevelTimeLimitSeconds(wordCount: Int, difficulty: GameDifficulty): Int =
        wordCount.coerceAtLeast(1) * difficulty.secondsPerWord

    fun calculateSpeedBonus(remainingSeconds: Int, totalSeconds: Int, multiplier: Float): Int {
        if (remainingSeconds <= 0 || totalSeconds <= 0) return 0
        val ratio = (remainingSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
        return kotlin.math.max(5, kotlin.math.round(30f * ratio * multiplier).toInt())
    }
}

enum class DailyMissionId {
    PASS_LEVEL,
    EXTRA_WORDS,
    COMPLETE_EXAM
}

data class DailyMissionDefinition(
    val id: DailyMissionId,
    val title: String,
    val arabic: String,
    val target: Int,
    val rewardPoints: Int = DailyMissionsManager.REWARD_POINTS
)

data class DailyMissionsState(
    val date: String = DailyMissionsManager.getTodayDateKey(),
    val levelsPassed: Int = 0,
    val extraWordsFound: Int = 0,
    val examsCompleted: Int = 0,
    val claimedIds: Set<DailyMissionId> = emptySet()
)

object DailyMissionsManager {
    const val REWARD_POINTS = 15

    val missions: List<DailyMissionDefinition> = listOf(
        DailyMissionDefinition(
            id = DailyMissionId.PASS_LEVEL,
            title = "Pass 1 level",
            arabic = "أكمل مستوى واحداً",
            target = 1
        ),
        DailyMissionDefinition(
            id = DailyMissionId.EXTRA_WORDS,
            title = "Find 2 extra words (3+ letters)",
            arabic = "اعثر على كلمتين إضافيتين (٣+ أحرف)",
            target = 2
        ),
        DailyMissionDefinition(
            id = DailyMissionId.COMPLETE_EXAM,
            title = "Complete 1 exam",
            arabic = "أنهِ اختباراً واحداً",
            target = 1
        )
    )

    fun getTodayDateKey(): String {
        val cal = java.util.Calendar.getInstance()
        val year = cal.get(java.util.Calendar.YEAR)
        val month = cal.get(java.util.Calendar.MONTH) + 1
        val day = cal.get(java.util.Calendar.DAY_OF_MONTH)
        return String.format(java.util.Locale.US, "%04d-%02d-%02d", year, month, day)
    }

    fun normalizeForToday(
        state: DailyMissionsState,
        today: String = getTodayDateKey()
    ): DailyMissionsState {
        return if (state.date == today) {
            state
        } else {
            DailyMissionsState(date = today)
        }
    }

    fun getProgress(state: DailyMissionsState, missionId: DailyMissionId): Int = when (missionId) {
        DailyMissionId.PASS_LEVEL -> state.levelsPassed
        DailyMissionId.EXTRA_WORDS -> state.extraWordsFound
        DailyMissionId.COMPLETE_EXAM -> state.examsCompleted
    }

    fun getClaimableCount(state: DailyMissionsState): Int {
        val normalized = normalizeForToday(state)
        return missions.count { mission ->
            getProgress(normalized, mission.id) >= mission.target &&
                !normalized.claimedIds.contains(mission.id)
        }
    }
}
