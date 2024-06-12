package com.example.arspapp_ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.camera2.*;
import android.hardware.camera2.CameraDevice.*;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class trapping extends AppCompatActivity {
    private static final String TAG = "trapping";
    SurfaceView surfaceView_trapping;
    SurfaceHolder holder_trapping;
    MediaRecorder recorder;
    private Button camera_btn3;
    private Button camera_btn4;
    private String mNextVideoAbsolutePath = null;
    private final String sd = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    private String path = (sd + "/video.mp4");
    private TextView count_view;
    private String text;
    private String conversionTime;
    private TextView count_down;
    private ImageView green;
    private Random Ran_move;
    private int move_X;
    private int move_Y;
    private Thread mTimerThread;
    private Boolean mThreadTimer_t;
    private ImageView counter_num;
    //private tracking = com.example.arspapp_ui.tracking()


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_trapping);
        surfaceView_trapping = findViewById(R.id.surfaceView_trapping);

        holder_trapping = surfaceView_trapping.getHolder();
        camera_btn3 = findViewById(R.id.camera_btn3);
        camera_btn4 = findViewById(R.id.camera_btn4);
        count_view = findViewById(R.id.count_view);
        green = findViewById(R.id.green);

        // count_down = findViewById(R.id.count_down);

        Intent intent = getIntent();
        text = Objects.requireNonNull(intent.getExtras()).getString("text");
        // mMyView = findViewById(R.id.mMyView);
        Ran_move = new Random();
        move_X = Ran_move.nextInt(800) + 1;
        move_Y = Ran_move.nextInt(480) + 1;
    }

    public void onClick(View view){
            if (view == camera_btn3) {

                try {
                    if (recorder != null) {
                        recorder.stop();
                        recorder.release();
                        recorder = null;
                    }

                    recorder = new MediaRecorder();


                    recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

                    recorder.setOutputFile(path);
                    recorder.setPreviewDisplay(holder_trapping.getSurface());


                    recorder.prepare();

                    Thread.sleep(3000);


                    recorder.start();
                    green.setVisibility(View.VISIBLE);

                    startHeavyDutyStuff();

                    switch (text) {
                        case "30":

                            conversionTime = "000030";
                            countDown(conversionTime);
                            break;

                        case "40":

                            conversionTime = "000040";
                            countDown(conversionTime);
                            break;
                        case "50":
                            conversionTime = "000050";
                            countDown(conversionTime);
                            break;
                        case "60":
                            conversionTime = "000060";
                            countDown(conversionTime);
                            break;

                        default:
                            Log.i(TAG, "switch 오류 ");
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }


                Toast.makeText(getApplicationContext(), "녹화 시작", Toast.LENGTH_LONG);
            }
        }
     /*  else if (view == camera_btn4) {
            if (recorder != null) {
                recorder.stop();
                recorder.release();
                recorder = null;
            }
            Toast.makeText(getApplicationContext(),"녹화 끝", Toast.LENGTH_LONG);
        }
        */



    public void countDown(String time) {

        long conversionTime;

        // 1000 단위가 1초
        // 60000 단위가 1분
        // 60000 * 3600 = 1시간

        String getHour = time.substring(0, 2);
        String getMin = time.substring(2, 4);
        String getSecond = time.substring(4, 6);

        // "00"이 아니고, 첫번째 자리가 0 이면 제거
        if (getHour.substring(0, 1) == "0") {
            getHour = getHour.substring(1, 2);
        }

        if (getMin.substring(0, 1) == "0") {
            getMin = getMin.substring(1, 2);
        }

        if (getSecond.substring(0, 1) == "0") {
            getSecond = getSecond.substring(1, 2);
        }

        // 변환시간
        conversionTime = Long.valueOf(getHour) * 1000 * 3600 + Long.valueOf(getMin) * 60 * 1000 + Long.valueOf(getSecond) * 1000;

        // 첫번쨰 인자 : 원하는 시간 (예를들어 30초면 30 x 1000(주기))
        // 두번쨰 인자 : 주기( 1000 = 1초)
        new CountDownTimer(conversionTime, 1000) {

            // 특정 시간마다 뷰 변경
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

                // 시간단위
                String hour = String.valueOf(millisUntilFinished / (60 * 60 * 1000));

                // 분단위
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));
                String min = String.valueOf(getMin / (60 * 1000)); // 몫

                // 초단위
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지

                // 밀리세컨드 단위
                String millis = String.valueOf((getMin % (60 * 1000)) % 1000); // 몫

                // 시간이 한자리면 0을 붙인다
                if (hour.length() == 1) {
                    hour = "0" + hour;
                }

                // 분이 한자리면 0을 붙인다
                if (min.length() == 1) {
                    min = "0" + min;
                }

                // 초가 한자리면 0을 붙인다
                if (second.length() == 1) {
                    second = "0" + second;
                }

                count_view.setText(hour + ":" + min + ":" + second);
            }

            // 제한시간 종료시
            public void onFinish() {

                // 변경 후
                count_view.setText("촬영종료!");
                if (recorder != null) {
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                    green.setVisibility(View.INVISIBLE);
                }

                // TODO : 타이머가 모두 종료될때 어떤 이벤트를 진행할지

            }
        }.start();

    }

    public void startHeavyDutyStuff() {


        mTimerThread = new Thread() {


            public void run() {

                mThreadTimer_t = true;

                while (mThreadTimer_t) {

                    Log.e("Thread", "run...");


                    try {

                        Thread.sleep(1 * 2000);

                        messageHandler.sendMessage(Message.obtain(messageHandler, 0x10));

                    } catch (InterruptedException e) {

                    }

                }

            }

        };

        mTimerThread.start();

    }

    private Handler messageHandler = new Handler() {

        @Override

        public void handleMessage(Message msg) {


            switch (msg.what) {


                case 0x10:

                    move_X = Ran_move.nextInt(1040) + 1;

                    move_Y = Ran_move.nextInt(300) + 1;

                    Log.e("move_X =" + move_X + "", "move_Y =" + move_Y + "");

                    RelativeLayout.LayoutParams params = null;

                    params = (RelativeLayout.LayoutParams) green.getLayoutParams();

                    params.leftMargin = move_X;

                    params.topMargin = move_Y;


                    green.setLayoutParams(params);


                case 0x20:


                    break;

            }

        }
    };


}









