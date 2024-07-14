package com.gonodono.nightwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.widget.RemoteViews

class NightWidget : AppWidgetProvider() {

    override fun onEnabled(context: Context) {
        NightWorker.enqueue(context)
    }

    override fun onDisabled(context: Context) {
        NightWorker.cancel(context)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        update(context, appWidgetManager, appWidgetIds)
    }

    companion object {

        fun update(
            context: Context,
            manager: AppWidgetManager,
            ids: IntArray
        ) {
            val uiMode = context.resources.configuration.uiMode
            val nightMode = uiMode and Configuration.UI_MODE_NIGHT_MASK
            val yesNightMode = nightMode == Configuration.UI_MODE_NIGHT_YES

            val views = RemoteViews(context.packageName, R.layout.widget)
            val bgColor = if (yesNightMode) Color.BLACK else Color.WHITE
            val fgColor = if (yesNightMode) Color.WHITE else Color.BLACK
            val text = if (yesNightMode) "Night" else "Day"
            views.setInt(R.id.text, "setBackgroundColor", bgColor)
            views.setTextColor(R.id.text, fgColor)
            views.setTextViewText(R.id.text, text)

            for (id in ids) manager.updateAppWidget(id, views)
        }
    }
}