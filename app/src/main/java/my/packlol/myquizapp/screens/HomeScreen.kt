@file:OptIn(ExperimentalMaterial3Api::class)

package my.packlol.myquizapp.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import my.packlol.myquizapp.R
import my.packlol.myquizapp.composables.GradientBackground
import my.packlol.myquizapp.navigation.AppComposeNavigator
import my.packlol.myquizapp.navigation.QuizAppScreens


@Composable
fun HomeScreen(
    navigator: AppComposeNavigator
) {

    var username by rememberSaveable {
        mutableStateOf("")
    }
    GradientBackground(
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.8f),
        ) {
            Text(
                stringResource(R.string.welcome_title),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(
                        vertical = 12.dp
                    )
            )
            Text(
                stringResource(R.string.welcome_heading),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.align(CenterHorizontally),
                color = Color.White

            )
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                },
                label = {
                    Text(
                        stringResource(R.string.username_label)
                    )
                },
                placeholder = {
                    Text(
                        stringResource(R.string.username_hint)
                    )
                },
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(
                        vertical = 12.dp
                    ),
            )
            Button(
                enabled = username.isNotBlank(),
                onClick = {
                    navigator.navigate(
                        QuizAppScreens.Quiz.createRoute(username)
                    )
                },
                modifier = Modifier
                    .align(CenterHorizontally)
                    .fillMaxWidth(0.8f)
                    .padding(
                        vertical = 12.dp
                    ),
            ) {
                Text(
                    stringResource(R.string.start_quiz_btn)
                )
            }
        }
    }
}
