package com.spongycode.searchgithubprofile.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.spongycode.searchgithubprofile.MainViewModel

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
            ProfileComponent(viewModel, username, navController)
        }
    }
}

@Composable
fun ProfileComponent(viewModel: MainViewModel, username: String, navController: NavController) {
    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val count = viewModel.profileResultDatabase.observeAsState()

        AsyncImage(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .size(100.dp),
            model = count.value?.get(username)?.avatar_url,
            contentDescription = null
        )
        if (!(count.value?.get(username)?.name.isNullOrBlank())) {
            Text(
                text = count.value?.get(username)?.name!!,
                fontSize = 25.sp,
                fontWeight = FontWeight.W600
            )
        }
        Text(
            text = username,
            fontSize = 20.sp,
            fontWeight = FontWeight.W400,
            color = Color.Gray
        )
        if (!(count.value?.get(username)?.bio.isNullOrBlank())) {
            Text(
                text = count.value?.get(username)?.bio.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.W400
            )
        }
        Row {
            Box(modifier = Modifier.clickable {
                navController.navigate("profileList/${username}.followers")
            }) {
                Row {
                    Text(
                        text = count.value?.get(username)?.followers.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600
                    )
                    Text(text = " followers · ", fontSize = 16.sp, fontWeight = FontWeight.W400)
                }
            }
            Box(modifier = Modifier.clickable {
                navController.navigate("profileList/${username}.following")
            }) {
                Row {
                    Text(
                        text = count.value?.get(username)?.following.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W600
                    )
                    Text(text = " following", fontSize = 16.sp, fontWeight = FontWeight.W400)
                }
            }
        }
    }
}

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
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}