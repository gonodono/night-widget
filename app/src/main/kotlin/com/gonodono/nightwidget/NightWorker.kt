package com.gonodono.nightwidget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class NightWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context.applicationContext, params) {

    override suspend fun doWork(): Result {
        val context = applicationContext
        val manager = AppWidgetManager.getInstance(context)
        val name = ComponentName(context, NightWidget::class.java)
        val ids = manager.getAppWidgetIds(name)
        NightWidget.update(context, manager, ids)
        enqueue(context)
        return Result.success()
    }

    companion object {

        private const val TAG = "NightWorker"

        private val UI_NIGHT_MODE_URI = Settings.Secure.CONTENT_URI
            .buildUpon().appendPath("ui_night_mode").build()

        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .addContentUriTrigger(UI_NIGHT_MODE_URI, false)
                .setTriggerContentMaxDelay(0L, TimeUnit.SECONDS)
                .build()
            val request = OneTimeWorkRequestBuilder<NightWorker>()
                .setConstraints(constraints)
                .addTag(TAG)
                .build()
            WorkManager.getInstance(context)
                .beginUniqueWork("Update", ExistingWorkPolicy.APPEND, request)
                .enqueue()
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(TAG)
        }
    }
}