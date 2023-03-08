package com.downloader_app


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

class Notifications(
    parsedContext: Context,
    myIntent: PendingIntent,
    showStatusPendingIntent: PendingIntent
) {
    private lateinit var manager: NotificationManager
    private var context: Context = parsedContext
    private val intent: PendingIntent = myIntent
    private val ID = "channelId"
    private val pendingIntent = showStatusPendingIntent


    private val channel =
        NotificationChannel(ID, "name", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "descriptionText"
        }


    private val icAssistant = BitmapFactory.decodeResource(
        parsedContext.resources,
        R.drawable.download_image
    )
    private val pictureStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(null)
        .bigLargeIcon(icAssistant)


    fun getNotification(systemService: Any, title: String, text: String) {
        val title: String = "Show status"
        println(title)
        val builder = NotificationCompat.Builder(context, ID)
            .setSmallIcon(R.drawable.ic_assistant)
            .setContentText(text)
            .setContentTitle(title)
            .setStyle(pictureStyle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setLargeIcon(icAssistant)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_assistant,
                title,
                pendingIntent
            )
        manager = systemService as NotificationManager
        manager.createNotificationChannel(channel)
        manager.notify(4, builder.build())
    }


}