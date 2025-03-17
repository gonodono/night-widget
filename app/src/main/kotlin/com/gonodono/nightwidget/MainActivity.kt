package com.gonodono.nightwidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gonodono.nightwidget.ui.NightWidgetTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { NightWidgetTheme { Content() } }
    }
}

@Composable
private fun Activity.Content() = Surface(color = Color.Transparent) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement =
            Arrangement.spacedBy(25.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (canPin()) {
            OutlinedButton({ tryPin(ContentUriTriggerWidget::class.java) }) {
                Text("Pin a Content URI trigger Widget")
            }
            OutlinedButton({ tryPin(OptionsChangeWidget::class.java) }) {
                Text("Pin an Options change Widget")
            }
        } else {
            Text("Can't pin. Place Widgets manually.")
        }

        OutlinedButton(::finish) { Text("Close") }
    }
}

// This app has its minSdk at 29, since that's when dark mode was added, but
// yours may have it lower, so I left the necessary checks below in comments.

private fun Context.canPin(): Boolean = /* Build.VERSION.SDK_INT >= 26 && */
    AppWidgetManager.getInstance(this).isRequestPinAppWidgetSupported

private fun Context.tryPin(widget: Class<*>) {
//    if (Build.VERSION.SDK_INT < 26) return
    val name = ComponentName(this, widget)
    AppWidgetManager.getInstance(this).requestPinAppWidget(name, null, null)
}