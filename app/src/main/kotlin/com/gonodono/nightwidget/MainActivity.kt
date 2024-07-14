package com.gonodono.nightwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Text(stringResource(R.string.app_name), fontSize = 36.sp)
                Spacer(Modifier.size(20.dp))
                when {
                    canPin() -> Button(::tryPin) { Text("Pin an App Widget") }
                    else -> Text("Can't pin. Place the App Widget manually.")
                }
                Spacer(Modifier.size(20.dp))
                Button(::finish) { Text("Close") }
            }
        }
    }

    private fun canPin(): Boolean = Build.VERSION.SDK_INT >= 26 &&
            AppWidgetManager.getInstance(this).isRequestPinAppWidgetSupported

    private fun tryPin() {
        if (Build.VERSION.SDK_INT >= 26) {
            AppWidgetManager.getInstance(this).requestPinAppWidget(
                ComponentName(this, NightWidget::class.java),
                null,
                createPendingResult(0, Intent(), PendingIntent.FLAG_IMMUTABLE)
            )
        }
    }

    @Suppress("DEPRECATION", "OVERRIDE_DEPRECATION")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        finish()
    }
}