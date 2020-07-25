package com.example.arspapp_ui;

import android.graphics.Bitmap;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;


public class tracking{
    public Boolean CanNotFind=false;

    private Scalar yellowLower = new Scalar(20,90,140);
    private Scalar yellowUpper = new Scalar(125,255,255);

    public Double[] circleVec_save=new Double[2];
    public Double distance=0.0;




    public Bitmap trackingBall(Bitmap bitmap){
        Mat matGaussian = new Mat();
        Mat matHsv = new Mat();
        Mat matMask = new Mat();
        Mat matMask1 = new Mat();
        Mat matCny = new Mat();
        Mat matInput = new Mat();
        Utils.bitmapToMat(bitmap,matInput);

        Point center;


        Imgproc.cvtColor(matInput,matHsv,Imgproc.COLOR_RGB2HSV_FULL);
        Core.inRange(matHsv,yellowLower,yellowUpper,matMask1);
        Imgproc.threshold(matMask1,matMask1,127,255,Imgproc.THRESH_BINARY);

        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,new Size(11,11));
        Imgproc.dilate(matMask1,matMask1,element);
        Imgproc.erode(matMask1,matMask,new Mat());
        Imgproc.blur(matMask,matGaussian,new Size(3,3),new Point(2,2));
        Imgproc.Canny(matGaussian,matCny,130,210,3);


        Mat circle = new Mat();
        Imgproc.HoughCircles(matGaussian,circle,Imgproc.HOUGH_GRADIENT,2,matInput.height()/4,100,40,1,200);

        if(circle.cols()>0) {
            for (int x = 0; x < Math.min(circle.cols(), 1); x++) {
                double circleVec[] = circle.get(0, x);
                if (circleVec == null)
                    break;
                center = new Point((int) circleVec[0], (int) circleVec[1]);
                circleVec_save[0] = circleVec[0];
                circleVec_save[1] = circleVec[1];

                int radius = (int) circleVec[2];
                Imgproc.circle(matInput, center, radius, new Scalar(0, 0, 0), 2);
            }
        }
        else {
            CanNotFind =true;
        }

        Utils.matToBitmap(matInput,bitmap);
        return bitmap;
    }




}