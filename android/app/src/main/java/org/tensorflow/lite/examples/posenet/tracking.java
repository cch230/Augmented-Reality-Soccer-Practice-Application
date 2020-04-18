package org.tensorflow.lite.examples.posenet;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;

public class tracking {
    private Mat matResult;
    private Mat matGaussian=new Mat();
    private Mat matHsv=new Mat();
    private Mat matDilatedMask =new Mat();
    private Mat matMask=new Mat();
    Scalar yellowLower = new Scalar(0,164,114);
    Scalar yellowUpper = new Scalar(94,255,255);
    public Double[] circleVec_save=new Double[2];
    ArrayList <Point> line_list=new ArrayList<>();
    public Bitmap trackingBall(Bitmap bitmap){
        Mat matInput=new Mat();
        Utils.bitmapToMat(bitmap, matInput);
        Mat input =new Mat();
        Utils.bitmapToMat(bitmap, input);


        Imgproc.blur(input,input,new Size(7,7),new Point(2,2));
        if (matResult == null)
            matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());

        org.opencv.core.Size s = new Size(11, 11);
        Imgproc.GaussianBlur(input, matGaussian, s, 0, 0);
        Imgproc.cvtColor(matInput, matHsv, Imgproc.COLOR_RGB2HSV_FULL);
        Core.inRange(matHsv, yellowLower, yellowUpper, matMask);
        Imgproc.dilate(matMask, matDilatedMask, new Mat());
        Imgproc.GaussianBlur(matDilatedMask, matGaussian, s, 0, 0);


        Mat circle = new Mat();

        Imgproc.HoughCircles(matGaussian,circle,Imgproc.HOUGH_GRADIENT,2.0,20,70,30,30,300);
        if (circle.cols() > 0) {
            for (int x = 0; x < Math.min(circle.cols(), 1); x++) {
                double circleVec[] = circle.get(0, x);
                if (circleVec == null) {
                    break;
                }
                Point center = new Point((int) circleVec[0], (int) circleVec[1]);
                circleVec_save[0]=circleVec[0];
                circleVec_save[1]=circleVec[1];

                int radius = (int) circleVec[2];
                Imgproc.circle(matInput, center, 3, new Scalar(0, 0, 0), 2);
                Imgproc.circle(matInput, center, radius, new Scalar(0, 0, 0), 2);

                line_list.add(0,center);
                while (line_list.size()>6){
                    line_list.remove(line_list.size()-1);
                }
                for(int j=1;j<line_list.size();j++){
                    System.out.println("center"+line_list.get(j));
                    int thickness = (int)(Math.sqrt(40/((float)(j+1)))*5);
                    Imgproc.line(matInput, line_list.get(j-1),line_list.get(j),new Scalar(0,0,255),thickness);

                }
            }
        }
        Utils.matToBitmap(matInput, bitmap);
        return bitmap;
    }

}