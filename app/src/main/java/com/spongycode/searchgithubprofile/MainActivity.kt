package com.spongycode.searchgithubprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spongycode.searchgithubprofile.screens.ProfileListScreen
import com.spongycode.searchgithubprofile.screens.ProfileScreen
import com.spongycode.searchgithubprofile.screens.SearchScreen
import com.spongycode.searchgithubprofile.ui.theme.SearchGithubProfileTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchGithubProfileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = hiltViewModel<MainViewModel>()
                    App(viewModel)
                }
            }
        }
    }
}


@Composable
fun App(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = "search"
    ) {
        composable(route = "search") {
            SearchScreen(viewModel = viewModel, navController = navController)
        }
        composable(
            route = "profile/{username}",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(300)
                )
            },
            popEnterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(300)
                )
            }
        ) {
            val username = it.arguments?.getString("username")
            ProfileScreen(viewModel, navController, username!!)
        }
        composable(
            route = "profileList/{endpoint}",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(300)
                )
            },
            popEnterTransition = {
                EnterTransition.None
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(300)
                )
            }
        ) {
            val username = it.arguments?.getString("endpoint")
            ProfileListScreen(viewModel, username!!, navController)
        }
    }
}