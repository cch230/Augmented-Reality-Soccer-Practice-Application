package com.example.arspapp_ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.rengwuxian.materialedittext.MaterialEditText


class shootingResult : AppCompatActivity() {

    private var videoView: VideoView? = null
    private var imageView1:ImageView? = null
    private var imageView2:ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val str = intent.getStringExtra("key") //인텐트의 key값을 통해 해당 String을 받는다.

        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        setContentView(R.layout.activity_shooting_result)
    }
}