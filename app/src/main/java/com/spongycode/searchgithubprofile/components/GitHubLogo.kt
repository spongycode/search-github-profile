package com.spongycode.searchgithubprofile.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.spongycode.searchgithubprofile.R

@Composable
fun GitHubLogo() {
    Icon(
        modifier = Modifier
            .size(100.dp)
            .padding(30.dp),
        painter = painterResource(id = R.drawable.github_mark),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary
    )
}