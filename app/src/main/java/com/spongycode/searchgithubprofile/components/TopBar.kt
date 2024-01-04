package com.spongycode.searchgithubprofile.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@ExperimentalMaterial3Api
@Composable
fun TopBar(navController: NavController, title: String) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                color = MaterialTheme.colorScheme.primary,
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.W500
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navController.navigateUp() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(26.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}
