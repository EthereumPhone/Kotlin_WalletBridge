package org.ethereumphone.kotlinbridgeserver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import org.ethereumphone.kotlinbridgeserver.BridgeService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val startService = findViewById<Button>(R.id.startService)
        val stopService = findViewById<Button>(R.id.stopService)

        startService.setOnClickListener {
            val serviceIntent = Intent(this, BridgeService::class.java)
            ContextCompat.startForegroundService(this, serviceIntent)
        }
        stopService.setOnClickListener {
            val serviceIntent = Intent(this, BridgeService::class.java)
            stopService(serviceIntent)
        }
    }
}