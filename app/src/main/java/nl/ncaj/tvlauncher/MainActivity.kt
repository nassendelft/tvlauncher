package nl.ncaj.tvlauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import nl.ncaj.tvlauncher.home.Home
import nl.ncaj.tvlauncher.home.HomeViewModel
import nl.ncaj.tvlauncher.quick.quickSettingsNavigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val navController = rememberNavController()

      NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier
          .fillMaxSize()
          .onKeyEvent {
            if (it.type == KeyEventType.KeyUp && it.key == Key.S) {
              navController.navigate("quick-settings")
              return@onKeyEvent true
            }
            return@onKeyEvent false
          }
      ) {
        composable(
          route = "home"
        ) {
          val homeViewModel = hiltViewModel<HomeViewModel>()
          Home(homeViewModel)
        }
        quickSettingsNavigation(
          route = "quick-settings",
          navController = navController
        )
      }
    }
  }
}
