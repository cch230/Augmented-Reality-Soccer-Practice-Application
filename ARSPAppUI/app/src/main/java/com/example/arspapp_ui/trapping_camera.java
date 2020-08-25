package com.example.arspapp_ui;
        import android.app.Activity;
        import android.content.SharedPreferences;
        import android.os.Bundle;

        import android.util.Log;
        import android.view.WindowManager;
        import androidx.appcompat.app.AppCompatActivity;
        import org.opencv.android.BaseLoaderCallback;
        import org.opencv.android.LoaderCallbackInterface;
        import org.opencv.android.OpenCVLoader;


public class trapping_camera extends AppCompatActivity {
    public static final String TAG = "src";
    public static Activity CameraActivity;
    public String data;
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.wtf(TAG, "OpenCV failed to load!");
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraActivity=trapping_camera.this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        Log.i("idontknow","What");
        Log.i("data1",String.valueOf(data));
        setContentView(R.layout.activity_camera);
        loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        data = getIntent().getStringExtra("key");
        loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        SharedPreferences sharedPreferences=getSharedPreferences("pref",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        int secends=Integer.parseInt(data);
        editor.putInt("secends",secends);
        editor.commit();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new trapping_posenet())
                    .commit();
        }
    }
    private BaseLoaderCallback loaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };



}