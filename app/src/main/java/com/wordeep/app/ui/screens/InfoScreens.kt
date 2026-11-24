package com.wordeep.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wordeep.app.data.WordGameRepository
import com.wordeep.app.model.AppScreen
import com.wordeep.app.viewmodel.WordGameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    title: String,
    content: String,
    faqList: List<Pair<String, String>>? = null,
    viewModel: WordGameViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(AppScreen.WELCOME) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (faqList != null) {
                    items(faqList) { faq ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            shadowElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = faq.first, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF0369A1))
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = faq.second, fontSize = 14.sp, color = Color(0xFF334155))
                            }
                        }
                    }
                } else {
                    item {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            shadowElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = content,
                                fontSize = 15.sp,
                                color = Color(0xFF334155),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FAQScreen(viewModel: WordGameViewModel) {
    InfoScreen(title = "FAQ (الأسئلة الشائعة)", content = "", faqList = WordGameRepository.faqList, viewModel = viewModel)
}

@Composable
fun PrivacyScreen(viewModel: WordGameViewModel) {
    InfoScreen(title = "Privacy Policy (سياسة الخصوصية)", content = WordGameRepository.privacyPolicyText, viewModel = viewModel)
}

@Composable
fun TermsScreen(viewModel: WordGameViewModel) {
    InfoScreen(title = "Terms & Conditions (الشروط والأحكام)", content = WordGameRepository.termsText, viewModel = viewModel)
}

@Composable
fun HowToPlayScreen(viewModel: WordGameViewModel) {
    val howToPlayText = """
EVERY WORD HIDES MORE.

WORDIG is a word puzzle game where one main word hides many smaller words. Your challenge is to discover them.

THE MAIN RULE:
Letters must always be selected from left to right. You can skip letters, but you cannot go backward or return to an earlier letter after selecting a later one.

FIND THE HIDDEN WORDS:
Around the main word, you will see word boxes containing empty squares. The number of squares shows the number of letters in a hidden word (e.g. [ ] [ ] [ ] means a 3-letter word).

SCORE MORE POINTS:
Longer words are worth more points. Each successfully discovered extra word awards 5 points. Hint cost is 20 points.
    """.trimIndent()
    InfoScreen(title = "How to Play (طريقة اللعب)", content = howToPlayText, viewModel = viewModel)
}
