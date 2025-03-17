package com.gonodono.nightwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context

class ContentUriTriggerWidget : AppWidgetProvider() {

    override fun onEnabled(context: Context) =
        ContentUriTriggerWorker.enqueue(context)

    override fun onDisabled(context: Context) =
        ContentUriTriggerWorker.cancel(context)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) =
        updateNightWidget(context, appWidgetManager, appWidgetIds, "URI")
}