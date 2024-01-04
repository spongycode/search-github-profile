package com.spongycode.searchgithubprofile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.spongycode.searchgithubprofile.MainViewModel
import com.spongycode.searchgithubprofile.util.shimmerEffect

@Composable
fun ProfileDetails(viewModel: MainViewModel, username: String, navController: NavController) {
    Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val profileObserver = viewModel.profileResultDatabase.observeAsState()
        if (profileObserver.value?.get(username)?.avatar_url.toString() == "null") {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .size(100.dp)
                    .shimmerEffect()
            )
        } else {
            AsyncImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .size(100.dp),
                model = profileObserver.value?.get(username)?.avatar_url,
                contentDescription = null
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        if (profileObserver.value?.get(username)?.name.toString() == "null") {
            Box(
                modifier = Modifier
                    .height(25.dp)
                    .width(200.dp)
                    .shimmerEffect()
            )
        } else {
            Text(
                text = profileObserver.value?.get(username)?.name!!,
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
        if (!(profileObserver.value?.get(username)?.bio.isNullOrBlank())) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                textAlign = TextAlign.Center,
                text = profileObserver.value?.get(username)?.bio.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.W400
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (profileObserver.value?.get(username)?.followers.toString() != "null" &&
            profileObserver.value?.get(username)?.following.toString() != "null"
        ) {
            Row {
                Box(modifier = Modifier.clickable {
                    navController.navigate("profileList/${username}.followers")
                }) {
                    Row {
                        Text(
                            text = profileObserver.value?.get(username)?.followers.toString(),
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
                            text = profileObserver.value?.get(username)?.following.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W600
                        )
                        Text(text = " following", fontSize = 16.sp, fontWeight = FontWeight.W400)
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .shimmerEffect()
            )
        }
    }
}