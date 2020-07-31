package com.example.arspapp_ui

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class shootingResult : AppCompatActivity() {

    private var videoView: VideoView? = null
    private var imageView1: ImageView? = null
    private var imageView2: ImageView? = null
    private val DETAIL_PATH = "DCIM/test1/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shooting_result)
        var aty: Activity? = CameraActvity.CameraActivity
        aty!!.finish()

        videoView=findViewById(R.id.playTextureView)
        imageView1=findViewById(R.id.ball)
        imageView2=findViewById(R.id.feed)
        val str = intent.getStringExtra("key") //인텐트의 key값을 통해 해당 String을 받는다.
        val trajectory_dir = intent.getStringExtra("trajectory")
        val feedback_dir = intent.getStringExtra("feedback")

        val dir = Environment.getExternalStorageDirectory().absoluteFile
        val path =
                dir.path + "/" + DETAIL_PATH
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        this.sendBroadcast(
                Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse(str)
                )
        )
        val mc = MediaController(this)
        videoView!!.setMediaController(mc)
        //비디오 경로 설정
        videoView!!.setVideoURI(Uri.parse(str))
        ///포커스를 설정
        videoView!!.requestFocus()
        //비디오 뷰의 재생 준비가 완료되었을 때 수행할 내용
        videoView!!.setOnPreparedListener { Toast.makeText(this, "비디오 재생 준비 완료", Toast.LENGTH_LONG).show() }
        //비디오 재생이 완료되었을 때 수행할 내용
        //비디오 파일이 여러 개일 때 다음 비디오 파일을 재생한다던가 사용자에게
        //재생 완료를 알려주는 코드를 작성
        videoView!!.setOnCompletionListener { Toast.makeText(this, "재생이 완료되었습니다.", Toast.LENGTH_LONG).show() }

        val trajectory = BitmapFactory.decodeFile(trajectory_dir)
        val feedback = BitmapFactory.decodeFile(feedback_dir)
        imageView1!!.setImageBitmap(trajectory)
        imageView2!!.setImageBitmap(feedback)
    }

    override fun onPostResume() {
        super.onPostResume()
        //여기서 duration을 1을 준것은 굉장히 중요합니다!! 1을 줘도 무방합니다.
        //원래 MainActivity안에 있어서 this만 사용해도 됩니다.
        Toast.makeText(this, "동영상 준비 중입니다.", Toast.LENGTH_LONG).show()
    }
}
