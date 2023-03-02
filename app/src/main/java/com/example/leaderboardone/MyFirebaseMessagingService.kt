package com.example.leaderboardone

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.nio.file.attribute.AclEntry.Builder

const val channelId = "NotificationChannel"
const val channelName = "com.example.leaderboardone"

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if(remoteMessage.notification != null){
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }

    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun generateNotification(title : String, message: String){
        val intent = Intent(this,Login_screen::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)


    fun getRemoteView(title: String, message: String): RemoteViews{
        val remoteView = RemoteViews("com.example.leaderboardone",R.layout.activity_notification)

        remoteView.setTextViewText(R.id.tv_notifi_title,title)
        remoteView.setTextViewText(R.id.tv_message_notify,message)
        remoteView.setImageViewResource(R.id.iv_notifi,R.drawable.download)

        return remoteView

    }

        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_MUTABLE)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.download)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())

    }

}