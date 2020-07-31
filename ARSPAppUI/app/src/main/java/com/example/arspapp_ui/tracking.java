package com.example.arspapp_ui;

import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class tracking{
    public Boolean CanNotFind=false;
    private Scalar yellowLower = new Scalar(-10,100,100);
    private Scalar yellowUpper = new Scalar(10,255,255);
    private ArrayList <Point> line_list = new ArrayList<>();
    private ArrayList <Point> line_save = new ArrayList<>();
    public Double[] circleVec_save=new Double[2];
    public Double distance=0.0;
    private Mat matHue=new Mat();
    private Mat matHist=new Mat();
    private Mat matBack=new Mat();
    private boolean trackOX = true;
    private int trackingOx =0;
    private Point moveBall = new Point();
    private Rect rect;
    private int framecnt=0;
    public Mat matSave=new Mat();

    public String filename;
    private String f_name;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Bitmap trackingBall(Bitmap bitmap,File filedir){
        Mat matGaussian = new Mat();
        Mat matHsv = new Mat();
        Mat matMask = new Mat();
        Mat matMask1 = new Mat();
        Mat matCny = new Mat();
        Mat matInput = new Mat();
        Utils.bitmapToMat(bitmap,matInput);
        Utils.bitmapToMat(bitmap,matSave);
        Point center;
        framecnt++;
        Imgproc.cvtColor(matInput,matHsv,Imgproc.COLOR_RGB2HSV_FULL);
        Core.inRange(matHsv,yellowLower,yellowUpper,matMask1);
        Imgproc.threshold(matMask1,matMask1,127,255,Imgproc.THRESH_BINARY);
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE,new Size(11,11));
        Imgproc.dilate(matMask1,matMask1,element);
        Imgproc.erode(matMask1,matMask,new Mat());
        Imgproc.blur(matMask,matGaussian,new Size(3,3),new Point(2,2));
        Imgproc.Canny(matGaussian,matCny,130,210,3);


        Mat circle = new Mat();
        Rect rectB = new Rect();
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

                if (trackOX == true) {
                    moveBall=center;
                    rectB.x = (int) (center.x - radius);
                    rectB.y = (int) (center.y - radius);
                    rectB.height = (int) radius * 2;
                    rectB.width = (int) radius * 2;
                    if (rectB.width > 0 && rectB.height > 0) {
                        trackingOx = -1;
                        trackOX = false;
                    }
                }

                if (trackingOx != 0) {
                    matHue.create(matHsv.size(), matHsv.depth());
                    List<Mat> hueList = new LinkedList<Mat>();
                    List<Mat> hsvList = new LinkedList<Mat>();
                    hsvList.add(matHsv);
                    hueList.add(matHue);
                    MatOfInt ch = new MatOfInt(0, 0);
                    Core.mixChannels(hsvList, hueList, ch);
                    MatOfFloat histRange = new MatOfFloat(0, 180);
                    if (trackingOx < 0) {
                        Mat matBall = matInput.submat(rectB);
                        Imgproc.calcHist(Arrays.asList(matBall), new MatOfInt(0), new Mat(), matHist, new MatOfInt(16), histRange);
                        Core.normalize(matHist, matHist, 0, 255, Core.NORM_MINMAX);
                        rect = rectB;
                        trackingOx = 1;
                    }
                    MatOfInt ch2 = new MatOfInt(0, 1);
                    Imgproc.calcBackProject(Arrays.asList(matHue), ch2, matHist, matBack, histRange, 1);
                    Core.bitwise_and(matBack, matMask, matBack);
                    RotatedRect trackBox = Video.CamShift(matBack, rect, new TermCriteria(TermCriteria.EPS | TermCriteria.MAX_ITER, 10, 1));
                    Imgproc.ellipse(matInput, trackBox, new Scalar(0, 0, 255), 4);
                    if (rect.area() <= 1) {
                        trackingOx = 0;
                    }

                    if (center.x>0||center.y>0) {
                        line_list.add(0, center);
                        line_save.add(0, center);
                    } else {
                        if(trackBox.center.y > 0 || trackBox.center.x > 0){
                            line_list.add(0, trackBox.center);
                            line_save.add(0, trackBox.center);
                        }
                    }

                    while (line_list.size() > 10) {
                        line_list.remove(line_list.size() - 1);
                    }

                    for (int j = 1; j < line_list.size(); j++) {
                        int thickness = (int) Math.sqrt(40 / ((float) (j + 1)) * 5);
                        Imgproc.line(matInput, line_list.get(j - 1), line_list.get(j), new Scalar(0, 0, 0), thickness);
                    }
                }
            }
        }
        else {
            CanNotFind =true;
        }
        if(framecnt==61){
            for (int j = 1; j < line_save.size(); j++) {
                int thickness = (int) Math.sqrt(40 / ((float) (j + 1)) * 5);
                Imgproc.line(matSave, line_save.get(j - 1), line_save.get(j), new Scalar(0, 0, 0), thickness);
            }
            Long time = System.currentTimeMillis(); //시간 받기
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
            //포멧 변환  형식 만들기
            Date dd =new Date(time); //받은 시간을 Date 형식으로 바꾸기
            String strTime = sdf.format(dd); //Data 정보를 포멧 변환하기
            Bitmap list;
            list=bitmap.copy(Bitmap.Config.ARGB_8888,true);
            Utils.matToBitmap(matSave,list);
            filename= saveBitmapToJpeg(list,strTime,filedir);
        }
        Utils.matToBitmap(matInput,bitmap);
        return bitmap;
    }
    private String saveBitmapToJpeg(Bitmap bitmap, String name,File dir) {

        //내부저장소 캐시 경로를 받아옵니다.
        File storage = dir;

        //저장할 파일 이름
        String fileName = name + ".jpg";

        //storage 에 파일 인스턴스를 생성합니다.
        File tempFile = new File(storage, fileName);
        try {

            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            // 스트림 사용후 닫아줍니다.
            out.close();
            f_name =tempFile.getPath();
        } catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
        }
        return f_name;
    }

}