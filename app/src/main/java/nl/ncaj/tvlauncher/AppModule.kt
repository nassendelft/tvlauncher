package nl.ncaj.tvlauncher

import android.app.ActivityManager
import android.app.DownloadManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log.DEBUG
import androidx.core.content.getSystemService
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
  fun provideContentResolver(
    @ApplicationContext context: Context
  ) = context.contentResolver

  @Provides
  @Singleton
  fun provideOkHttp() = OkHttpClient()

  @Provides
  @Singleton
  fun provideJson() = Json { ignoreUnknownKeys = true }

  @Provides
  @Singleton
  fun provideActivityManager(
    @ApplicationContext context: Context
  ) : ActivityManager = context.getSystemService()
    ?: error("Could not find instance of ActivityManager")
}
