package com.example.tvlauncher

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.util.Log.DEBUG
import androidx.core.content.getSystemService
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import com.example.tvlauncher.updater.AppUpdateWork
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun provideWorkerManagerConfiguration(workerFactory: HiltWorkerFactory) = Configuration.Builder()
    .setMinimumLoggingLevel(DEBUG)
    .setWorkerFactory(workerFactory)
    .build()

  @Provides
  fun provideDownloadManager(
    @ApplicationContext context: Context
  ) = context.getSystemService<DownloadManager>()
    ?: error("Could not find instance of DownloadManager")

  @Provides
  fun providePackageManager(
    @ApplicationContext context: Context
  ): PackageManager = context.packageManager

  @Provides
  fun provideWorkManager(
    @ApplicationContext context: Context
  ) = WorkManager.getInstance(context)

  @Provides
  @Singleton
  fun provideOkHttp() = OkHttpClient()

  @Provides
  @Singleton
  fun provideJson() = Json { ignoreUnknownKeys = true }

}
