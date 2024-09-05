package com.pachuho.earthquakemap.ui.screen.table

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TableRoute(
    viewModel: TableViewModel = hiltViewModel(),
    onSnackBar: (message: String) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val settingFlow = viewModel.settingsFlow.collectAsStateWithLifecycle().value

    TableScreen()
}

@Composable
fun TableScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "테이블")
    }
}

@Composable
@Preview(showBackground = true)
fun TableScreenPreview() {
    TableScreen()
}