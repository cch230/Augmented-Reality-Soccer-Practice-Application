package com.example.arspapp_ui;
import android.graphics.Bitmap;
import android.os.SystemClock;

import androidx.core.graphics.RectKt;

import org.jetbrains.annotations.NotNull;
import org.opencv.android.Utils;
import org.opencv.core.Core;
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

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class tracking{
    public Boolean CanNotFind=false;

    private Mat matResult;
    private Scalar yellowLower = new Scalar(20,90,140);
    private Scalar yellowUpper = new Scalar(125,255,255);
    private Scalar yellowLower1 = new Scalar(50,0,30);
    private Scalar yellowUpper1 = new Scalar(130,100,200);

    private ArrayList <Point> line_list = new ArrayList<>();
    public Double[] circleVec_save=new Double[2];
    public Double distance=0.0;

    private Mat matHue=new Mat();
    private Mat matHist=new Mat();
    private Mat matBack=new Mat();
    private boolean trackOX = true;
    private int trackingOx =0;
    private Point moveBall = new Point();
    private Rect rect;

    private Scalar red = new Scalar(255,0,0);
    private Scalar whiteLower = new Scalar(0,0,180);
    private Scalar whiteUpper = new Scalar(255,30,255);

    private List<MatOfPoint> contours = new ArrayList<>();
    private Rect rectGoals;

    public Bitmap trackingBall(Bitmap bitmap){
        Mat matGaussian = new Mat();
        Mat matHsv = new Mat();
        Mat matMask = new Mat();
        Mat matMask1 = new Mat();
        Mat matMask2 = new Mat();
        Mat matCny = new Mat();
        Mat matInput = new Mat();
        Utils.bitmapToMat(bitmap,matInput);

        Point center=null;


        Imgproc.cvtColor(matInput,matHsv,Imgproc.COLOR_RGB2HSV_FULL);
        Core.inRange(matHsv,yellowLower,yellowUpper,matMask1);
        Imgproc.threshold(matMask1,matMask1,127,255,Imgproc.THRESH_BINARY);
        /*Core.inRange(matHsv,yellowLower1,yellowUpper1,matMask2);
        Imgproc.threshold(matMask2,matMask2,127,255,Imgproc.THRESH_BINARY);
        Core.add(matMask1,matMask2,matMask);*/
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
                    } else {
                        if(trackBox.center.y > 0 || trackBox.center.x > 0)
                            line_list.add(0, trackBox.center);
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

        Utils.matToBitmap(matInput,bitmap);
        return bitmap;
    }

    public Point ballCenter(){
        return moveBall;
    }

    public Bitmap trackingPost(Bitmap bitmap) {
        Mat matHsvGoals = new Mat();
        Mat matDilatedMaskGoals = new Mat();
        Mat matDilatedMaskGoals2 = new Mat();
        Mat matDilatedMaskGoals3 = new Mat();
        Mat matDilatedMaskGoals4 = new Mat();
        Mat matContourGoals = new Mat();
        Mat kernelGoals = Mat.ones(5, 5, 0);
        Mat matMaskGoals = new Mat();
        Mat hierarchyGoals = new Mat();
        Mat matInputGoals = new Mat();
        Utils.bitmapToMat(bitmap, matInputGoals);
        Mat input = new Mat();
        Utils.bitmapToMat(bitmap, input);



        Imgproc.cvtColor(input, matHsvGoals, Imgproc.COLOR_RGB2HSV_FULL);
        Core.inRange(matHsvGoals, whiteLower, whiteUpper, matMaskGoals);
        Imgproc.dilate(matMaskGoals, matDilatedMaskGoals, new Mat());
        Imgproc.dilate(matDilatedMaskGoals, matDilatedMaskGoals2, new Mat());
        Imgproc.dilate(matDilatedMaskGoals2, matDilatedMaskGoals3, new Mat());
        Imgproc.dilate(matDilatedMaskGoals3, matDilatedMaskGoals4, new Mat());
        Imgproc.morphologyEx(matDilatedMaskGoals4, matContourGoals, Imgproc.MORPH_CLOSE, kernelGoals);

        Imgproc.findContours(matContourGoals, contours, hierarchyGoals, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

        if (contours.size() > 0) {
            rectGoals = Imgproc.boundingRect(contours.get(contours.size() - 1));
            if (rectGoals.x != 0 && rectGoals.y != 0 && rectGoals.width > 2 * rectGoals.height && 3 * rectGoals.height > rectGoals.width) {
                Imgproc.rectangle(matInputGoals, rectGoals.tl(), rectGoals.br(), red, 3);
            }
        }
        Utils.matToBitmap(matInputGoals, bitmap);
        return bitmap;

    }

    public Bitmap finishTrack(Bitmap bitmap){
        if(matResult==null) {
            Mat matFinish = new Mat();
            Utils.bitmapToMat(bitmap, matFinish);
            matResult = new Mat(matFinish.rows(), matFinish.cols(), matFinish.type());
            matFinish.copyTo(matResult);
            for(int i=1;i<line_list.size();i++) {
                Imgproc.line(matResult, line_list.get(i-1), line_list.get(i), new Scalar(0, 0, 0), 3);
            }
        }
        Utils.matToBitmap(matResult, bitmap);
        return bitmap;
    }


}