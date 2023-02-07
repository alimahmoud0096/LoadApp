package com.udacity

import android.app.NotificationManager
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.utils.FILE_NAME
import com.udacity.utils.NOTIFICATION_ID
import com.udacity.utils.STATUS
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(toolbar)
        init()
    }

    private fun init() {
        if (intent.hasExtra(FILE_NAME)) {
            var notifID = intent.getIntExtra(NOTIFICATION_ID,-1)
            var downloadStatus = intent.getStringExtra(STATUS)
            with(binding) {
                fileName.text = intent.getStringExtra(FILE_NAME)
                status.text = downloadStatus
                when (downloadStatus) {
                    "Fail" -> {
                        status.setTextColor(getColor(R.color.red))
                    }
                }
            }
            if (notifID!=-1) {
                cancelNotification(notifID)
            }
        }

        ok.setOnClickListener {
            onBackPressed()
        }
    }

    private fun cancelNotification(NOTIFICATION_ID: Int) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancel(NOTIFICATION_ID) //if you saved it or cancelAll()
    }

}
