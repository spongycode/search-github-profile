package com.spongycode.searchgithubprofile.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.spongycode.searchgithubprofile.MainViewModel
import com.spongycode.searchgithubprofile.util.shimmerEffect

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
            if (!(observer.value?.get(username)?.followers_list.isNullOrEmpty())) {
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

@Composable
fun ListItemSkeleton() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(65.dp)
                .clip(RoundedCornerShape(100.dp))
                .height(20.dp)
                .shimmerEffect()
        )
        Column(Modifier.padding(start = 20.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(25.dp)
                    .shimmerEffect()
            )
        }
    }
}