package com.spongycode.searchgithubprofile.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.spongycode.searchgithubprofile.MainViewModel
import com.spongycode.searchgithubprofile.components.ListItemSkeleton
import com.spongycode.searchgithubprofile.components.ProfileItem
import com.spongycode.searchgithubprofile.components.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileListScreen(viewModel: MainViewModel, endpoint: String, navController: NavController) {
    viewModel.populateFollowersList(endpoint)
    val observer = viewModel.profileResultDatabase.observeAsState()
    val username = endpoint.split(".").firstOrNull().toString()
    val title = endpoint.replace(".", "'s ")
    Scaffold(topBar = {
        TopBar(navController, title)
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = it.calculateTopPadding(), start = 5.dp, end = 5.dp)
        ) {
            if (observer.value?.get(username)?.followers_list != null) {
                items(observer.value?.get(username)?.followers_list!!) { profile ->
                    ProfileItem(profile, navController)
                }
            } else {
                repeat(10) {
                    item {
                        ListItemSkeleton()
                    }
                }
            }
        }
    }
}