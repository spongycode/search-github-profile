package com.spongycode.searchgithubprofile.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.spongycode.searchgithubprofile.MainViewModel
import com.spongycode.searchgithubprofile.components.ProfileDetails
import com.spongycode.searchgithubprofile.components.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    navController: NavController,
    username: String
) {
    viewModel.makeProfileQuery(username)
    Scaffold(topBar = {
        TopBar(navController, username)
    }) {
        Column(
            Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileDetails(viewModel, username, navController)
        }
    }
}