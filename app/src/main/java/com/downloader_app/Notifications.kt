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
    lateinit var manager: NotificationManager
     var context: Context = parsedContext
    var intent: PendingIntent = myIntent
     var pendingIntent = showStatusPendingIntent
     private var pictureStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(null)
        .bigLargeIcon( BitmapFactory.decodeResource(
            parsedContext.resources,
            R.drawable.download_image
        ))


    fun getNotification(systemService: Any, title: String, text: String) {
        val builder = NotificationCompat.Builder(context, "channelId")
            .setSmallIcon(R.drawable.icon_download)
            .setContentText(text)
            .setContentTitle(title)
            .setStyle(pictureStyle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(intent)
            .addAction(
                R.drawable.icon_download,
                "show file",
                pendingIntent
            ).build()
        manager = systemService as NotificationManager
        manager.createNotificationChannel( NotificationChannel("channelId", "name", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "descriptionText"
        })
        manager.notify(5, builder)
    }


}