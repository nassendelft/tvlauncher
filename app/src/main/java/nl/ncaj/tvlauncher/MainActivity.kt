package nl.ncaj.tvlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import nl.ncaj.tvlauncher.home.Channels
import nl.ncaj.tvlauncher.home.Home
import nl.ncaj.tvlauncher.home.HomeViewModel

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
