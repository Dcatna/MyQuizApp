package my.packlol.myquizapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import my.packlol.myquizapp.R
import my.packlol.myquizapp.composables.GradientBackground
import my.packlol.myquizapp.navigation.AppComposeNavigator
import my.packlol.myquizapp.navigation.QuizAppScreens

@Composable
fun ResultScreen(
    navigator: AppComposeNavigator,
    questions: Int,
    correct: Int,
    username: String,
) {
    GradientBackground {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.result),
                style = MaterialTheme.typography.headlineLarge,
            )
            Image(
                painter = painterResource(R.drawable.ic_trophy),
                contentDescription = "trophy",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )
            Text(
                stringResource(R.string.congrats, username),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Text(
                stringResource(R.string.score, correct, questions),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Button(
                onClick = {
                    navigator.navigate(
                        QuizAppScreens.Home.route
                    )
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    stringResource(R.string.finish)
                )
            }
        }
    }
}