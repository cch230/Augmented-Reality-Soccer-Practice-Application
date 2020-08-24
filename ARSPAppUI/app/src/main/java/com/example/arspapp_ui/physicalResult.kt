package com.example.arspapp_ui


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*


class physicalResult : AppCompatActivity() {

    private var videoView: VideoView? = null
    private var imageView3: ImageView? = null
    private var textView:TextView?=null
    private val DETAIL_PATH = "DCIM/test1/"
    private var editor:SharedPreferences.Editor?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_physical_result)
        var aty: Activity? = physical_camera.CameraActivity
        aty!!.finish()

        videoView=findViewById(R.id.playTextureView2)
        imageView3=findViewById(R.id.grade2)
        textView=findViewById(R.id.gradePoint2)
        var settings=applicationContext.getSharedPreferences("pref",0)
        var setting_time =settings.getInt("secends",0)
        var editor =settings.edit()
        editor.clear()
        editor.commit()
        var str = intent.getStringExtra("key") //인텐트의 key값을 통해 해당 String을 받는다.
        var grade=intent.getIntExtra("grade",0)

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

        textView!!.setText(grade.toString())
        if(setting_time==60) grade=grade
        else if(setting_time==50) grade=(grade*1.3).toInt()
        else if(setting_time==40) grade=(grade*1.6).toInt()
        else grade=grade*2

        val time = System.currentTimeMillis() //시간 받기
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        //포멧 변환  형식 만들기
        val dd = Date(time) //받은 시간을 Date 형식으로 바꾸기
        val strTime: String = sdf.format(dd) //Data 정보를 포멧 변환하기
        var result_data=""
        val sharedPreferences = getSharedPreferences("physical", 0)
        editor = sharedPreferences.edit()

        if(grade>=60) {
            imageView3!!.setImageResource(R.drawable.a)
            result_data="A"
        }
        else if(grade>=50) {
            imageView3!!.setImageResource(R.drawable.b)
            result_data="B"
        }
        else if(grade>=40) {
            imageView3!!.setImageResource(R.drawable.c)
            result_data="C"
        }
        else if(grade>=30) {
            imageView3!!.setImageResource(R.drawable.d)
            result_data="D"
        }
        else if(grade>=20) {
            imageView3!!.setImageResource(R.drawable.e)
            result_data="E"
        }
        else if(grade>=10) {
            imageView3!!.setImageResource(R.drawable.f)
            result_data="F"
        }
        else {
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
