package com.gonodono.nightwidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.widget.RemoteViews

internal val Context.nightMode: Int
    get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

internal fun updateNightWidget(
    context: Context,
    manager: AppWidgetManager,
    ids: IntArray,
    text: String
) {
    val yesNightMode = context.nightMode == Configuration.UI_MODE_NIGHT_YES
    val bgColor = if (yesNightMode) Color.BLACK else Color.WHITE
    val fgColor = if (yesNightMode) Color.WHITE else Color.BLACK

    val views = RemoteViews(context.packageName, R.layout.widget)
    views.setInt(R.id.text, "setBackgroundColor", bgColor)
    views.setTextColor(R.id.text, fgColor)
    views.setTextViewText(R.id.text, text)

    for (id in ids) manager.updateAppWidget(id, views)
}