package my.packlol.myquizapp.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import my.packlol.myquizapp.R

@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(R.drawable.ic_bg),
            contentScale = ContentScale.FillBounds,
            contentDescription = "background"
        )
        content()
    }
}