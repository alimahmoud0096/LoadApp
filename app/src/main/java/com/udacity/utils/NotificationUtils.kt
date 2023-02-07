package com.udacity.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.DetailActivity
import com.udacity.R

const val FILE_NAME = "fileName"
const val STATUS = "status"
const val NOTIFICATION_ID = "NOTIFICATION_ID"


fun NotificationManager.sendNotification(applicationContext: Context,downloadID:Int,fileName:String,status: String) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra(FILE_NAME,fileName)
    contentIntent.putExtra(STATUS,status)
    contentIntent.putExtra(NOTIFICATION_ID,downloadID)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        downloadID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_Channel)
    ) .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setContentText(fileName)
        .addAction(
            R.drawable.abc_vector_test,
            applicationContext.getString(R.string.chech_status),
            contentPendingIntent
        )
     notify(downloadID, builder.build())



}
fun showNotification(app:Context,downloadID:Int,fileName:String,status: String) {
    val notificationManager = ContextCompat.getSystemService(
        app,
        NotificationManager::class.java
    ) as NotificationManager
    notificationManager.sendNotification(app,downloadID,fileName,status)
}

fun createChannel(channelId: String, channelName: String,applicationContext: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_LOW
        )

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "Downloads Alerts"
        val notificationManager = applicationContext.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(notificationChannel)

    }
}

