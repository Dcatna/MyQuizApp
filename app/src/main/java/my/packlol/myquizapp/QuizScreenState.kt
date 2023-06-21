package my.packlol.myquizapp

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import my.packlol.myquizapp.navigation.AppComposeNavigator
import my.packlol.myquizapp.navigation.QuizAppScreens

class QuizScreenState(
    private val navigator: AppComposeNavigator,
    private val username: String,
    private val scope: CoroutineScope
) {

    val questions =  Constants.questionsList

    val snackbarHostState = SnackbarHostState()

    var questionIdx by mutableIntStateOf(0)

    val currentQuestion by derivedStateOf { questions[questionIdx] }

    var correctAnswers by mutableIntStateOf(0)

    val options by derivedStateOf {
        with(currentQuestion) {
            listOf(
                optionOne, optionTwo, optionThree, optionFour
            )
        }
    }

    var selectedIdx by mutableStateOf<Int?>(null)

    val isLast by derivedStateOf { questionIdx == questions.lastIndex }

    var showCorrectAnswer by mutableStateOf(false)

    fun showCorrectAnswer() {
        if (selectedIdx != null) {
            if (selectedIdx == currentQuestion.correctAnswer - 1) {
                correctAnswers += 1
            }
            showCorrectAnswer = true
        } else {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "You must select an answer",
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )
            }
        }
    }

    fun goToNextQuestion() {
        selectedIdx = null
        showCorrectAnswer = false
        if (questionIdx < questions.lastIndex) {
            questionIdx += 1
        } else {
            navigator.navigate(
                QuizAppScreens.Result.createRoute(
                    username = username,
                    correct = correctAnswers,
                    amount = questions.size
                )
            )
        }
    }
}