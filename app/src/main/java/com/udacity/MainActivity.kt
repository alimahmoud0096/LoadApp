package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.utils.createChannel
import com.udacity.utils.showNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    //    private lateinit var notificationManager: NotificationManager
//    private lateinit var pendingIntent: PendingIntent
//    private lateinit var action: NotificationCompat.Action
    private lateinit var url: String
    private var fileName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        createChannel(
            getString(R.string.notification_Channel),
            getString(R.string.notification_description),
            applicationContext
        )
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        init()
        custom_button.setOnClickListener {
            if (!::url.isInitialized) {
                Toast.makeText(this, getText(R.string.pleaseSelectOption), Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            download()
        }
    }

    private fun init() {

        rGroup.setOnCheckedChangeListener { group, checkedId ->
            run {
                url = when (checkedId) {
                    R.id.rGlide -> {
                        "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
                    }
                    R.id.rLoading -> {
                        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
                    }
                    else -> {
                        "https://github.com/square/retrofit/archive/refs/heads/master.zip"
                    }
                }
            }
        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            custom_button.changeBtnState(ButtonState.Completed)
            if (downloadID == id) {
                val query = DownloadManager.Query()
                val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val cursor = downloadManager.query(query)
                if (cursor.moveToFirst()) {
                    val success =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                    val isSuccess = success == DownloadManager.STATUS_SUCCESSFUL
                    val fileName =
                        cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                    var status = when (isSuccess) {
                        true -> getString(R.string.Success)
                        else -> getString(R.string.Fail)
                    }
                    showNotification(applicationContext, id.toInt(), fileName, status)
                }
            }


        }
    }

    private fun download() {
        custom_button.changeBtnState(ButtonState.Loading)
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"

    }

}
