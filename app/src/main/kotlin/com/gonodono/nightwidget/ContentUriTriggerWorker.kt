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

class ContentUriTriggerWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context.applicationContext, params) {

    override suspend fun doWork(): Result {
        val context = applicationContext
        val manager = AppWidgetManager.getInstance(context)
        val name = ComponentName(context, ContentUriTriggerWidget::class.java)
        val ids = manager.getAppWidgetIds(name)
        updateNightWidget(context, manager, ids, "URI")

        enqueue(context)

        return Result.success()
    }

    companion object {

        private const val WORK_NAME = "nightWidgetUpdate"

        private val UI_NIGHT_MODE_URI = Settings.Secure.CONTENT_URI
            .buildUpon().appendPath("ui_night_mode").build()

        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .addContentUriTrigger(UI_NIGHT_MODE_URI, false)
                .setTriggerContentMaxDelay(0L, TimeUnit.SECONDS)
                .build()
            val request = OneTimeWorkRequestBuilder<ContentUriTriggerWorker>()
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context)
                .beginUniqueWork(WORK_NAME, ExistingWorkPolicy.APPEND, request)
                .enqueue()
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}