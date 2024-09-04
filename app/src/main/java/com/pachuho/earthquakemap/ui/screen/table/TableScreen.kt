package com.pachuho.earthquakemap.ui.screen.table

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
}

@Composable
fun TableScreen() {
    Box(modifier = Modifier.fillMaxSize()) {

    }
}

@Composable
@Preview(showBackground = true)
fun TableScreenPreview() {
    TableScreen()
}