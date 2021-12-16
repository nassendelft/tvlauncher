package com.example.tvlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tvlauncher.home.Home
import com.example.tvlauncher.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val navController = rememberNavController()

      NavHost(navController = navController, startDestination = "home") {
        composable("home") {
          val homeViewModel = hiltViewModel<HomeViewModel>()
          Home(homeViewModel)
        }
      }
    }
  }

  override fun onBackPressed() {
    // no-op - for now, to prevent closing this activity
  }
}
