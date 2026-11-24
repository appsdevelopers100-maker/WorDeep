package com.wordeep.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordeep.app.model.ExtraWordFeedback
import com.wordeep.app.model.GameDifficulty
import com.wordeep.app.model.Level
import com.wordeep.app.model.TimeTrialScoring
import com.wordeep.app.model.WordItem
import com.wordeep.app.viewmodel.WordGameViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GameplayScreen(
    level: Level,
    levelNumber: Int,
    totalLevels: Int,
    difficulty: GameDifficulty,
    timeTrialEnabled: Boolean,
    levelTimeLimitSeconds: Int,
    timeRemainingSeconds: Int,
    isTimeUp: Boolean,
    lastSpeedBonus: Int,
    extraWordPoints: Int,
    score: Int,
    selectedLetterIndices: List<Int>,
    extractedWords: List<WordItem>,
    discoveredExtraWords: List<String>,
    extraWordFeedback: ExtraWordFeedback?,
    canClaimFormedExtraWord: Boolean,
    isFormedExtraAlreadyClaimed: Boolean,
    isLevelComplete: Boolean,
    soundEnabled: Boolean,
    onBack: () -> Unit,
    onToggleSound: () -> Unit,
    onSpeakWord: (String) -> Unit,
    onLetterClick: (Int) -> Unit,
    onClaimExtraWord: () -> Unit,
    onClearSelection: () -> Unit,
    onUseHint: () -> Unit,
    onRetryLevel: () -> Unit,
    onNextLevel: () -> Unit
) {
    val visibleWords = extractedWords.filter { it.length >= 3 }
    val groupedWords = visibleWords.groupBy { it.length }.toSortedMap()
    val formedWord = selectedLetterIndices.mapNotNull { level.mainWord.getOrNull(it) }.joinToString("")
    val hasMoreHints = visibleWords.any { !it.isDiscovered && it.revealedIndices.size < it.length }

    val haptic = LocalHapticFeedback.current
    val discoveredCount = visibleWords.count { it.isDiscovered }
    var prevDiscoveredCount by remember(level.id) { mutableIntStateOf(discoveredCount) }
    var prevExtraCount by remember(level.id) { mutableIntStateOf(discoveredExtraWords.size) }

    LaunchedEffect(discoveredCount, discoveredExtraWords.size) {
        if (discoveredCount > prevDiscoveredCount || discoveredExtraWords.size > prevExtraCount) {
            if (soundEnabled) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
        prevDiscoveredCount = discoveredCount
        prevExtraCount = discoveredExtraWords.size
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Level $levelNumber/$totalLevels",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            maxLines = 1
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (difficulty) {
                                GameDifficulty.EASY -> Color(0xFF16A34A)
                                GameDifficulty.MEDIUM -> Color(0xFF0284C7)
                                GameDifficulty.HARD -> Color(0xFFD97706)
                            }
                        ) {
                            Text(
                                text = "${difficulty.label} ${difficulty.multiplierLabel}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to World Map",
                            tint = Color(0xFF0F172A)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onToggleSound) {
                        Icon(
                            imageVector = if (soundEnabled) {
                                Icons.AutoMirrored.Filled.VolumeUp
                            } else {
                                Icons.AutoMirrored.Filled.VolumeOff
                            },
                            contentDescription = "Toggle Sound",
                            tint = Color(0xFF0F172A)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFEF08A),
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text(
                            text = "⭐ $score",
                            color = Color(0xFF854D0E),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF38BDF8))
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE0F2FE), Color(0xFFFEF9C3), Color(0xFFDCFCE7))
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Time-Trial Countdown Timer Bar
                if (timeTrialEnabled) {
                    val isUrgent = timeRemainingSeconds <= 10
                    val isWarning = timeRemainingSeconds in 11..20
                    val progress = if (levelTimeLimitSeconds > 0) {
                        (timeRemainingSeconds.toFloat() / levelTimeLimitSeconds.toFloat()).coerceIn(0f, 1f)
                    } else 0f
                    val projectedSpeedBonus = TimeTrialScoring.calculateSpeedBonus(
                        timeRemainingSeconds,
                        levelTimeLimitSeconds,
                        difficulty.multiplier
                    )
                    val formattedTime = String.format(
                        Locale.US,
                        "%02d:%02d",
                        timeRemainingSeconds / 60,
                        timeRemainingSeconds % 60
                    )

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = when {
                            isUrgent -> Color(0xFFFEF2F2)
                            isWarning -> Color(0xFFFFFBEB)
                            else -> Color.White.copy(alpha = 0.94f)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = when {
                                    isUrgent -> Color(0xFFFCA5A5)
                                    isWarning -> Color(0xFFFCD34D)
                                    else -> Color(0xFFBAE6FD)
                                },
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Timer,
                                        contentDescription = "Countdown timer",
                                        tint = when {
                                            isUrgent -> Color(0xFFDC2626)
                                            isWarning -> Color(0xFFD97706)
                                            else -> Color(0xFF0284C7)
                                        },
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = formattedTime,
                                        color = when {
                                            isUrgent -> Color(0xFFB91C1C)
                                            isWarning -> Color(0xFFB45309)
                                            else -> Color(0xFF0F172A)
                                        },
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "(${visibleWords.size}×${difficulty.secondsPerWord}s)",
                                        color = Color(0xFF64748B),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFDCFCE7)
                                    ) {
                                        Text(
                                            text = "Extra +$extraWordPoints pts",
                                            color = Color(0xFF15803D),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Filled.Bolt,
                                            contentDescription = null,
                                            tint = Color(0xFFF59E0B),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text(
                                            text = "+$projectedSpeedBonus",
                                            color = Color(0xFF15803D),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                color = when {
                                    isUrgent -> Color(0xFFEF4444)
                                    isWarning -> Color(0xFFF59E0B)
                                    else -> Color(0xFF0284C7)
                                },
                                trackColor = Color(0xFFE2E8F0),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }
                    }
                }

                // Main Word Card: English word + Arabic translation side-by-side (NO "Science" tag)
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = level.mainWord,
                                color = Color(0xFF0F172A),
                                fontSize = when {
                                    level.mainWord.length >= 9 -> 20.sp
                                    level.mainWord.length >= 7 -> 22.sp
                                    else -> 25.sp
                                },
                                fontWeight = FontWeight.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFE0F2FE)
                            ) {
                                Text(
                                    text = level.arabic,
                                    color = Color(0xFF0369A1),
                                    fontSize = if (level.mainWord.length >= 8) 14.sp else 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            IconButton(
                                onClick = { onSpeakWord(level.mainWord) },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Pronounce main word",
                                    tint = Color(0xFF0284C7),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Responsive letter tile row sized dynamically so words like DIAMETER / CREATIVITY never clip
                        BoxWithConstraints(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            val letterCount = level.mainWord.length.coerceAtLeast(1)
                            val gapDp = when {
                                letterCount >= 9 -> 4.dp
                                letterCount >= 7 -> 5.dp
                                else -> 7.dp
                            }
                            val rawTileSize = (maxWidth - (gapDp * (letterCount - 1))) / letterCount
                            val tileSize = rawTileSize.coerceAtMost(48.dp)
                            val letterFontSize = when {
                                tileSize < 32.dp -> 14.sp
                                tileSize < 38.dp -> 16.sp
                                tileSize < 44.dp -> 19.sp
                                else -> 21.sp
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(gapDp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                level.mainWord.forEachIndexed { idx, ch ->
                                    val isSelected = selectedLetterIndices.contains(idx)
                                    val lastSelectedIdx = selectedLetterIndices.lastOrNull() ?: -1
                                    val isDisabledBackward =
                                        selectedLetterIndices.isNotEmpty() && idx < lastSelectedIdx

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = when {
                                            isSelected -> Color(0xFF22C55E)
                                            isDisabledBackward -> Color(0xFFF1F5F9)
                                            else -> Color(0xFF38BDF8)
                                        },
                                        shadowElevation = if (isSelected) 5.dp else 2.dp,
                                        modifier = Modifier
                                            .size(tileSize)
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable(enabled = !isLevelComplete && !isTimeUp && !isDisabledBackward) {
                                                if (soundEnabled) {
                                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                                }
                                                onLetterClick(idx)
                                            }
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = ch.toString(),
                                                color = if (isDisabledBackward) Color(0xFF94A3B8) else Color.White,
                                                fontSize = letterFontSize,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Current selection preview + Clear + Claim Extra +5 + 50pt 1-letter Hint
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF1F5F9),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = if (formedWord.isEmpty()) "Select left → right (3+)" else formedWord,
                                    color = if (formedWord.isEmpty()) Color(0xFF64748B) else Color(0xFF0F172A),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp)
                                )
                            }

                            if (canClaimFormedExtraWord) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isFormedExtraAlreadyClaimed) {
                                        Color(0xFFE2E8F0)
                                    } else {
                                        Color(0xFF8B5CF6)
                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable(onClick = onClaimExtraWord)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = "Claim Extra Word",
                                            tint = if (isFormedExtraAlreadyClaimed) Color(0xFF475569) else Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = if (isFormedExtraAlreadyClaimed) {
                                                "Claimed · +0"
                                            } else {
                                                "Extra +$extraWordPoints"
                                            },
                                            color = if (isFormedExtraAlreadyClaimed) Color(0xFF475569) else Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (selectedLetterIndices.isNotEmpty()) {
                                    Color(0xFFFEE2E2)
                                } else {
                                    Color(0xFFF1F5F9)
                                },
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(enabled = selectedLetterIndices.isNotEmpty(), onClick = onClearSelection)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = "Clear selection",
                                        tint = if (selectedLetterIndices.isNotEmpty()) {
                                            Color(0xFFDC2626)
                                        } else {
                                            Color(0xFF94A3B8)
                                        },
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            val canHint = score >= WordGameViewModel.HINT_COST && hasMoreHints && !isLevelComplete && !isTimeUp
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (canHint) Color(0xFFF59E0B) else Color(0xFFE2E8F0),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(enabled = canHint, onClick = onUseHint)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Lightbulb,
                                        contentDescription = "Hint (1 letter for 50 points)",
                                        tint = if (canHint) Color.White else Color(0xFF94A3B8),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Hint 50",
                                        color = if (canHint) Color.White else Color(0xFF94A3B8),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Light celebratory banner when an Extra Word is discovered
                AnimatedVisibility(
                    visible = extraWordFeedback != null,
                    enter = fadeIn() + scaleIn(initialScale = 0.92f),
                    exit = fadeOut() + scaleOut(targetScale = 0.95f)
                ) {
                    extraWordFeedback?.let { feedback ->
                        val isNew = feedback.points > 0
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (isNew) Color(0xFFDCFCE7) else Color(0xFFFEF3C7),
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 1.dp,
                                    color = if (isNew) Color(0xFF22C55E) else Color(0xFFF59E0B),
                                    shape = RoundedCornerShape(18.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AutoAwesome,
                                    contentDescription = null,
                                    tint = if (isNew) Color(0xFF15803D) else Color(0xFFB45309),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (!feedback.isDuplicate && feedback.points > 0) {
                                        val arabicPart = feedback.arabic?.let { " ($it)" } ?: ""
                                        "Extra Word: ${feedback.word}$arabicPart (+${feedback.points} pts!)"
                                    } else {
                                        val arabicPart = feedback.arabic?.let { " ($it)" } ?: ""
                                        "Already claimed: ${feedback.word}$arabicPart (+0 pts)"
                                    },
                                    color = if (isNew) Color(0xFF15803D) else Color(0xFFB45309),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Time's Up retry card
                if (timeTrialEnabled && isTimeUp && !isLevelComplete) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, Color(0xFFEF4444), RoundedCornerShape(24.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Timer,
                                contentDescription = null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(34.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Time's Up! (انتهى الوقت!)",
                                color = Color(0xFF991B1B),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Found $discoveredCount of ${visibleWords.size} hidden words for ${level.mainWord}.",
                                color = Color(0xFF7F1D1D),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onRetryLevel,
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Try Again (إعادة المحاولة)",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = onBack,
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(44.dp)
                            ) {
                                Text(
                                    text = "Back to World Map",
                                    color = Color(0xFF334155),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Level Complete celebration card
                if (isLevelComplete) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, Color(0xFF22C55E), RoundedCornerShape(24.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎉 LEVEL $levelNumber COMPLETE! 🎉",
                                color = Color(0xFF166534),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "All hidden words discovered! (أحسنت!)",
                                color = Color(0xFF15803D),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            val levelWordsEarned = visibleWords.sumOf { it.earnedPoints }
                            val allReplay = visibleWords.isNotEmpty() && visibleWords.all { it.wasAlreadyRewarded }
                            if (timeTrialEnabled && lastSpeedBonus > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFFFFBEB),
                                    modifier = Modifier.border(
                                        1.dp,
                                        Color(0xFFFCD34D),
                                        RoundedCornerShape(12.dp)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Bolt,
                                            contentDescription = null,
                                            tint = Color(0xFFD97706),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Speed Bonus: +$lastSpeedBonus pts (${timeRemainingSeconds}s left!)",
                                            color = Color(0xFF92400E),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                            if (allReplay) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Replay complete · Previously claimed words (+0 pts)",
                                    color = Color(0xFF475569),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )
                            } else if (levelWordsEarned > 0) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "+$levelWordsEarned pts earned (${difficulty.label} ${difficulty.multiplierLabel})",
                                    color = Color(0xFF15803D),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onNextLevel,
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (levelNumber % 5 == 0) {
                                        "Start Exam Checkpoint ${levelNumber / 5}"
                                    } else if (levelNumber < totalLevels) {
                                        "Next Level (المستوى التالي)"
                                    } else {
                                        "Back to World Map"
                                    },
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Target hidden words grouped strictly from 3+ letters
                groupedWords.forEach { (wordLen, groupItems) ->
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "$wordLen-Letter Words (${groupItems.count { it.isDiscovered }}/${groupItems.size})",
                                color = Color(0xFF0369A1),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )

                            groupItems.forEach { item ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                        for (charIdx in 0 until item.length) {
                                            val isRevealed = item.isDiscovered || item.revealedIndices.contains(charIdx)
                                            val ch = item.word.getOrNull(charIdx)?.toString() ?: ""
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = when {
                                                    item.isDiscovered -> Color(0xFF22C55E)
                                                    isRevealed -> Color(0xFFF59E0B)
                                                    else -> Color(0xFFE2E8F0)
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Text(
                                                        text = if (isRevealed) ch else "",
                                                        color = Color.White,
                                                        fontSize = 15.sp,
                                                        fontWeight = FontWeight.ExtraBold
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    if (item.isDiscovered) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            if (item.wasAlreadyRewarded) {
                                                Surface(
                                                    shape = RoundedCornerShape(8.dp),
                                                    color = Color(0xFFE2E8F0)
                                                ) {
                                                    Text(
                                                        text = "Already claimed · +0",
                                                        color = Color(0xFF475569),
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                            } else if (item.earnedPoints > 0) {
                                                Text(
                                                    text = "+${item.earnedPoints}",
                                                    color = Color(0xFF16A34A),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.ExtraBold
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                            }
                                            Text(
                                                text = item.arabic,
                                                color = Color(0xFF15803D),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            IconButton(
                                                onClick = { onSpeakWord(item.word) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                                    contentDescription = "Speak ${item.word}",
                                                    tint = Color(0xFF16A34A),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    } else if (item.revealedIndices.isNotEmpty()) {
                                        Text(
                                            text = "Hinted",
                                            color = Color(0xFFD97706),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Discovered Extra Words list with Arabic translation shown next to each word
                if (discoveredExtraWords.isNotEmpty()) {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Text(
                                text = "Extra Words Found (+$extraWordPoints pts each)",
                                color = Color(0xFF7C3AED),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                discoveredExtraWords.forEach { extra ->
                                    val extraArabic = com.wordeep.app.data.EnglishDictionary.getArabicTranslation(extra)
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFFF3E8FF),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { onSpeakWord(extra) }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                                contentDescription = "Pronounce $extra",
                                                tint = Color(0xFF6B21A8),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Text(
                                                text = extra,
                                                color = Color(0xFF6B21A8),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.ExtraBold
                                            )
                                            if (!extraArabic.isNullOrBlank()) {
                                                Text(
                                                    text = "· $extraArabic",
                                                    color = Color(0xFF7C3AED),
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                            Text(
                                                text = "(+$extraWordPoints)",
                                                color = Color(0xFF6B21A8),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
