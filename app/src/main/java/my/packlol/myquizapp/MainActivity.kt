@file:OptIn(ExperimentalAnimationApi::class)

package my.packlol.myquizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch
import my.packlol.myquizapp.navigation.AppComposeNavigator
import my.packlol.myquizapp.navigation.QuizAppComposeNavigator
import my.packlol.myquizapp.navigation.QuizAppScreens
import my.packlol.myquizapp.screens.HomeScreen
import my.packlol.myquizapp.screens.QuizScreen
import my.packlol.myquizapp.screens.ResultScreen
import my.packlol.myquizapp.theme.QuizAppTheme


class MainActivity : ComponentActivity() {

    private val appNavigator = QuizAppComposeNavigator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            QuizAppTheme {
                NavigationHost(
                    appComposeNavigator = appNavigator
                )
            }
        }
    }
}

@Composable
fun NavigationHost(
    navController: NavHostController = rememberAnimatedNavController(),
    appComposeNavigator: AppComposeNavigator
) {

    LaunchedEffect(Unit) {
        launch {
            appComposeNavigator.handleNavigationCommands(navController)
        }
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = QuizAppScreens.Home.name
    ) {

        composableWithSlideInOut(
            route = QuizAppScreens.Home.name,
            args = QuizAppScreens.Home.navArguments
        ) {
            HomeScreen(appComposeNavigator)
        }

        composableWithSlideInOut(
            route = QuizAppScreens.Quiz.name,
            args = QuizAppScreens.Quiz.navArguments
        ) { backStackEntry ->
            QuizScreen(
                navigator = appComposeNavigator,
                username = backStackEntry
                    .arguments?.getString(QuizAppScreens.Quiz.KEY_USERNAME) ?: ""
            )
        }
        composableWithSlideInOut(
            route = QuizAppScreens.Result.name,
            args = QuizAppScreens.Result.navArguments
        ) { backStackEntry ->
            ResultScreen(
                navigator = appComposeNavigator,
                username = backStackEntry.arguments
                    ?.getString(QuizAppScreens.Result.KEY_USERNAME) ?: "",
                questions = backStackEntry.arguments
                    ?.getInt(QuizAppScreens.Result.KEY_AMOUNT) ?: 0,
                correct = backStackEntry.arguments
                    ?.getInt(QuizAppScreens.Result.KEY_CORRECT) ?: 0
            )
        }
    }
}

fun NavGraphBuilder.composableWithSlideInOut(
    route: String,
    args:  List<NamedNavArgument> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = args,
        enterTransition = {
            slideInHorizontally { it }
        },
        exitTransition = {
            slideOutHorizontally { -it }
        },
        popEnterTransition = {slideInHorizontally { -it } },
        popExitTransition = { slideOutHorizontally { it } }
    ) {
        content(it)
    }
}
