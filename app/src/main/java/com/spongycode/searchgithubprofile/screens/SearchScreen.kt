package com.spongycode.searchgithubprofile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.spongycode.searchgithubprofile.MainViewModel
import com.spongycode.searchgithubprofile.components.GitHubLogo
import com.spongycode.searchgithubprofile.components.SearchBar

@Composable
fun SearchScreen(viewModel: MainViewModel, navController: NavController) {
    Column(
        Modifier
            .fillMaxHeight()
            .padding(10.dp)
            .background(colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(viewModel, navController)
        GitHubLogo()
    }
}
