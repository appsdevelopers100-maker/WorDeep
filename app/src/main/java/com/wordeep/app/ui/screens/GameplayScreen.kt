package com.wordeep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordeep.app.model.AppScreen
import com.wordeep.app.model.WordItem
import com.wordeep.app.viewmodel.WordGameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameplayScreen(viewModel: WordGameViewModel) {
    val level = viewModel.currentLevel
    val selectedIndices = viewModel.selectedLetterIndices.value
    val extractedWords = viewModel.currentExtractedWords.value
    val discoveredExtraWords = viewModel.discoveredExtraWords.value
    val formedWord = selectedIndices.map { level.mainWord[it] }.joinToString("")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "L${level.id}: ${level.mainWord}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(AppScreen.LEVEL_MAP) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Speaker button for main word
                    IconButton(onClick = { viewModel.speakWord(level.mainWord) }, modifier = Modifier.size(36.dp)) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Pronounce", tint = Color(0xFF0369A1))
                    }
                    // Hint Button (-50 pts)
                    Button(
                        onClick = { viewModel.useHint() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                        modifier = Modifier.padding(horizontal = 2.dp),
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(Icons.Default.Lightbulb, contentDescription = "Hint", tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("Hint", color = Color.White, fontSize = 11.sp)
                    }
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEF08A),
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            text = "⭐${viewModel.score}",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFF854D0E)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF38BDF8))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE0F2FE), Color(0xFFFEF9C3), Color(0xFFDCFCE7))
                    )
                )
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Category & Arabic Translation
                Text(
                    text = "${level.arabic} (${level.category})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0369A1)
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Main Word Interactive Letter Tiles
                val wordLength = level.mainWord.length
                val tileSize: Dp = when {
                    wordLength <= 6 -> 44.dp
                    wordLength <= 8 -> 36.dp
                    wordLength <= 10 -> 30.dp
                    else -> 26.dp
                }
                val fontSize: TextUnit = when {
                    wordLength <= 6 -> 20.sp
                    wordLength <= 8 -> 17.sp
                    wordLength <= 10 -> 14.sp
                    else -> 12.sp
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 6.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tap letters left-to-right (اختر الحروف من اليسار لليمين):",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        val scrollState = rememberScrollState()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(scrollState),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            level.mainWord.forEachIndexed { index, char ->
                                val isSelected = selectedIndices.contains(index)
                                LetterTile(
                                    char = char,
                                    isSelected = isSelected,
                                    size = tileSize,
                                    fontSize = fontSize,
                                    onClick = { viewModel.onLetterClicked(index) }
                                )
                                if (index < level.mainWord.length - 1) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Current Formed Word Display & Clear Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Formed: $formedWord",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0284C7)
                            )
                            IconButton(onClick = { viewModel.clearSelection() }) {
                                Icon(Icons.Default.Refresh, contentDescription = "Clear", tint = Color.Red)
                            }
                        }
                    }
                }

                // Extra Discovered Words Section (with Pronunciation Speaker Button)
                if (discoveredExtraWords.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Extra Words Discovered (+30 pts each):",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (extraWord in discoveredExtraWords) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFE0F2FE),
                                    shadowElevation = 2.dp
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = { viewModel.speakWord(extraWord) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.VolumeUp,
                                                contentDescription = "Pronounce",
                                                tint = Color(0xFF0369A1),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = extraWord,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = Color(0xFF0369A1)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Extracted Words Target Slots
                Text(
                    text = "Hidden Words to Discover (${extractedWords.count { it.isDiscovered }}/${extractedWords.size}):",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF166534)
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(extractedWords) { wordItem ->
                        ExtractedWordRow(
                            wordItem = wordItem,
                            formedWord = formedWord,
                            onPronounce = { viewModel.speakWord(wordItem.word) }
                        )
                    }
                }
            }

            // Level Complete Dialog / Overlay
            if (viewModel.isLevelComplete) {
                AlertDialog(
                    onDismissRequest = {},
                    title = { Text("🎉 LEVEL COMPLETED! (أحسنت!) 🎉", fontWeight = FontWeight.Bold) },
                    text = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("You discovered all hidden words for ${level.mainWord}!")
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Score bonus added!", fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = { viewModel.nextLevel() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E))
                        ) {
                            Text("Next Level (المستوى التالي)", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun LetterTile(char: Char, isSelected: Boolean, size: Dp, fontSize: TextUnit, onClick: () -> Unit) {
    val bg = if (isSelected) Color(0xFF38BDF8) else Color(0xFFF1F5F9)
    val textColor = if (isSelected) Color.White else Color(0xFF0F172A)

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = bg,
        shadowElevation = if (isSelected) 4.dp else 1.dp,
        modifier = Modifier
            .size(size)
            .clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "$char",
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun ExtractedWordRow(wordItem: WordItem, formedWord: String, onPronounce: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (wordItem.isDiscovered) Color(0xFFDCFCE7) else Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (wordItem.isDiscovered) {
                    IconButton(onClick = onPronounce, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Pronounce", tint = Color(0xFF166534))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = wordItem.word,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF166534)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "(${wordItem.arabic})",
                        fontSize = 14.sp,
                        color = Color(0xFF475569)
                    )
                } else {
                    // Display slots with sequential prefix matching
                    val isPrefixMatch = formedWord.isNotEmpty() && wordItem.word.startsWith(formedWord)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (i in 0 until wordItem.length) {
                            val isFilled = isPrefixMatch && i < formedWord.length
                            val charToDisplay = if (isFilled) wordItem.word[i].toString() else "_"

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isFilled) Color(0xFF38BDF8) else Color(0xFFE2E8F0),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = charToDisplay,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isFilled) Color.White else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Text(
                text = if (wordItem.isDiscovered) "✅" else "${wordItem.length} letters",
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}