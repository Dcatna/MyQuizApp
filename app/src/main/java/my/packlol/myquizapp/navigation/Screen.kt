package my.packlol.myquizapp.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class QuizAppScreens(
    val route: String,
    val index: Int? = null,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    val name: String = route.appendArguments(navArguments)

    // home screen
    object Home : QuizAppScreens("home")

    // message screen
    object Quiz : QuizAppScreens(
        route = "quiz",
        navArguments = listOf(
            navArgument("username") {
                type = NavType.StringType
                nullable = false
            },
        )
    ) {
        const val KEY_USERNAME = "username"
        fun createRoute(username: String) =
            name.replace("{${navArguments.first().name}}", username)
    }

    // call info screen
    object Result : QuizAppScreens(
        route = "result",
        navArguments = listOf(
            navArgument("username") {
                type = NavType.StringType
                nullable = false
            },
            navArgument("correct") {
                type = NavType.IntType
                nullable = false
            },
            navArgument("amount") {
                type = NavType.IntType
                nullable = false
            }
        )
    ) {
        const val KEY_USERNAME = "username"
        const val KEY_CORRECT = "correct"
        const val KEY_AMOUNT = "amount"
        fun createRoute(username: String, correct: Int, amount: Int) =
            name.replace("{${navArguments.first().name}}", username)
                .replace("{${navArguments[1].name}}", correct.toString())
                .replace("{${navArguments[2].name}}", amount.toString())
    }
}

private fun String.appendArguments(navArguments: List<NamedNavArgument>): String {
    val mandatoryArguments = navArguments.filter { it.argument.defaultValue == null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "/", prefix = "/") { "{${it.name}}" }
        .orEmpty()
    val optionalArguments = navArguments.filter { it.argument.defaultValue != null }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "&", prefix = "?") { "${it.name}={${it.name}}" }
        .orEmpty()
    return "$this$mandatoryArguments$optionalArguments"
}