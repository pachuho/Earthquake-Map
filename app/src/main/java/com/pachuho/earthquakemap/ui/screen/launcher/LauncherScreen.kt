package com.pachuho.earthquakemap.ui.screen.launcher

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pachuho.earthquakemap.R
import kotlinx.coroutines.delay

@Composable
fun LauncherScreen(
    onDownloadCompleted: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    var isLottieVisible by remember { mutableStateOf(false) }
    var isTextVisible by remember { mutableStateOf(false) }
    var textIndex by remember { mutableIntStateOf(0) }

    val textList = listOf(
        "서울시 지진안전포털에서\n데이터를 받아오고 있어요",
        "과거 지진 데이터를 받아오고 있어요.\n잠시만 기다려주세요.",
        "국내 뿐만 아니라 해외 지진 기록도\n확인할 수 있어요",
        "지진 위치와 규모를 확인 해보세요.",
    )
    LaunchedEffect(composition) {
        if (composition != null) {
            isLottieVisible = true

            while (true) {
                isTextVisible = true
                delay(5000)
                isTextVisible = false
                delay(2000)
                textIndex = (textIndex + 1) % textList.size
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .align(Alignment.Center)
        ) {
            AnimatedVisibility(
                visible = isLottieVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 500))
            ) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    contentScale = ContentScale.FillHeight
                )
            }

            AnimatedVisibility(
                visible = isTextVisible,
                enter = fadeIn(animationSpec = tween(durationMillis = 500)),
                exit = fadeOut(animationSpec = tween(durationMillis = 500))
            ) {
                Text(
                    text = textList[textIndex],
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 30.sp
                )
            }
        }
    }
}