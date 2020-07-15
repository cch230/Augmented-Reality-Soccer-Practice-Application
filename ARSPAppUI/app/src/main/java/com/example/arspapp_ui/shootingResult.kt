package com.example.arspapp_ui

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File


class shootingResult : AppCompatActivity() {

    private var videoView: VideoView? = null
    private var imageView1: ImageView? = null
    private var imageView2: ImageView? = null
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
      /*  Uri videoUri=Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4");

        PlayerView pv;
        //실제 비디오를 플레이하는 객체의 참조 변수
        SimpleExoPlayer player;


        //화면에 보이기 시작할때!!

        //화면에 안보일 때..

        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
        if(file.exists()) {


             mediaPlayer = MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(applicationContext, videouri)
                prepare()
                start()
            }
        }
        setContentView(R.layout.activity_shooting_result)
    }

    override fun onStart() {
        super.onStart();

        //SimpleExoPlayer객체 생성
        player= ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        //플레이어뷰에게 플레이어 설정
        pv.setPlayer(player);

        //비디오데이터 소스를 관리하는 DataSource 객체를 만들어주는 팩토리 객체 생성
        DataSource.Factory factory= new DefaultDataSourceFactory(this,"Ex89VideoAndExoPlayer");
        //비디오데이터를 Uri로 부터 추출해서 DataSource객체 (CD or LP판 같은 ) 생성
        ProgressiveMediaSource mediaSource= new ProgressiveMediaSource.Factory(factory).createMediaSource(videoUri);

        //만들어진 비디오데이터 소스객체인 mediaSource를
        //플레이어 객체에게 전당하여 준비하도록!![ 로딩하도록 !!]
        player.prepare(mediaSource);

        //로딩이 완료되어 준비가 되었을 때
        //자동 실행되도록..
        player.setPlayWhenReady(true);

    }

    override fun onPause() {
        super.onPause()

        //비디오 일시 정지
        if ( videoView != null &&  videoView!!.isPlaying())  videoView!!.pause()
    }

    //액티비티가 메모리에서 사라질때..

    override fun onDestroy() {
        super.onDestroy()
        //
        if ( videoView != null)  videoView!!.stopPlayback()
        mediaPlayer?.release()
        mediaPlayer = null
    }
    @Override
    override fun onStop() {
        super.onStop();
        //플레이어뷰 및 플레이어 객체 초기화
        pv.setPlayer(null);
        player.release();
        player=null;
}

*/