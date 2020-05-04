package org.tensorflow.lite.examples.posenet;

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
    private Scalar yellowLower = new Scalar(0,164,114);
    private Scalar yellowUpper = new Scalar(94,255,255);
    private Double[] circleVec_save=new Double[2];
    private ArrayList <Point> line_list=new ArrayList<>();
    private List<MatOfPoint> contours = new ArrayList<>();
    private Rect rect;
    private Double distance=0.0;
    private Boolean sig=false;

    public Bitmap trackingBall(Bitmap bitmap){
        Mat matGaussian=new Mat();
        Mat matHsv=new Mat();
        Mat matDilatedMask =new Mat();
        Mat matMask=new Mat();
        Mat matCny=new Mat();
        Mat matGray=new Mat();
        Mat hierarchy = new Mat();
        Mat matInput=new Mat();
        Utils.bitmapToMat(bitmap, matInput);
        Mat input =new Mat();
        Utils.bitmapToMat(bitmap, input);
        Mat kernel1=Mat.ones(2,2,0);
        //Mat kernel2=Mat.ones(9,5,0);
        Point goalpost=null;
        Point center=null;
        String myText;
        Point myPoint;
        int myFontFace;
        Double myFontScale;




        Imgproc.blur(input,input,new Size(7,7),new Point(2,2));
        if (matResult == null)
            matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());

        org.opencv.core.Size s = new Size(11, 11);
        Imgproc.GaussianBlur(input, matGaussian, s, 0, 0);
        Imgproc.cvtColor(matInput, matHsv, Imgproc.COLOR_RGB2HSV_FULL);
        Core.inRange(matHsv, yellowLower, yellowUpper, matMask);
        Imgproc.dilate(matMask, matDilatedMask, new Mat());
        Imgproc.GaussianBlur(matDilatedMask, matGaussian, s, 0, 0);
        Imgproc.cvtColor(input, matGray, Imgproc.COLOR_BGR2GRAY,1);


        Imgproc.Canny(matGray, matCny, 10, 100, 3, true); // Canny Edge 검출
        Imgproc.adaptiveThreshold(matCny, matGray, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 3, 12);  //block size 3
        Imgproc.morphologyEx(matGray,matGray,Imgproc.MORPH_GRADIENT,kernel1);
        //Imgproc.morphologyEx(matGray,matGray,Imgproc.MORPH_CLOSE,kernel2);
        Imgproc.findContours(matGray, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        if(contours.size()>0&&rect==null) {
           // for(int idx = 0; idx < contours.size(); idx++) {
            rect = Imgproc.boundingRect(contours.get(contours.size()-1));
                if (/*rect.width /rect.height>2 && rect.width /rect.height<4 &&*/ rect.width > rect.height && rect.height > 10 && rect.width > 40 && !(rect.width >= 512 - 5 && rect.height >= 512 - 5)){
                    goalpost=new Point(rect.br().x - rect.width/2,rect.br().y);
                    Imgproc.rectangle(matInput,new Point(rect.br().x - rect.width, rect.br().y - rect.height), rect.br(), new Scalar(0, 0, 0), 3);
                    Imgproc.circle(matInput,goalpost,1,new Scalar(255, 0, 0));
                }
           // }
        }
        if(rect!=null){
            Imgproc.rectangle(matInput, new Point(rect.br().x - rect.width, rect.br().y - rect.height)
                    , rect.br()
                    , new Scalar(0, 0, 0), 3);
            Imgproc.circle(matInput,new Point(rect.br().x - rect.width/2,rect.br().y),4,new Scalar(255, 0, 0));
        }


        Mat circle = new Mat();

        Imgproc.HoughCircles(matGaussian,circle,Imgproc.HOUGH_GRADIENT,2.0,20,70,30,20,1000);
        if (circle.cols() > 0) {
            for (int x = 0; x < Math.min(circle.cols(), 1); x++) {
                double circleVec[] = circle.get(0, x);
                if (circleVec == null) {
                    break;
                }
                center = new Point((int) circleVec[0], (int) circleVec[1]);
                Log.i("center",center.toString());
                circleVec_save[0]=circleVec[0];
                circleVec_save[1]=circleVec[1];

                int radius = (int) circleVec[2];
                //Imgproc.circle(matInput, center, 3, new Scalar(0, 0, 0), 2);
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
       if(goalpost!=null&&center!=null&&sig==false){
            sig=true;
            distance=ballspeed(goalpost,center)*40;
            Log.i("distance",distance.toString());
       }
        if(distance!=0.0){
            myText = "거리: "+ distance.toString();
        }
        /// Text Location
        myPoint=new Point();
        myPoint.x = 10;
        myPoint.y = 550;
        /// Font Face
        myFontFace = 2;
        /// Font Scale
        myFontScale = 1.2;
        Imgproc.putText( matInput, "DISTANCE: "+ 0.542+"M", myPoint, myFontFace, myFontScale, new Scalar(0,255,0));
        Utils.matToBitmap(matInput, bitmap);
        return bitmap;
    }

    Double ballspeed(@NotNull Point goalpost, @NotNull Point center){
        Double dist;
        Double vecX= goalpost.x- center.x;
        Double vecY= goalpost.y- center.y;
        dist= Math.sqrt(Math.pow(vecX,2)+Math.pow(vecY,2));
        Double result=speedFunc(dist);
        return result;
    }

    Double speedFunc(Double dist){
        Double speed=12.925*Math.pow(dist,-1);
        return  speed;
    }
}