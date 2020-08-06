package com.example.tracking_ver4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import static android.Manifest.permission.CAMERA;


public class MainActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "opencv";
    private Mat matInput;
    private Mat matResult;
    private Mat matGaussian=new Mat();
    private Mat matHsv=new Mat();
    private Mat matDilatedMask =new Mat();
    private Mat matMask=new Mat();
    Scalar yellowLower = new Scalar(0,164,114);
    Scalar yellowUpper = new Scalar(94,255,255);
    public ArrayList<Point> deque = new ArrayList<>();

    private CameraBridgeViewBase mOpenCvCameraView;
//    public native void ConvertRGBtoGray(long matAddrInput, long matAddrResult);

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (CameraBridgeViewBase)findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(0); // front-camera(1),  back-camera(0)
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "onResume :: Internal OpenCV library not found.");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "onResum :: OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
    }

    @Override
    public void onCameraViewStopped() {
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        matInput = inputFrame.rgba();
        Mat input = inputFrame.gray();
        Imgproc.blur(input,input,new Size(7,7),new Point(2,2));
        if (matResult == null)
            matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());

//        ConvertRGBtoGray(matInput.getNativeObjAddr(), matResult.getNativeObjAddr());
        org.opencv.core.Size s = new Size(11, 11);
        Imgproc.GaussianBlur(input, matGaussian, s, 0, 0);
        Imgproc.cvtColor(matInput, matHsv, Imgproc.COLOR_RGB2HSV_FULL);
        Core.inRange(matHsv, yellowLower, yellowUpper, matMask);
        //Core.inRange(matHsv, new Scalar(0, 120, 106), new Scalar(6, 211, 172), matMask);
        Imgproc.dilate(matMask, matDilatedMask, new Mat());
        Imgproc.GaussianBlur(matDilatedMask, matGaussian, s, 0, 0);

        Mat matHierarchy = new Mat();
        Mat matContour = new Mat();
        Mat circle = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        MatOfPoint max_contour = new MatOfPoint();
        Imgproc.HoughCircles(matGaussian,circle,Imgproc.HOUGH_GRADIENT,1.0,20,70,30,30,300);
        if (circle.cols() > 0) {
            int maxRadius=0;
            for (int x = 0; x < Math.min(circle.cols(), 1); x++) {
                double circleVec[] = circle.get(0, x);
                if (circleVec == null) {
                    break;
                }
                Point center = new Point((int) circleVec[0], (int) circleVec[1]);
                int radius = (int) circleVec[2];
                Imgproc.circle(matInput, center, 3, new Scalar(255, 255, 255), 5);
                Imgproc.circle(matInput, center, radius, new Scalar(255, 255, 255), 2);


            }

        }
//        Imgproc.findContours(matDilatedMask, contours, matHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
//        double maxArea = 0;
//
//        while(contours.iterator().hasNext()){
//            MatOfPoint contour = contours.iterator().next();
//            double area = Imgproc.contourArea(contour);
//            if(area>maxArea){
//                maxArea =area;
//                max_contour=contour;
//            }
//            Rect rect = Imgproc.boundingRect(max_contour);
//            if (rect.height > 10) {
//                Imgproc.circle(matInput, new Point((rect.br().x + rect.x) / 2, (rect.br().y + rect.y) / 2), rect.width / 2, new Scalar(0, 0, 255), 4);
//                Imgproc.circle(matInput, new Point((rect.br().x + rect.x) / 2, (rect.br().y + rect.y) / 2), 5, new Scalar(0, 0, 255), 1);
//            }
//        }
        return matInput;
    }
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    //여기서부턴 퍼미션 관련 메소드
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;

    protected void onCameraPermissionGranted() {
        List<? extends CameraBridgeViewBase> cameraViews = getCameraViewList();
        if (cameraViews == null) {
            return;
        }
        for (CameraBridgeViewBase cameraBridgeViewBase: cameraViews) {
            if (cameraBridgeViewBase != null) {
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean havePermission = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                havePermission = false;
            }
        }
        if (havePermission) {
            onCameraPermissionGranted();
        }
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            onCameraPermissionGranted();
        }else{
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder( MainActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id){
                requestPermissions(new String[]{CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.create().show();
    }
}