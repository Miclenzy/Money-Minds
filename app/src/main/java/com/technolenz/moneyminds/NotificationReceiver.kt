package com.technolenz.moneyminds

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Intent to open the MainActivity with the specific fragment
        val fragmentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("openFragment", "FinancialGoalsFragment") // Add a key-value pair for fragment identification
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            fragmentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(context, "mm_channel_id")
            .setSmallIcon(R.mipmap.ic_launcher) // Replace with your notification icon
            .setContentTitle("Money Minds")
            .setContentText("Don't forget to keep your eyes on your goals.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(1, notificationBuilder.build())
    }
}
