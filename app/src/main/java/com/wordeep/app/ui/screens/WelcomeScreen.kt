package com.wordeep.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordeep.app.R
import com.wordeep.app.model.AppScreen
import com.wordeep.app.model.GameDifficulty

@Composable
fun WelcomeScreen(
    difficulty: GameDifficulty,
    timeTrialEnabled: Boolean,
    claimableMissionsCount: Int,
    onSelectDifficulty: (GameDifficulty) -> Unit,
    onToggleTimeTrial: (Boolean) -> Unit,
    onOpenDailyMissions: () -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2FA8D8), Color(0xFF48BFE3), Color(0xFF68C75A))
                )
            )
    ) {
        // Respect bottom system navigation bar / safe-area insets while keeping background seamless
        Box(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            // Artwork frame preserving exact 1376:3064 proportions so all puzzle hit-targets align cleanly
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1376f / 3064f, matchHeightConstraintsFirst = true)
            ) {
                val artWidth = maxWidth
                val artHeight = maxHeight

                Image(
                    painter = painterResource(id = R.drawable.launchpage),
                    contentDescription = "WORDIG Launch Screen",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

                // START PLAYING (center orange puzzle piece: top 44%, left 22%, w 56%, h 16.5%)
                ArtworkHitZone(
                    modifier = Modifier
                        .offset(x = artWidth * 0.22f, y = artHeight * 0.44f)
                        .width(artWidth * 0.56f)
                        .height(artHeight * 0.165f),
                    onClick = { onNavigate(AppScreen.LEVEL_MAP) }
                )

                // HOW TO PLAY (left orange puzzle piece: top 62.5%, left 13%, w 37.5%, h 9%)
                ArtworkHitZone(
                    modifier = Modifier
                        .offset(x = artWidth * 0.13f, y = artHeight * 0.625f)
                        .width(artWidth * 0.375f)
                        .height(artHeight * 0.09f),
                    onClick = { onNavigate(AppScreen.HOW_TO_PLAY) }
                )

                // FAQ (right teal puzzle piece: top 61.5%, left 55.5%, w 32%, h 10%)
                ArtworkHitZone(
                    modifier = Modifier
                        .offset(x = artWidth * 0.555f, y = artHeight * 0.615f)
                        .width(artWidth * 0.32f)
                        .height(artHeight * 0.10f),
                    onClick = { onNavigate(AppScreen.FAQ) }
                )

                // Privacy Policy (bottom left white puzzle piece: top 78.5%, left 9.5%, w 41%, h 9.5%)
                ArtworkHitZone(
                    modifier = Modifier
                        .offset(x = artWidth * 0.095f, y = artHeight * 0.785f)
                        .width(artWidth * 0.41f)
                        .height(artHeight * 0.095f),
                    onClick = { onNavigate(AppScreen.PRIVACY) }
                )

                // Terms & Conditions (bottom right white puzzle piece: top 78.5%, left 51%, w 39.5%, h 9.5%)
                ArtworkHitZone(
                    modifier = Modifier
                        .offset(x = artWidth * 0.51f, y = artHeight * 0.785f)
                        .width(artWidth * 0.395f)
                        .height(artHeight * 0.095f),
                    onClick = { onNavigate(AppScreen.TERMS) }
                )
            }

            // One Small Compact Daily Missions Button in Top-Right Sky Area
            PuzzleTileButton(
                isActive = false,
                activeTopColor = Color.White,
                activeBottomColor = Color(0xFFEEF4F8),
                activeBevelColor = Color(0xFFB4C5D5),
                activeBorderColor = Color(0xFFD5E1EB),
                role = Role.Button,
                onClick = onOpenDailyMissions,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 8.dp, end = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CardGiftcard,
                        contentDescription = "Daily Missions",
                        tint = Color(0xFF0EA5E9),
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Missions",
                        color = Color(0xFF1E3346),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    if (claimableMissionsCount > 0) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF59E0B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = claimableMissionsCount.toString(),
                                color = Color.White,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            // 4 Matching Game-Tile Buttons in Two Clean Rows Below Privacy Policy & Terms & Conditions
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Row 1: 3 Difficulty Buttons (Easy / Medium / Hard)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GameDifficulty.entries.forEach { item ->
                        val isSelected = difficulty == item
                        PuzzleTileButton(
                            isActive = isSelected,
                            activeTopColor = Color(0xFF33C4CD),
                            activeBottomColor = Color(0xFF178E98),
                            activeBevelColor = Color(0xFF0E6972),
                            activeBorderColor = Color(0xFF6BE5ED),
                            role = Role.RadioButton,
                            onClick = { onSelectDifficulty(item) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.label,
                                    color = if (isSelected) Color.White else Color(0xFF1E3346),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isSelected) {
                                        Color.White.copy(alpha = 0.24f)
                                    } else {
                                        Color(0xFFDCE8F2)
                                    }
                                ) {
                                    Text(
                                        text = item.multiplierLabel,
                                        color = if (isSelected) Color.White else Color(0xFF2B4358),
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        maxLines = 1,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Row 2: Compact Time Toggle Button (White Terms Tile when OFF / Gold How-to-Play Tile when ON)
                PuzzleTileButton(
                    isActive = timeTrialEnabled,
                    activeTopColor = Color(0xFFF7B234),
                    activeBottomColor = Color(0xFFDE8112),
                    activeBevelColor = Color(0xFFA85B06),
                    activeBorderColor = Color(0xFFFDD875),
                    role = Role.Switch,
                    onClick = { onToggleTimeTrial(!timeTrialEnabled) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Timer,
                            contentDescription = "Time Challenge",
                            tint = if (timeTrialEnabled) Color.White else Color(0xFF178E98),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (timeTrialEnabled) "Time-Trial: ON" else "Time-Trial: OFF",
                            color = if (timeTrialEnabled) Color.White else Color(0xFF1E3346),
                            fontSize = 11.sp,
                            fontWeight = if (timeTrialEnabled) FontWeight.ExtraBold else FontWeight.Bold,
                            maxLines = 1
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (timeTrialEnabled) {
                                Color.White.copy(alpha = 0.24f)
                            } else {
                                Color(0xFFDCE8F2)
                            }
                        ) {
                            Text(
                                text = if (timeTrialEnabled) {
                                    "${difficulty.secondsPerWord}s/word · +10"
                                } else {
                                    "Extra +5"
                                },
                                color = if (timeTrialEnabled) Color.White else Color(0xFF2B4358),
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                maxLines = 1,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PuzzleTileButton(
    isActive: Boolean,
    activeTopColor: Color,
    activeBottomColor: Color,
    activeBevelColor: Color,
    activeBorderColor: Color,
    role: Role,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)
    val bevelColor = if (isActive) activeBevelColor else Color(0xFFB4C5D5)
    val borderColor = if (isActive) activeBorderColor else Color(0xFFD5E1EB)
    val faceBrush = if (isActive) {
        Brush.verticalGradient(colors = listOf(activeTopColor, activeBottomColor))
    } else {
        Brush.verticalGradient(colors = listOf(Color.White, Color(0xFFEEF4F8)))
    }

    Surface(
        shape = shape,
        color = bevelColor,
        shadowElevation = if (isActive) 5.dp else 3.dp,
        modifier = modifier
            .clip(shape)
            .clickable(role = role, onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 3.dp)
                .clip(shape)
                .background(faceBrush)
                .border(width = 1.dp, color = borderColor, shape = shape),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}

@Composable
private fun ArtworkHitZone(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    )
}
