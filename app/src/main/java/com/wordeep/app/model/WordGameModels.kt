package com.wordeep.app.model

data class WordItem(
    val word: String,
    val arabic: String,
    val length: Int,
    var isDiscovered: Boolean = false
)

data class Level(
    val id: Int,
    val mainWord: String,
    val arabic: String,
    val category: String,
    val extractedWords: List<WordItem>
)

data class ExamQuestion(
    val id: Int,
    val question: String,
    val arabic: String,
    val options: List<String>,
    val correctIndex: Int
)

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
