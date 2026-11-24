package com.wordeep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordeep.app.model.Level

private sealed interface MapRowItem {
    data class LevelRow(val level: Level, val levelNumber: Int) : MapRowItem
    data class ExamRow(val examNumber: Int) : MapRowItem
    data object ComingSoonBanner : MapRowItem
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelMapScreen(
    levels: List<Level>,
    unlockedLevel: Int,
    score: Int,
    examBestScores: Map<Int, Int>,
    claimableMissionsCount: Int,
    onBack: () -> Unit,
    onSelectLevel: (Int) -> Unit,
    onStartExam: (Int) -> Unit,
    onOpenDailyMissions: () -> Unit
) {
    val mapItems = remember(levels) {
        buildList {
            levels.forEachIndexed { index, level ->
                val levelNumber = index + 1
                add(MapRowItem.LevelRow(level = level, levelNumber = levelNumber))
                if (levelNumber % 5 == 0) {
                    add(MapRowItem.ExamRow(examNumber = levelNumber / 5))
                }
            }
            add(MapRowItem.ComingSoonBanner)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "World Map (خريطة المستويات)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF0F172A)
                        )
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.85f))
                                .clickable(onClick = onOpenDailyMissions),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CardGiftcard,
                                contentDescription = "Daily Missions",
                                tint = Color(0xFF0369A1),
                                modifier = Modifier.size(18.dp)
                            )
                            if (claimableMissionsCount > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF59E0B))
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFFEF08A)
                        ) {
                            Text(
                                text = "⭐ Score: $score",
                                color = Color(0xFF854D0E),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF38BDF8))
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE0F2FE), Color(0xFFFEF9C3), Color(0xFFDCFCE7))
                    )
                )
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item(key = "header_subtitle") {
                Text(
                    text = "Select Your Puzzle Level! (اختر مستواك)",
                    color = Color(0xFF0369A1),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(
                items = mapItems,
                key = { item ->
                    when (item) {
                        is MapRowItem.LevelRow -> "level_${item.level.id}"
                        is MapRowItem.ExamRow -> "exam_${item.examNumber}"
                        MapRowItem.ComingSoonBanner -> "coming_soon_banner"
                    }
                }
            ) { item ->
                when (item) {
                    is MapRowItem.LevelRow -> {
                        val isUnlocked = item.levelNumber <= unlockedLevel
                        val isPassed = item.levelNumber < unlockedLevel
                        val subtitle = if (!item.level.category.isNullOrBlank()) {
                            "${item.level.arabic} • ${item.level.category}"
                        } else {
                            item.level.arabic
                        }

                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isUnlocked) Color.White else Color(0xFFE2E8F0)
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = if (isUnlocked) 4.dp else 1.dp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(88.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .clickable(enabled = isUnlocked) {
                                    onSelectLevel(item.levelNumber - 1)
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 18.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isUnlocked) Color(0xFF0284C7) else Color(0xFF64748B)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (isUnlocked) {
                                            Text(
                                                text = item.levelNumber.toString(),
                                                color = Color.White,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Filled.Lock,
                                                contentDescription = "Locked",
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column {
                                        if (isUnlocked) {
                                            Text(
                                                text = "Level.${item.levelNumber}: ${item.level.mainWord}",
                                                color = Color(0xFF0F172A),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = subtitle,
                                                color = Color(0xFF475569),
                                                fontSize = 13.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        } else {
                                            // Do not spoil future locked level words or translations
                                            Text(
                                                text = "Level.${item.levelNumber}",
                                                color = Color(0xFF64748B),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1
                                            )
                                            Text(
                                                text = "Locked",
                                                color = Color(0xFF64748B),
                                                fontSize = 12.sp,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }

                                if (isUnlocked) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                        repeat(3) {
                                            Icon(
                                                imageVector = Icons.Filled.Star,
                                                contentDescription = null,
                                                tint = if (isPassed) {
                                                    Color(0xFFEAB308)
                                                } else {
                                                    Color(0xFFEAB308).copy(alpha = 0.35f)
                                                },
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    is MapRowItem.ExamRow -> {
                        val isExamUnlocked = item.examNumber * 5 <= unlockedLevel
                        val bestScore = examBestScores[item.examNumber]

                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isExamUnlocked) Color(0xFFFDE047) else Color(0xFFE2E8F0)
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = if (isExamUnlocked) 4.dp else 1.dp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .clickable(enabled = isExamUnlocked) {
                                    onStartExam(item.examNumber)
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 18.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isExamUnlocked) Color(0xFFD97706) else Color(0xFF64748B)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isExamUnlocked) {
                                                Icons.Filled.School
                                            } else {
                                                Icons.Filled.Lock
                                            },
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Column {
                                        Text(
                                            text = "⭐ EXAM CHECKPOINT ${item.examNumber} ⭐",
                                            color = if (isExamUnlocked) Color(0xFF78350F) else Color(0xFF64748B),
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = when {
                                                !isExamUnlocked -> "Locked"
                                                bestScore != null -> "Best: $bestScore/10 · Tap to retake"
                                                else -> "Test your vocabulary (10 pts max)"
                                            },
                                            color = if (isExamUnlocked) Color(0xFF92400E) else Color(0xFF475569),
                                            fontSize = 12.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                if (isExamUnlocked && bestScore != null) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color.White.copy(alpha = 0.85f)
                                    ) {
                                        Text(
                                            text = "$bestScore/10",
                                            color = Color(0xFF78350F),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    MapRowItem.ComingSoonBanner -> {
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp, bottom = 16.dp)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF38BDF8).copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(24.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE0F2FE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color(0xFF0284C7),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "More challenging words and exams are coming soon. Keep up the good work",
                                    color = Color(0xFF0F172A),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
