package com.example.arspapp_ui;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import java.lang.Math;
public class test {
    private Mat matResult;
    private Scalar yellowLower = new Scalar(20, 60, 30);
    private Scalar yellowUpper = new Scalar(59, 255, 255);
    // private Scalar whiteUpper = new Scalar(116,110,221);
    //private Scalar whiteLower = new Scalar(38,0,152);
    public Double[] circleVec_save = new Double[3];
    public int x_newSaver = 0;
    public int y_newSaver = 0;
    public int w_newSaver = 0;
    public int x_LSaver = 0;
    public int y_LSaver = 0;
    public int x_MSaver = 0;
    public int y_MSaver = 0;
    public int x_RSaver = 0;
    public int y_RSaver = 0;
    public int R_old_avg = 0;
    public int G_old_avg = 0;
    public int B_old_avg = 0;
    public int R_LAvg = 0;
    public int G_LAvg = 0;
    public int B_LAvg = 0;
    public int R_MAvg = 0;
    public int B_MAvg = 0;
    public int G_MAvg = 0;
    public int R_RAvg = 0;
    public int G_RAvg = 0;
    public int B_RAvg = 0;
    private int FindCnt = 0;
    protected final static int C_confirmed = 0;
    protected final static int L_confirmed = 1;
    protected final static int M_confirmed = 2;
    protected final static int R_confirmed = 3;
    private Boolean L_checked = false;
    private Boolean M_checked = false;
    private Boolean R_checked = false;
    private Boolean C_checked = false;
    public int frame = 0;
    private ArrayList<Point> line_list = new ArrayList<>();
    private List<MatOfPoint> contours = new ArrayList<>();
    private Rect rect;
    public Double distance = 0.0;
    private Boolean dist_sig = false;
    public Boolean CanNotFind = false;
    public Double[][] goalpostPoint = new Double[9][2];
    public boolean Imready = false;
    public int operate = 0;

    public Bitmap trackingBall(Bitmap bitmap, Boolean signal) {
        Mat matGaussian = new Mat();
        Mat matHsv = new Mat();
        Mat matDilatedMask = new Mat();
        Mat matMask = new Mat();
        Mat matCny = new Mat();
        Mat matGray = new Mat();
        Mat hierarchy = new Mat();
        Mat matInput = new Mat();
        Utils.bitmapToMat(bitmap, matInput);
        Mat input = new Mat();
        Utils.bitmapToMat(bitmap, input);
        //Mat kernel1=Mat.ones(2,2,0);
        Mat kernel2 = Mat.ones(5, 5, 0);
        Point goalpost = null;
        Point center = null;
        Boolean shoot = false;

        // Imgproc.blur(input,input,new Size(7,7),new Point(2,2));
        if (true/*signal*/) {

            if (matResult == null)
                matResult = new Mat(matInput.rows(), matInput.cols(), matInput.type());

            Size s = new Size(11, 11);
            Imgproc.GaussianBlur(input, matGaussian, s, 0, 0);
            Imgproc.cvtColor(input, matHsv, Imgproc.COLOR_RGB2HSV_FULL);
            Core.inRange(matHsv, yellowLower, yellowUpper, matMask);
            Imgproc.dilate(matMask, matDilatedMask, new Mat());
            Imgproc.GaussianBlur(matDilatedMask, matGaussian, s, 0, 0);
            //      Imgproc.cvtColor(input, matGray, Imgproc.COLOR_BGR2GRAY,1);

            //Core.inRange(matHsv, whiteLower, whiteUpper, matMask);
            // Imgproc.Canny(matGray, matCny, 10, 100, 3, true); // Canny Edge 검출

            //   Imgproc.adaptiveThreshold(input, matGray, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 3, 12);  //block size 3
            //Imgproc.morphologyEx(matGray,matCny,Imgproc.MORPH_GRADIENT,kernel2);
            //  Imgproc.morphologyEx(matGray,matDilatedMask,Imgproc.MORPH_CLOSE,kernel2);
            // Imgproc.findContours(matDilatedMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

            //  if(contours.size()>0) {

            // for(int idx = 0; idx < contours.size(); idx++) {
            // rect = Imgproc.boundingRect(contours.get(contours.size()-1));
                  /*  if (rect.width /rect.height>2 && rect.width /rect.height<4 && rect.width>rect.height&&rect.height<300&&rect.width<300&&rect.height > 10 && rect.width > 40){
                        goalpost=new Point(rect.br().x - rect.width/2,rect.br().y);
                        Imgproc.rectangle(matInput,new Point(rect.br().x - rect.width, rect.br().y - rect.height), rect.br(), new Scalar(0,255, 0), 3);
                        Imgproc.circle(matInput,goalpost,1,new Scalar(255, 0, 0));
                    }*/
            // }
            //  }
               /* if(rect!=null){
                    Imgproc.rectangle(matInput, new Point(rect.br().x - rect.width, rect.br().y - rect.height)
                            , rect.br()
                            , new Scalar(0, 0, 0), 3);
                    Imgproc.circle(matInput,new Point(rect.br().x - rect.width/2,rect.br().y),4,new Scalar(255, 0, 0));
                }
        */
            Mat circle = new Mat();
            circleVec_save[0] = null;
            circleVec_save[1] = null;
            circleVec_save[2] = null;
            Imgproc.HoughCircles(matGaussian, circle, Imgproc.HOUGH_GRADIENT, 2.0, 20, 70, 30, 5, 1000);

            if (circle.cols() > 0) { //검출
                if (operate != 5) { //동작 하지않았다면
                    operate++; //1회성
                    for (int x = 0; x < Math.min(circle.cols(), 1); x++) {
                        double circleVec[] = circle.get(0, x);
                        if (circleVec == null) {
                            break;
                        }
                        center = new Point((int) circleVec[0], (int) circleVec[1]);
                        Log.i("center_r", center.toString());
                        circleVec_save[0] = circleVec[0];
                        circleVec_save[1] = circleVec[1];
                        circleVec_save[2] = circleVec[2];
                        int radius = (int) circleVec[2];
                        x_newSaver = circleVec_save[0].intValue();
                        y_newSaver = circleVec_save[1].intValue();
                        if (frame < 11) {
                            w_newSaver = 20;

                        } else {
                            w_newSaver = 10;
                        }

                        Imgproc.circle(matInput, center, radius, new Scalar(0, 0, 0), 2);
                        line_list.add(0, center);
                    }

                }
                if (operate == 5) {
                    if (x_newSaver != 0 && y_newSaver != 0 && w_newSaver != 0) { //검출확인
                        int[] avg = trackingArea(bitmap, x_newSaver, y_newSaver, w_newSaver);  //구역 평균 계산
                        //Imgproc.circle(matInput, center, 3, new Scalar(0, 0, 0), 2);

                        if (R_old_avg == 0 && G_old_avg == 0 && B_old_avg == 0) { //처음
                            R_old_avg = avg[0];
                            G_old_avg = avg[1];
                            B_old_avg = avg[2];
                        } else {  //처음이 아니라면
                            FindDifference(
                                    bitmap,
                                    avg[0],
                                    avg[1],
                                    avg[2],
                                    C_confirmed);
                        }

                    }
                }
            }
            //line_list.add(0, center);
          /*  while (line_list.size() > 15) {
                line_list.remove(line_list.size() - 1);
            }*/
          /*  else {
                CanNotFind = true;
            }
            if (goalpost != null && center != null && dist_sig == false) {f
                dist_sig = true;
                distance = balldistance(goalpost, center) * 40;
                Log.i("distance", distance.toString());
            } else {
                for (int j = 1; j < line_list.size(); j++) {

                    System.out.println("center_r" + line_list.get(j));
                    int thickness = (int) (Math.sqrt(40 / ((float) (j + 1))) * 5);
                    Imgproc.line(matInput, line_list.get(j - 1), line_list.get(j), new Scalar(0, 0, 255), thickness);
                }
            }*/

        }
     /*   if (x_saver != 0 && y_saver != 0&&w_saver!=0) {
            Double[] avg=new Double[3];
            int[][] rgb=new int[2*w_saver][2* w_saver];
            int R=0,G=0,B=0;
            Double cnt=0.0;
            for(int i=0; i<2*w_saver;i++){
                for(int j=0;j<2*w_saver;j++){
                    cnt+=1;
                    rgb[i][j] = bitmap.getPixel(x_saver-w_saver+i,y_saver-w_saver+j); //원하는 좌표값 입력
                    R += Color.red(  rgb[i][j]); //red값 추출
                    G += Color.green( rgb[i][j]); //green값 추출
                    B += Color.blue(rgb[i][j]); //blue값 추출
                }
                avg[0]=R/cnt;
                avg[1]=B/cnt;
                avg[2]=G/cnt;
            }
            Log.i("iu", (avg[0])+" "+(avg[1])+" "+(avg[2]));
        }*/
        //  frame++;

        for (int j = 1; j < line_list.size(); j++) {
            // System.out.println("center_r" + line_list.get(j));
            int thickness = (int) (Math.sqrt(40 / ((float) (j + 1))) * 5);
            Imgproc.line(matInput, line_list.get(j - 1), line_list.get(j), new Scalar(0, 0, 255), thickness);
        }

        Utils.matToBitmap(matInput, bitmap);
        return bitmap;
    }

    Double balldistance(@NotNull Point goalpost, @NotNull Point center) {
        Double dist;
        Double vecX = goalpost.x - center.x;
        Double vecY = goalpost.y - center.y;
        dist = Math.sqrt(Math.pow(vecX, 2) + Math.pow(vecY, 2));
        Double result = distanceFunc(dist);
        return result;
    }

    Double distanceFunc(Double dist) {
        Double speed = 12.925 * Math.pow(dist, -1);
        return speed;
    }

    int[] trackingArea(Bitmap bitmap, int x, int y, int w) {
        int[][] rgb = new int[2 * w][2 * w];
        int R = 0, G = 0, B = 0;
        Double cnt = 0.0;
        int[] avg = new int[3];
        avg[0] = 0;
        avg[1] = 0;
        avg[2] = 0;
        for (int i = 0; i < (2 * w); i++) {
            for (int j = 0; j < (2 * w); j++) {
                cnt += 1;
                rgb[i][j] = bitmap.getPixel(x - w + i, y - w + j); //원하는 좌표값 입력
                R += Color.red(rgb[i][j]); //red값 추출
                G += Color.green(rgb[i][j]); //green값 추출
                B += Color.blue(rgb[i][j]); //blue값 추출
            }
        }
        avg[0] = (int) (R / cnt);
        avg[1] = (int) (B / cnt);
        avg[2] = (int) (G / cnt);

        Log.i("iu", (avg[0]) + " a " + (avg[1]) + " " + (avg[2]));
        return avg;
    }

    void Check_bound(Bitmap bitmap, int x, int y, int w) {
        int[] avg;
        x_LSaver = 0;
        y_LSaver = 0;
        x_MSaver = 0;
        y_MSaver = 0;
        x_RSaver = 0;
        y_RSaver = 0;
        if (x - 3 * w > 80 && y - 3 * w > 0 && x + 3 * w < 560) { //바욶드 검사
            x_LSaver = x - 2 * w;
            y_LSaver = y - 2 * w;
            avg = trackingArea(bitmap, x_LSaver, y_LSaver, w);
            R_LAvg = avg[0];
            G_LAvg = avg[1];
            B_LAvg = avg[2];
            x_MSaver = x;
            y_MSaver = y - 2 * w;
            avg = trackingArea(bitmap, x_MSaver, y_MSaver, w);
            R_MAvg = avg[0];
            G_MAvg = avg[1];
            B_MAvg = avg[2];
            x_RSaver = x + 2 * w;
            y_RSaver = y - 2 * w;
            avg = trackingArea(bitmap, x_MSaver, y_MSaver, w);
            R_RAvg = avg[0];
            G_RAvg = avg[1];
            B_RAvg = avg[2];
        } else {
            R_LAvg = 0;
            G_LAvg = 0;
            B_LAvg = 0;
            R_MAvg = 0;
            G_MAvg = 0;
            B_MAvg = 0;
            R_RAvg = 0;
            G_RAvg = 0;
            B_RAvg = 0;
            if (y - 3 * w > 0) {
                x_MSaver = x;
                y_MSaver = y - 2 * w;
                avg = trackingArea(bitmap, x_MSaver, y_MSaver, w);
                R_MAvg = avg[0];
                G_MAvg = avg[1];
                B_MAvg = avg[2];
                if (x_newSaver - 3 * w > 80) {
                    x_LSaver = x - 2 * w;
                    y_LSaver = y - 2 * w;
                    avg = trackingArea(bitmap, x_LSaver, y_LSaver, w);
                    R_LAvg = avg[0];
                    G_LAvg = avg[1];
                    B_LAvg = avg[2];
                }
                if (x_newSaver + 3 * w < 560) {
                    x_RSaver = x + 2 * w;
                    y_RSaver = y - 2 * w;
                    avg = trackingArea(bitmap, x_RSaver, y_RSaver, w);
                    R_RAvg = avg[0];
                    G_RAvg = avg[1];
                    B_RAvg = avg[2];
                }
            }
        }
    }


    void FindDifference(Bitmap bitmap, int R_avg, int G_avg, int B_avg, int confirmed){
            //if(FindCnt!=2){
                Point center;
                int R_compareVelue=Math.abs(R_old_avg-R_avg); //R_이질감 확인
                int G_compareVelue=Math.abs(B_old_avg-G_avg); //G_이질감 확인
                int B_compareVelue=Math.abs(G_old_avg-B_avg); //B_이질감 확인
                if(frame<8) {
                    if(R_compareVelue>30||G_compareVelue>15||B_compareVelue>15){ //비교
                        C_checked=true;


                        // Imgproc.circle(mat, center, 3, new Scalar(0, 0, 0), 2);
                        Check_bound(bitmap, x_newSaver,y_newSaver,w_newSaver);
                        if(confirmed==1){
                            x_newSaver=x_LSaver;
                            y_newSaver=y_LSaver;
                            R_old_avg=R_LAvg;
                            G_old_avg=G_LAvg;
                            B_old_avg=B_LAvg;
                            L_checked=true;
                            center = new Point(x_newSaver, y_newSaver);
                            line_list.add(0, center);
                        }
                        if(confirmed==2){
                            x_newSaver=x_MSaver;
                            y_newSaver=y_MSaver;
                            R_old_avg=R_MAvg;
                            G_old_avg=G_MAvg;
                            B_old_avg=B_MAvg;
                            M_checked=true;
                            center = new Point(x_newSaver, y_newSaver);
                            line_list.add(0, center);
                        }
                        if(confirmed==3){
                            x_newSaver=x_RSaver;
                            y_newSaver=y_RSaver;
                            R_old_avg=R_RAvg;
                            G_old_avg=G_RAvg;
                            B_old_avg=B_RAvg;
                            R_checked=true;
                            center = new Point(x_newSaver, y_newSaver);
                            line_list.add(0, center);
                        }
                    }
                }
                else if(frame<15)
                {
                    if(R_compareVelue>16||G_compareVelue>9||B_compareVelue>9){ //비교
                        C_checked=true;


                        // Imgproc.circle(mat, center, 3, new Scalar(0, 0, 0), 2);
                        Check_bound(bitmap, x_newSaver,y_newSaver,w_newSaver);
                        if(confirmed==1){
                            x_newSaver=x_LSaver;
                            y_newSaver=y_LSaver;
                            R_old_avg=R_LAvg;
                            G_old_avg=G_LAvg;
                            B_old_avg=B_LAvg;
                            L_checked=true;
                            center = new Point(x_newSaver, y_newSaver);
                            line_list.add(0, center);
                        }
                        if(confirmed==2){
                            x_newSaver=x_MSaver;
                            y_newSaver=y_MSaver;
                            R_old_avg=R_MAvg;
                            G_old_avg=G_MAvg;
                            B_old_avg=B_MAvg;
                            M_checked=true;
                            center = new Point(x_newSaver, y_newSaver);
                            line_list.add(0, center);
                        }
                        if(confirmed==3){
                            x_newSaver=x_RSaver;
                            y_newSaver=y_RSaver;
                            R_old_avg=R_RAvg;
                            G_old_avg=G_RAvg;
                            B_old_avg=B_RAvg;
                            R_checked=true;
                            center = new Point(x_newSaver, y_newSaver);
                            line_list.add(0, center);
                        }
                    }
                }
                else{
                    if(R_compareVelue>9|G_compareVelue>6||B_compareVelue>6){ //비교
                        C_checked=true;


                        // Imgproc.circle(mat, center, 3, new Scalar(0, 0, 0), 2);
                        Check_bound(bitmap, x_newSaver,y_newSaver,w_newSaver);
                        if(confirmed==1){
                            x_newSaver=x_LSaver;
                            y_newSaver=y_LSaver;
                            R_old_avg=R_LAvg;
                            G_old_avg=G_LAvg;
                            B_old_avg=B_LAvg;
                            L_checked=true;
                            center = new Point(x_newSaver, y_newSaver);
                            line_list.add(0, center);
                        }
                        if(confirmed==2){
                            x_newSaver=x_MSaver;
                            y_newSaver=y_MSaver;
                            R_old_avg=R_MAvg;
                            G_old_avg=G_MAvg;
                            B_old_avg=B_MAvg;
                            M_checked=true;
                            center = new Point(x_newSaver, y_newSaver);
                            line_list.add(0, center);
                        }
                        if(confirmed==3){
                            x_newSaver=x_RSaver;
                            y_newSaver=y_RSaver;
                            R_old_avg=R_RAvg;
                            G_old_avg=G_RAvg;
                            B_old_avg=B_RAvg;
                            R_checked=true;
                            center = new Point(x_newSaver, y_newSaver);
                            line_list.add(0, center);
                        }
                    }
                }
                if(C_checked==true){
                    if(R_LAvg!=0&&L_checked==false){
                        L_checked=true;
                        FindDifference(bitmap, R_LAvg, G_LAvg, B_LAvg,L_confirmed);
                    }
                    if(R_MAvg!=0&&M_checked==false){
                        M_checked=true;
                        FindDifference(bitmap, R_MAvg, G_MAvg, B_MAvg,M_confirmed);
                    }
                    if(R_RAvg!=0&&R_checked==false){
                        R_checked=true;
                        FindDifference(bitmap, R_RAvg, G_RAvg, B_RAvg,R_confirmed);
                    }
                }
           // }
            frame++;
          // FindCnt++;
          //if(FindCnt==2){
                //FindCnt=0;

          //  }
            C_checked=false;
            L_checked=false;
            M_checked=false;
            R_checked=false;
    }

}