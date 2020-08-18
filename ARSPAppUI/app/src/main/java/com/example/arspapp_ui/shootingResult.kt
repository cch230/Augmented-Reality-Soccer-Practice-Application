package com.example.arspapp_ui

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class shootingResult : AppCompatActivity() {

    private var videoView: VideoView? = null
    private var imageView1: ImageView? = null
    private var imageView2: ImageView? = null
    private var imageView3: ImageView? = null
    private var textView:TextView?=null
    private val editor:SharedPreferences.Editor?=null
    private val DETAIL_PATH = "DCIM/test1/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shooting_result)
        var aty: Activity? = CameraActvity.CameraActivity
        aty!!.finish()

        videoView=findViewById(R.id.playTextureView)
        imageView1=findViewById(R.id.ball)
        imageView2=findViewById(R.id.feed)
        imageView3=findViewById(R.id.grade)
        textView=findViewById(R.id.text)
        var sete=applicationContext.getSharedPreferences("pref",0)
        var editor =sete.edit()
        editor.clear()
        editor.commit()
        var str = intent.getStringExtra("key") //인텐트의 key값을 통해 해당 String을 받는다.
        var check=intent.getIntExtra("check",0)
        var trajectory_dir:String=""
        var feedback_dir:String=""
        if(check==1){
            trajectory_dir = intent.getStringExtra("trajectory")
            feedback_dir = intent.getStringExtra("feedback")
        }

        var feedback_str=intent.getStringExtra("feedback_str")
        var train_grade=intent.getIntExtra("grade",0)
        Log.i("점수", train_grade.toString())
        val dir = Environment.getExternalStorageDirectory().absoluteFile
        val path =
                dir.path + "/" + DETAIL_PATH
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        Log.i("str",path)
        this.sendBroadcast(
                Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.parse(str)
                )
        )
        val mc: MediaController = MediaController(this)
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
        if(check==1){
            val trajectory = BitmapFactory.decodeFile(trajectory_dir!!)
            val feedback = BitmapFactory.decodeFile(feedback_dir)
            imageView1!!.setImageBitmap(trajectory)
            imageView2!!.setImageBitmap(feedback)
        }
        val time = System.currentTimeMillis() //시간 받기
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        //포멧 변환  형식 만들기
        val dd = Date(time) //받은 시간을 Date 형식으로 바꾸기
        val strTime: String = sdf.format(dd) //Data 정보를 포멧 변환하기
        var result_data=""
        val sharedPreferences = getSharedPreferences("shooting", 0)
        editor = sharedPreferences.edit()

        textView!!.setText(feedback_str)
        if(train_grade==60) {
            imageView3!!.setImageResource(R.drawable.a)
            result_data="A"
        }
        else if(train_grade==50) {
            imageView3!!.setImageResource(R.drawable.b)
            result_data="B"
        }
        else if(train_grade==40) {
            imageView3!!.setImageResource(R.drawable.c)
            result_data="C"
        }
        else if(train_grade==30) {
            imageView3!!.setImageResource(R.drawable.d)
            result_data="D"
        }
        else if(train_grade==20) {
            imageView3!!.setImageResource(R.drawable.e)
            result_data="E"
        }
        else if(train_grade==10) {
            imageView3!!.setImageResource(R.drawable.f)
            result_data="F"
        }
        else if(train_grade==10) {
            imageView3!!.setImageResource(R.drawable.x)
            result_data="X"
        }
        editor.putString(strTime, result_data)
        editor.commit()
    }

    override fun onPostResume() {
        super.onPostResume()
        //여기서 duration을 1을 준것은 굉장히 중요합니다!! 1을 줘도 무방합니다.
        //원래 MainActivity안에 있어서 this만 사용해도 됩니다.
        Toast.makeText(this, "동영상 준비 중입니다.", Toast.LENGTH_LONG).show()
    }
}
