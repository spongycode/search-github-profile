package com.spongycode.searchgithubprofile.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HelperText(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        textAlign = TextAlign.Center,
        text = text,
        style = MaterialTheme.typography.titleMedium
    )
}
