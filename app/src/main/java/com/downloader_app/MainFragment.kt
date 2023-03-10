package com.downloader_app

import android.Manifest
import android.app.DownloadManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.animation.doOnEnd
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.downloader_app.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    private var downloadID: Long = 0
    private var state = false
    private var radioButton: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1
        )
        binding.root.custom_button.setOnClickListener {
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
            radioButton = binding.root.radioGroup.checkedRadioButtonId
            if (radioButton > 0) {
                var title ="Loading"
                binding.root.textView.text = "Loading"
                val animator = custom_button.animatorLoading(binding.root)
                custom_button.animatorCircle()
                val request =
                    DownloadManager.Request(Uri.parse(URL))
                        .setTitle(getString(R.string.app_name))
                        .setDescription(getString(R.string.app_description))
                        .setRequiresCharging(false)

                animator!!.doOnEnd {
                    title ="Download"
                    binding.root.textView.text = title
                    val myIntent = Intent(binding.root.context, DetailActivity::class.java)
                    var body = " "
                    val bodyGlide: String =
                        resources.getString(R.string.glide_image_loading_library_by_bumptech)
                    val bodyLoadApp: String =
                        resources.getString(R.string.loadapp_current_repository_by_udacity)
                    val bodyRetrofit: String =
                        resources.getString(R.string.retrofit_type_safe_http_client_for_android)
                    when (radioButton) {
                        binding.root.glide_button.id -> body = bodyGlide
                        binding.root.loadapp_button.id -> body =bodyLoadApp
                        binding.root.retrofit_button.id -> body =bodyRetrofit
                    }
                    myIntent.putExtra("body", body)
                    myIntent.putExtra("state", state.toString())

                    Notifications(binding.root.context, (PendingIntent.getActivity(
                        binding.root.context,
                        5,
                        Intent(),
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )), ( PendingIntent.getActivity(
                        binding.root.context,
                        5,
                        myIntent,
                        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                    )))
                        .getNotification(
                            binding.root.context.getSystemService(Context.NOTIFICATION_SERVICE),
                            title,
                            body
                        )
                }
            } else {
                val toast = Toast.makeText(
                    binding.root.context,
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
        return binding.root
    }


    var URL =
        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"


}