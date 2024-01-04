package com.spongycode.searchgithubprofile.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.spongycode.searchgithubprofile.MainViewModel
import com.spongycode.searchgithubprofile.R


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(viewModel: MainViewModel, navController: NavController) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val queryStateObserver = viewModel.queryState.observeAsState()
    Column {
        OutlinedTextField(
            value = text,
            onValueChange = { newText -> text = newText },
            placeholder = {
                Text(
                    color = MaterialTheme.colorScheme.inversePrimary, text = "Search username..."
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(28),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.LightGray,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            trailingIcon = {
                Box(
                    modifier = Modifier.padding(end = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (text.text.isNotBlank()) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "clear text",
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable {
                                        text = TextFieldValue("")
                                    },
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                        if (queryStateObserver.value == MainViewModel.QueryState.Checking) {
                            val strokeWidth = 2.dp
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .drawBehind {
                                        drawCircle(
                                            Color.Black,
                                            radius = size.width / 2 - strokeWidth.toPx() / 2,
                                            style = Stroke(strokeWidth.toPx())
                                        )
                                    }
                                    .size(25.dp),
                                color = Color.LightGray,
                                strokeWidth = strokeWidth
                            )
                        } else {
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
                                tint = MaterialTheme.colorScheme.primary
                            )

                        }
                    }
                }
            },
            textStyle = TextStyle(
                fontWeight = FontWeight.W500, fontSize = 18.sp
            )
        )

        Spacer(modifier = Modifier.height(10.dp))
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