@file:OptIn(ExperimentalMaterial3Api::class)

package my.packlol.myquizapp.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import my.packlol.myquizapp.QuizScreenState
import my.packlol.myquizapp.R
import my.packlol.myquizapp.composables.GradientBackground
import my.packlol.myquizapp.navigation.AppComposeNavigator
import my.packlol.myquizapp.theme.Purple80


@Composable
fun rememberQuizScreenState(
    navigator: AppComposeNavigator,
    username: String,
    scope: CoroutineScope = rememberCoroutineScope()
) = remember {
    QuizScreenState(navigator, username, scope)
}

@Composable
fun QuizScreen(
    navigator: AppComposeNavigator,
    username: String,
) {

    val state = rememberQuizScreenState(navigator = navigator, username = username)
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = state.snackbarHostState)
        }
    ) { paddingValues ->
        GradientBackground {
            Column(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                QuizQuestionHeading(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f),
                    question = state.currentQuestion.question,
                    imageRes = state.currentQuestion.image
                )
                QuizProgressBar(
                    modifier = Modifier
                        .padding(
                            horizontal = 22.dp
                        )
                        .fillMaxWidth()
                        .height(90.dp),
                    current = state.questionIdx,
                    max = state.questions.size
                )
                QuizQuestions(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    options = state.options,
                    selectedIdx = state.selectedIdx,
                    showCorrectAnswer = state.showCorrectAnswer,
                    correctIdx = state.currentQuestion.correctAnswer - 1,
                    onItemClick = {
                        state.selectedIdx = it
                    }
                )
                Button(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(0.8f)
                        .align(CenterHorizontally),
                    onClick = {
                        if (state.showCorrectAnswer) {
                            state.goToNextQuestion()
                        } else {
                            state.showCorrectAnswer()
                        }
                    }
                ) {
                    Text(
                        text = stringResource(
                            id = when {
                                !state.showCorrectAnswer -> R.string.submit
                                state.isLast -> R.string.finish
                                else -> R.string.go_to_next
                            }
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun QuizQuestions(
    modifier: Modifier,
    options: List<String>,
    selectedIdx: Int?,
    showCorrectAnswer: Boolean,
    correctIdx: Int,
    onItemClick: (idx: Int) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        itemsIndexed(
            items = options,
            key = { _, item -> item  }
        ) {idx, item ->
            val selected = selectedIdx == idx
            val shape = RoundedCornerShape(32.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(60.dp)
                    .clip(shape)
                    .background(
                        if (showCorrectAnswer && correctIdx == idx) {
                            Color.Green
                        } else if (showCorrectAnswer && idx == selectedIdx) {
                            Color.Red
                        } else {
                            Color.White
                        }
                    )
                    .border(
                        color = Purple80,
                        width = if (selected) 4.dp else 0.dp,
                        shape = shape
                    )
                    .clickable {
                        if (!showCorrectAnswer) {
                            onItemClick(idx)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    fontWeight = if (selected)
                        FontWeight.Bold
                    else
                        FontWeight.Normal,
                    color = if (selected)
                        Color.Black
                    else
                        Color.LightGray
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun QuizProgressBar(
    modifier: Modifier,
    current: Int,
    max: Int,
) {
    val currentPercentage by remember(current, max) {
        derivedStateOf { (current / max).toFloat() }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        LinearProgressIndicator(
            progress = currentPercentage,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = stringResource(
                R.string.progress,
                current, max
            ),
            modifier = Modifier.padding(
                horizontal = 12.dp
            )
        )
    }
}

@Composable
fun QuizQuestionHeading(
    modifier: Modifier,
    question: String,
    contentDescription: String = "",
    @DrawableRes imageRes: Int,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(22.dp))
        Image(
            modifier = Modifier.fillMaxWidth(0.8f),
            painter = painterResource(imageRes),
            contentScale = ContentScale.Crop,
            contentDescription = contentDescription
        )
    }
}