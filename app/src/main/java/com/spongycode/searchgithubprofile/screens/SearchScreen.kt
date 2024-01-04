package com.spongycode.searchgithubprofile.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.spongycode.searchgithubprofile.MainViewModel
import com.spongycode.searchgithubprofile.R
import com.spongycode.searchgithubprofile.data.model.Profile


@Composable
fun SearchScreen(viewModel: MainViewModel, navController: NavController) {
    Column(
        Modifier
            .fillMaxHeight()
            .padding(10.dp)
            .background(colorScheme.background),
        verticalArrangement = Arrangement.Top
    ) {
        SearchBar(viewModel, navController)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(viewModel: MainViewModel, navController: NavController) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(TextFieldValue("")) }

    OutlinedTextField(
        value = text,
        onValueChange = { newText -> text = newText },
        placeholder = {
            Text(
                color = colorScheme.inversePrimary, text = "Search username..."
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.background),
        shape = RoundedCornerShape(28),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = colorScheme.primary,
            unfocusedIndicatorColor = Color.LightGray,
            focusedContainerColor = colorScheme.background,
            unfocusedContainerColor = colorScheme.background,
            cursorColor = colorScheme.primary
        ),
        maxLines = 1,
        trailingIcon = {
            Box(
                modifier = Modifier.padding(end = 10.dp), contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = "send",
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            viewModel.makeProfileQuery(text.text, true)
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        },
                    tint = colorScheme.primary
                )

            }
        },
        textStyle = TextStyle(
            fontWeight = FontWeight.W500, fontSize = 18.sp
        )
    )

    Spacer(modifier = Modifier.height(10.dp))
    val queryStateObserver = viewModel.queryState.observeAsState()
    Column(modifier = Modifier.padding(10.dp)) {
        when (queryStateObserver.value) {
            MainViewModel.QueryState.Checking -> HelperText(text = stringResource(R.string.checking))
            MainViewModel.QueryState.Error -> HelperText(text = stringResource(R.string.error))
            MainViewModel.QueryState.NotFound -> HelperText(text = stringResource(R.string.user_not_found))
            MainViewModel.QueryState.Found -> viewModel.profileResultDatabase.value?.get(text.text)
                ?.let { ProfileItem(it, navController) }

            else -> {}
        }
    }
}

@Composable
fun HelperText(text: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp),
        textAlign = TextAlign.Center,
        text = text
    )
}

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
                fontWeight = W400,
                color = Color.Gray
            )
        }
    }
}
