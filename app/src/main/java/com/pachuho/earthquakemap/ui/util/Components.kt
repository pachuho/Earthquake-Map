package com.pachuho.earthquakemap.ui.util

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SpacerSmall() {
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun SpacerMedium() {
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SpacerLarge() {
    Spacer(modifier = Modifier.height(64.dp))
}