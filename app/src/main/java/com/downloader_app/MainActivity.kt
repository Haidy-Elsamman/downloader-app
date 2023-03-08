package com.downloader_app

import android.Manifest
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import com.downloader_app.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*


class MainActivity : AppCompatActivity() {
    private var downloadID: Long = 0
    private var state = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var hasNotificationPermissionGranted = false

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(toolbar)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1
        )
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        custom_button.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    it.context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Snackbar.make(
                    it,
                    "please go to settings and allow notification permission",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            val radioId = binding.root.radioGroup.checkedRadioButtonId
            if (radioId > 0) {
                binding.root.textView.text = "Loading"
                val animator = custom_button.animateLoadingLayout(binding.root)
                custom_button.moveImageInCircle()
                download()
                animator!!.doOnEnd {
                    binding.root.textView.text = "Download"
                    val myIntent = Intent(this, DetailActivity::class.java)

                    var body: String = " "
                    when (radioId) {
                        binding.root.glide_button.id -> body =
                            resources.getString(R.string.glide_image_loading_library_by_bumptech)
                        binding.root.loadapp_button.id -> body =
                            resources.getString(R.string.loadapp_current_repository_by_udacity)
                        binding.root.retrofit_button.id -> body =
                            resources.getString(R.string.retrofit_type_safe_http_client_for_android)
                    }
                    myIntent.putExtra("body", body)
                    myIntent.putExtra("state", state.toString())
                    val showStatusPendingIntent: PendingIntent = PendingIntent.getActivity(
                        applicationContext,
                        1,
                        myIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    val pendingIntent: PendingIntent = PendingIntent.getActivity(
                        applicationContext,
                        1,
                        Intent(),
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    Notifications(applicationContext, pendingIntent, showStatusPendingIntent)
                        .getNotification(
                            getSystemService(Context.NOTIFICATION_SERVICE),
                            "Download",
                            body
                        )
                }
            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    "Please select one of buttons",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        binding.root.radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                binding.root.glide_button.id -> URL = "https://github.com/bumptech/glide"
                binding.root.loadapp_button.id -> URL =
                    "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
                binding.root.retrofit_button.id -> URL = "https://github.com/square/retrofit"
            }

        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val query = DownloadManager.Query()
            query.setFilterById(downloadID)
            val manager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor: Cursor = manager.query(query)
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                state = DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)

            }
        }
    }

    companion object {
        private var URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)

    }
}

