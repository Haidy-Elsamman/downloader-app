package com.downloader_app

import android.app.NotificationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val statusNameValue = findViewById<TextView>(R.id.statusNameValue)
        val fileNameValue = findViewById<TextView>(R.id.fileNameValue)
        val animationIdLayout = findViewById<MotionLayout>(R.id.animationIdLayout)

        statusNameValue.text = intent.getStringExtra("state").toString()
        fileNameValue.text = intent.getStringExtra("body").toString()

        object : CountDownTimer(1500, 1500) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                animationIdLayout.transitionToEnd()
            }
        }.start()


        val notificationManager =
            ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
        notificationManager.cancelAll()

    }

}
