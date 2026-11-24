package com.wordeep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordeep.app.model.AppScreen
import com.wordeep.app.model.Level
import com.wordeep.app.viewmodel.WordGameViewModel

sealed class MapItem {
    data class LevelItem(val level: Level, val levelNumber: Int) : MapItem()
    data class ExamItem(val examNumber: Int) : MapItem()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelMapScreen(viewModel: WordGameViewModel) {
    val mapItems = mutableListOf<MapItem>()
    viewModel.levels.forEachIndexed { index, level ->
        val levelNum = index + 1
        mapItems.add(MapItem.LevelItem(level, levelNum))
        if (levelNum % 5 == 0) {
            mapItems.add(MapItem.ExamItem(levelNum / 5))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "World Map (خريطة المستويات)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(AppScreen.WELCOME) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFEF08A),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = "⭐ Score: ${viewModel.score}",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Select Your Puzzle Level! (اختر مستواك)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0369A1)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                items(mapItems) { item ->
                    when (item) {
                        is MapItem.LevelItem -> {
                            val isUnlocked = item.levelNumber <= viewModel.unlockedLevel
                            LevelCard(
                                levelNumber = item.levelNumber,
                                mainWord = item.level.mainWord,
                                arabic = item.level.arabic,
                                category = item.level.category,
                                isUnlocked = isUnlocked,
                                onClick = {
                                    if (isUnlocked) {
                                        viewModel.selectLevel(item.levelNumber - 1)
                                    }
                                }
                            )
                        }
                        is MapItem.ExamItem -> {
                            val isUnlocked = (item.examNumber * 5) <= viewModel.unlockedLevel
                            ExamCheckpointCard(
                                examNumber = item.examNumber,
                                isUnlocked = isUnlocked,
                                onClick = {
                                    if (isUnlocked) {
                                        viewModel.startExam()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LevelCard(
    levelNumber: Int,
    mainWord: String,
    arabic: String,
    category: String,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isUnlocked) Color.White else Color(0xFFE2E8F0)

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor,
        shadowElevation = if (isUnlocked) 8.dp else 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable(enabled = isUnlocked, onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = if (isUnlocked) Color(0xFF0284C7) else Color.Gray,
                    modifier = Modifier.size(50.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isUnlocked) {
                            Text(
                                text = "$levelNumber",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        } else {
                            Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Level $levelNumber: $mainWord",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = if (isUnlocked) Color(0xFF0F172A) else Color.Gray
                    )
                    Text(
                        text = "$arabic • $category",
                        fontSize = 13.sp,
                        color = if (isUnlocked) Color(0xFF475569) else Color.DarkGray
                    )
                }
            }

            if (isUnlocked) {
                Row {
                    repeat(3) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFEAB308),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExamCheckpointCard(
    examNumber: Int,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isUnlocked) Color(0xFFFDE047) else Color(0xFFE2E8F0)

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = backgroundColor,
        shadowElevation = if (isUnlocked) 8.dp else 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(enabled = isUnlocked, onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = if (isUnlocked) Color(0xFFD97706) else Color.Gray,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isUnlocked) {
                            Icon(Icons.Default.School, contentDescription = null, tint = Color.White)
                        } else {
                            Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "⭐ EXAM CHECKPOINT $examNumber ⭐",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isUnlocked) Color(0xFF78350F) else Color.Gray
                    )
                    Text(
                        text = "Test your vocabulary & spelling rules",
                        fontSize = 12.sp,
                        color = if (isUnlocked) Color(0xFF92400E) else Color.DarkGray

                    )
                }
            }
        }
    }
}
