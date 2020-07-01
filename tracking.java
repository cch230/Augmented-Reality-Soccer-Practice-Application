package com.example.arspapp_ui;
import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class tracking {
    private Mat matResult;
    private Scalar red = new Scalar(255,0,0);
    private Scalar whiteLower = new Scalar(0,0,180);
    private Scalar whiteUpper = new Scalar(255,30,255);

    private List<MatOfPoint> contours = new ArrayList<>();
    private Rect rectGoals;

    public Bitmap trackingBall(Bitmap bitmap,Boolean signal){
        Mat matHsvGoals = new Mat();
        Mat matDilatedMaskGoals =new Mat();
        Mat matDilatedMaskGoals2 =new Mat();
        Mat matDilatedMaskGoals3 =new Mat();
        Mat matDilatedMaskGoals4 =new Mat();
        Mat matContourGoals =new Mat();
        Mat kernelGoals = Mat.ones(5,5,0);
        Mat matMaskGoals = new Mat();
        Mat hierarchyGoals = new Mat();
        Mat matInputGoals = new Mat();
        Utils.bitmapToMat(bitmap, matInputGoals);
        Mat input = new Mat();
        Utils.bitmapToMat(bitmap, input);


        if (matResult == null)
            matResult = new Mat(matInputGoals.rows(), matInputGoals.cols(), matInputGoals.type());

        Imgproc.cvtColor(input, matHsvGoals, Imgproc.COLOR_RGB2HSV_FULL);
        Core.inRange(matHsvGoals, whiteLower, whiteUpper, matMaskGoals);
        Imgproc.dilate(matMaskGoals, matDilatedMaskGoals, new Mat());
        Imgproc.dilate(matDilatedMaskGoals, matDilatedMaskGoals2, new Mat());
        Imgproc.dilate(matDilatedMaskGoals2, matDilatedMaskGoals3, new Mat());
        Imgproc.dilate(matDilatedMaskGoals3, matDilatedMaskGoals4, new Mat());
        Imgproc.morphologyEx(matDilatedMaskGoals4, matContourGoals, Imgproc.MORPH_CLOSE, kernelGoals);

        Imgproc.findContours(matContourGoals, contours, hierarchyGoals, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        if(contours.size() > 0) {
            rectGoals = Imgproc.boundingRect(contours.get(contours.size() - 1));
            if (rectGoals.x != 0 && rectGoals.y != 0 && rectGoals.width > 2 * rectGoals.height && 3 * rectGoals.height > rectGoals.width){
                Imgproc.rectangle(matInputGoals, rectGoals.tl(), rectGoals.br(), red, 3);
            }
        }
        Utils.matToBitmap(matInputGoals,bitmap);
        return bitmap;
    }
}