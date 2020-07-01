package com.example.ball_ex;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
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
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.sql.Blob;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import static android.Manifest.permission.CAMERA;


public class MainActivity extends AppCompatActivity
        implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "opencv";
    private Mat matInput;
    private Mat matResult;
    private Mat matGaussian = new Mat();
    private Mat matHsv = new Mat();
    private Mat matDilatedMask = new Mat();
    private Mat matMask = new Mat();
    private Mat matMask2 = new Mat();
    private Mat matDilatedMask2 = new Mat();
    private Mat matGaussian2 = new Mat();
    private Mat matGaussian1 = new Mat();
    private Mat matBall = new Mat();
    private Mat matRoi = new Mat();
    private int trackingOx = 0;
    private boolean trackOX = false;

    private Rect rectTrack = new Rect();
    Scalar yellowLower = new Scalar(0, 164, 114);
    Scalar yellowUpper = new Scalar(94, 255, 255);
    ArrayList<Point> line_list = new ArrayList<>();

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
                case LoaderCallbackInterface.SUCCESS: {
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
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

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.activity_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        mOpenCvCameraView.setCameraIndex(0); // front-camera(1),  back-camera(0)
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
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
        int bin[][] = new int[matInput.height()][matInput.width()];
        Mat input = inputFrame.gray();
        Imgproc.blur(input, input, new Size(7, 7), new Point(2, 2));
        if (matResult == null)
            matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());

//        ConvertRGBtoGray(matInput.getNativeObjAddr(), matResult.getNativeObjAddr());
        org.opencv.core.Size s = new Size(11, 11);
        Imgproc.GaussianBlur(input, matGaussian, s, 0, 0);
        Imgproc.cvtColor(matInput, matHsv, Imgproc.COLOR_RGB2HSV_FULL);
        //Core.inRange(matHsv, yellowLower, yellowUpper, matMask);
        Core.inRange(matHsv, new Scalar(27, 30, 45), new Scalar(60, 255, 255), matMask);
        Core.inRange(matHsv, new Scalar(10, 90, 30), new Scalar(40, 255, 80), matMask2);

        Imgproc.erode(matMask, matMask, new Mat());
        Imgproc.dilate(matMask, matDilatedMask, new Mat());
        Imgproc.GaussianBlur(matDilatedMask, matGaussian1, s, 0, 0);

        Imgproc.erode(matMask2, matMask2, new Mat());
        Imgproc.dilate(matMask2, matDilatedMask2, new Mat());
        Imgproc.GaussianBlur(matDilatedMask2, matGaussian2, s, 0, 0);
        Core.add(matGaussian1, matGaussian2, matGaussian);

        Mat matHierarchy = new Mat();
        Mat matContour = new Mat();
        Mat circle = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        MatOfPoint max_contour = new MatOfPoint();
        Imgproc.threshold(matGaussian, matGaussian, 127, 255, Imgproc.THRESH_BINARY);
        Imgproc.erode(matGaussian, matGaussian, new Mat());
        Rect rect = new Rect();
        /*if(trackOX==false) {
            Imgproc.HoughCircles(matGaussian, circle, Imgproc.HOUGH_GRADIENT, 1.0, 10, 70, 30, 1, 300);//70,30
        }*/
       /* else {

            Imgproc.HoughCircles(matBall, circle, Imgproc.HOUGH_GRADIENT, 1, 10, 70, 30, 1, 300);
        }*/
        /*for (int i = 0; i < matGaussian.height(); i++) {
            for (int j = 0; j < matGaussian.width(); j++){
                if(matGaussian.get(i,j)[0]>50){
                    bin[i][j]=1;
                }
                else
                    bin[i][j]=0;
            }
        }
        for (int i = 0; i < matGaussian.height(); i++) {
            for (int j = 0; j < matGaussian.width(); j++){
                System.out.println(bin[i][j]);
            }
        }*/
        Imgproc.HoughCircles(matGaussian, circle, Imgproc.HOUGH_GRADIENT, 1.0, 10, 70, 30, 1, 300);//70,30

        if (circle.cols() > 0) {
            int maxRadius = 0;
            for (int x = 0; x < Math.min(circle.cols(), 1); x++) {
                double circleVec[] = circle.get(0, x);
                if (circleVec == null) {
                    break;
                }
                Point center = new Point((int) circleVec[0], (int) circleVec[1]);
                int radius = (int) circleVec[2];

                /*if(center!=null) {
                    if(center.x-(radius*2)<=0)
                        rect.x = 0;
                    else if(center.x+(radius*2)>=matInput.width())
                        rect.x = matInput.width()-(radius*4);
                    else rect.x =(int) (center.x - (radius * 2));
                    if(center.y-(radius*2)<=0)
                        rect.y = 0;
                    else if(center.y+(radius*2)>=matInput.height())
                        rect.y=matInput.height()-(radius*4);
                    else rect.y = (int)center.y-(radius*2);
                    rect.height = (int) radius * 4;
                    rect.width = (int) radius * 4;
                    if (rect != null) {
                        matBall = matGaussian.submat(rect);
                        trackOX=true;
                    }
                }
*/
                /*
                Imgproc.circle(matInput, center, 3, new Scalar(255, 255, 255), 5);
                Imgproc.circle(matInput, center, radius, new Scalar(255, 255, 255), 2);
*/
                /*rect.x = (int) (center.x - radius);
                rect.y = (int) (center.y - radius);
                rect.height = (int) radius * 2;
                rect.width = (int) radius * 2;
                matBall=matInput.submat(rect);
                matHist = new Mat(matBall.size(),matBall.depth());
                Imgproc.cvtColor(matBall,matHist,Imgproc.COLOR_BayerBG2GRAY);
                Imgproc.threshold(matHist,matHist,50,255,Imgproc.THRESH_BINARY);
                int numofLabel = Imgproc.connectedComponentsWithStats(matHist,matBack,matInput,matHue,8, CvType.CV_32S);
*/

                //Point centerB=new Point(matHue.cols(),matHue.rows());
                Imgproc.circle(matInput, center, 3, new Scalar(255, 255, 255), 5);
                Imgproc.circle(matInput, center, radius, new Scalar(255, 255, 255), 5);
                /*
                if (rect.width > 0 && rect.height > 0&&trackOX==true) {
                    trackingOx=-1;
                    trackOX=false;
                }
                if(trackingOx!=0){
                    matHue.create(matHsv.size(), matHsv.depth());
                    List<Mat> hueList = new LinkedList<Mat>();
                    List<Mat> hsvList = new LinkedList<Mat>();
                    hsvList.add(matHsv);
                    hueList.add(matHue);
                    MatOfInt ch = new MatOfInt(0,0);
                    Core.mixChannels(hsvList, hueList, ch);
                    MatOfFloat histRange = new MatOfFloat(0, 180);
                    if (trackingOx < 0) {
                        Mat matBall = matInput.submat(rect);
                        Imgproc.calcHist(Arrays.asList(matBall), new MatOfInt(0), new Mat(), matHist, new MatOfInt(16), histRange);
                        Core.normalize(matHist, matHist, 0, 255, Core.NORM_MINMAX);
                        rectTrack = rect;
                        trackingOx = 1;
                    }
                    MatOfInt ch2 = new MatOfInt(0, 1);
                    Imgproc.calcBackProject(Arrays.asList(matHue), ch2, matHist, matBack, histRange, 1);
                    Core.bitwise_and(matBack, matMask, matBack);
                    RotatedRect trackBox = Video.CamShift(matBack, rectTrack, new TermCriteria(TermCriteria.EPS | TermCriteria.MAX_ITER, 10,1));
                    Imgproc.ellipse(matInput, trackBox, new Scalar(0,0,255),4);
                    if (rectTrack.area() <= 1) { trackingOx = 0; }
                }
                if(rect!=null)
                    Imgproc.rectangle(matInput,rect.tl(),rect.br(),new Scalar(0,255,255),2);
*/
                //line_list.add(0,new Point((int)center.x,(int)center.y));
                line_list.add(0, center);
                while (line_list.size() > 6) {
                    line_list.remove(line_list.size() - 1);
                }
                for (int j = 1; j < line_list.size(); j++) {
                    System.out.println("center" + line_list.get(j));
                    int thickness = (int) (Math.sqrt(40 / ((float) (j + 1))) * 5);
                    Imgproc.line(matInput, line_list.get(j - 1), line_list.get(j), new Scalar(255, 255, 255), thickness);

                }
            }

        }

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
        for (CameraBridgeViewBase cameraBridgeViewBase : cameraViews) {
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