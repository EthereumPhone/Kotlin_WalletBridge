package org.ethereumphone.kotlinbridgeserver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.squareup.moshi.Moshi
import org.ethereumphone.kotlinbridgeserver.BridgeServer
import org.ethereumphone.kotlinbridgeserver.MainActivity
import java.net.UnknownHostException


class BridgeService : Service() {
    lateinit var bridgeServer: BridgeServer
    override fun onCreate() {
        super.onCreate()
        try {
            var moshi: Moshi = Moshi.Builder().build()
            bridgeServer = BridgeServer(moshi)
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val input = intent.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("WalletConnect Bridge")
            .setContentText(input) //.setSmallIcon(R.drawable.)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        bridgeServer.start()
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        /**
         * Stop stuff
         */
        try {
            bridgeServer.stop()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    companion object {
        const val CHANNEL_ID = "BridgeServer"
    }
}