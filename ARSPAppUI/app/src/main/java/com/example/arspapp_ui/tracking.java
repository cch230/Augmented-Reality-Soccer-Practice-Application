package com.example.arspapp_ui;
import android.graphics.Bitmap;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;

public class tracking {
    private Mat matResult;
    private Scalar yellowLower = new Scalar(20,30,60);
    private Scalar yellowUpper = new Scalar(59,255,255);

    public Double[] circleVec_save=new Double[2];
    private ArrayList <Point> line_list=new ArrayList<>();
    private List<MatOfPoint> contours = new ArrayList<>();
    public Double distance=0.0;
    private Boolean dist_sig=false;
    public Boolean CanNotFind=false;
    public boolean Imready=false;
    public Double r=null;
    private Scalar red = new Scalar(255,0,0);
    private Scalar whiteLower = new Scalar(0,0,180);
    private Scalar whiteUpper = new Scalar(255,30,255);

    private Rect rectGoals;

    public Bitmap trackingBall(Bitmap bitmap,Boolean signal){
        Mat matGaussian=new Mat();
        Mat matHsv=new Mat();
        Mat matDilatedMask =new Mat();
        Mat matMask=new Mat();
        Mat matInput=new Mat();
        Utils.bitmapToMat(bitmap, matInput);
        Mat input =new Mat();
        Utils.bitmapToMat(bitmap, input);
        Point goalpost=null;
        Point center;
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
        Utils.bitmapToMat(bitmap, input);





        Imgproc.blur(input,input,new Size(7,7),new Point(2,2));
        if (matResult == null)
            matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());

        org.opencv.core.Size s = new Size(11, 11);
        Imgproc.GaussianBlur(input, matGaussian, s, 0, 0);
        Imgproc.cvtColor(input, matHsv, Imgproc.COLOR_RGB2HSV_FULL);
        Core.inRange(matHsv, yellowLower, yellowUpper, matMask);
        Imgproc.dilate(matMask, matDilatedMask, new Mat());
        Imgproc.GaussianBlur(matDilatedMask, matGaussian, s, 0, 0);
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







        Mat circle = new Mat();
        circleVec_save[0]=null;
        circleVec_save[1]=null;

        Imgproc.HoughCircles(matGaussian,circle,Imgproc.HOUGH_GRADIENT,2.0,20,70,30,20,1000);
        center = new Point(560, 480);
        int radius = 8;
        Imgproc.circle(matInput, center, radius, new Scalar(0, 0, 0), 2);
        if (circle.cols() > 0) {
            for (int x = 0; x < Math.min(circle.cols(), 1); x++) {
                double circleVec[] = circle.get(0, x);
                if (circleVec == null) {
                    break;
                }
                center = new Point(100, 100);
                Log.i("center_r",center.toString());
                circleVec_save[0]=circleVec[0];
                circleVec_save[1]=circleVec[1];
                radius = 8;
                //(int) circleVec[2];
                r=radius+0.0;
                Log.i("di",String.valueOf(r));
                //Imgproc.circle(matInput, center, 3, new Scalar(0, 0, 0), 2);
                Imgproc.circle(matInput, center, radius, new Scalar(0, 0, 0), 2);

                line_list.add(0,center);
                while (line_list.size()>6){
                    line_list.remove(line_list.size()-1);
                }
                Double avgX=0.0, avgY=0.0;
                for(int j=1;j<line_list.size();j++) {
                    avgX+=line_list.get(j-1).x;
                    avgY+=line_list.get(j-1).y;
                }
                avgX/=5;
                avgY/=5;
                int ready[]=new int[5];
                for(int j=1;j<line_list.size();j++) {
                    ready[j-1]=0;
                    if(Math.abs(avgX-line_list.get(j-1).x)<25&&Math.abs(avgY-line_list.get(j-1).y)<25) {
                        ready[j-1] = 1;
                    }
                }
                if(ready[0]==1&&ready[1]==1&&ready[2]==1&&ready[3]==1&&ready[4]==1){
                    Imready=true;
                }
                if(signal){
                    for(int j=1;j<line_list.size();j++){

                        System.out.println("center_r"+line_list.get(j));
                        int thickness = (int)(Math.sqrt(40/((float)(j+1)))*5);
                        Imgproc.line(matInput, line_list.get(j-1),line_list.get(j),new Scalar(0,0,255),thickness);
                    }
                }
            }
        }
        else{
            CanNotFind =true;
        }
        if(goalpost!=null&&center!=null&&dist_sig==false){
            dist_sig=true;
            distance=balldistance(goalpost,center)*40;
            Log.i("distance",distance.toString());
        }


        if(contours.size() > 0) {
            rectGoals = Imgproc.boundingRect(contours.get(contours.size() - 1));
            if (rectGoals.x != 0 && rectGoals.y != 0 && rectGoals.width > 2 * rectGoals.height && 3 * rectGoals.height > rectGoals.width){
                Imgproc.rectangle(matInput, rectGoals.tl(), rectGoals.br(), red, 3);
            }
        }



        Utils.matToBitmap(matInput,bitmap);
        return bitmap;
    }

    Double balldistance(@NotNull Point goalpost, @NotNull Point center){
        Double dist;
        Double vecX= goalpost.x- center.x;
        Double vecY= goalpost.y- center.y;
        dist= Math.sqrt(Math.pow(vecX,2)+Math.pow(vecY,2));
        Double result=distanceFunc(dist);
        return result;
    }

    Double distanceFunc(Double dist){
        Double speed=12.925*Math.pow(dist,-1);
        return  speed;
    }
}