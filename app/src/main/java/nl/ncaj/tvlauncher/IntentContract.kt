package nl.ncaj.tvlauncher

import androidx.activity.compose.ManagedActivityResultLauncher

/**
 * Convenience function for [ManagedActivityResultLauncher.launch] without any input
 */
fun <T> ManagedActivityResultLauncher<Unit, T>.launch() = launch(Unit)
