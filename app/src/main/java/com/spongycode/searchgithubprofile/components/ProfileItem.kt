package com.spongycode.searchgithubprofile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.spongycode.searchgithubprofile.data.model.Profile

@Composable
fun ProfileItem(profile: Profile, navController: NavController) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { navController.navigate("profile/${profile.login}") },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = profile.avatar_url,
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .size(65.dp),
            contentDescription = null
        )
        Column(Modifier.padding(start = 20.dp)) {
            if (profile.name.isNotBlank()) {
                Text(text = profile.name, fontSize = 20.sp, fontWeight = FontWeight.W600)
            }
            Text(
                text = profile.login,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
                color = Color.Gray
            )
        }
    }
}