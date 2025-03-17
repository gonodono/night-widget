package com.gonodono.nightwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.core.content.edit

class OptionsChangeWidget : AppWidgetProvider() {

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        val preferences =
            context.getSharedPreferences("widgets", Context.MODE_PRIVATE)

        // onAppWidgetOptionsChanged runs per instance, so we must track this
        // separately for each one, otherwise only the first will ever update.
        val key = "previous_night_mode:$appWidgetId"

        val currentNightMode = context.nightMode
        val previousNightMode =
            preferences.getInt(key, Configuration.UI_MODE_NIGHT_UNDEFINED)
        if (currentNightMode == previousNightMode) return

        preferences.edit { putInt(key, currentNightMode) }
        onUpdate(context, appWidgetManager, intArrayOf(appWidgetId))
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) =
        updateNightWidget(context, appWidgetManager, appWidgetIds, "Options")
}