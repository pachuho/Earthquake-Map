//package com.pachuho.earthquakemap.ui.screen.launcher
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.airbnb.lottie.compose.LottieAnimation
//import com.airbnb.lottie.compose.LottieCompositionSpec
//import com.airbnb.lottie.compose.LottieConstants
//import com.airbnb.lottie.compose.rememberLottieComposition
//import com.pachuho.earthquakemap.R
//import com.pachuho.earthquakemap.ui.util.UiState
//import com.pachuho.earthquakemap.ui.util.showToast
//import com.pachuho.earthquakemap.ui.util.successOrNull
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import timber.log.Timber
//
//@Composable
//fun LauncherRoute(
//    viewModel: LauncherViewModel = hiltViewModel(),
//    onDownloadCompleted: () -> Unit
//) {
//    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
//    val context = LocalContext.current
//    val ioScope = CoroutineScope(Dispatchers.IO)
//
//    LaunchedEffect(true) {
//        viewModel.errorFlow.collectLatest {
//            Timber.d(it)
//            context.showToast(context.getString(R.string.error_download))
//        }
//    }
//
//
//    Box {
//        LauncherScreen()
//
//        uiState.value.successOrNull()?.let {
//            LaunchedEffect(uiState.value) {
//                delay(5_000)
//                withContext(Dispatchers.Main) {
//                    Timber.d("success!!")
//                    onDownloadCompleted.invoke()
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun LauncherScreen() {
//    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
//    var isLottieVisible by remember { mutableStateOf(false) }
//
//    LaunchedEffect(composition) {
//        if (composition != null) {
//            isLottieVisible = true
//        }
//    }
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier
//                .fillMaxSize()
//                .align(Alignment.Center)
//        ) {
//            AnimatedVisibility(
//                visible = isLottieVisible,
//                enter = fadeIn(animationSpec = tween(durationMillis = 500))
//            ) {
//                LottieAnimation(
//                    composition = composition,
//                    iterations = LottieConstants.IterateForever,
//                    contentScale = ContentScale.FillHeight
//                )
//            }
//
//            AnimatedVisibility(
//                visible = isLottieVisible,
//                enter = fadeIn(animationSpec = tween(durationMillis = 500)),
//                exit = fadeOut(animationSpec = tween(durationMillis = 500))
//            ) {
//                Text(
//                    text = "서울시 지진안전포털에서\n데이터를 받아오고 있어요",
//                    fontSize = 14.sp,
//                    color = Color.DarkGray,
//                    textAlign = TextAlign.Center,
//                    fontFamily = FontFamily.Monospace,
//                    lineHeight = 30.sp
//                )
//            }
//        }
//    }
//}