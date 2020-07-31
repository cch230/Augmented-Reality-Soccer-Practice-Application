package com.example.arspapp_ui;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class point {

    private org.opencv.core.Point[] keypoint=null;
    private org.opencv.core.Point[] startpoint=null;
    private org.opencv.core.Point[] stoppoint=null;
    public String filename;
    private String f_name;
    private ArrayList<org.opencv.core.Point> opencv_list_point=new ArrayList<org.opencv.core.Point>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void givebitmap(Bitmap bitmap, ArrayList<Point> keylist, ArrayList<Point> startlist, ArrayList<Point> stoplist, File filedir){
        Mat matInput = new Mat();

        Utils.bitmapToMat(bitmap,matInput);
        for (int j = 0; j < keylist.size(); j++) {

            keypoint= new org.opencv.core.Point[keylist.size()];
            keypoint[j]= new org.opencv.core.Point(keylist.get(j).x,keylist.get(j).y);
            Imgproc.circle(matInput,keypoint[j],1, new Scalar(255, 0, 0),2);
            Log.i("원",keypoint[j].toString());
        }
        for (int j = 0; j < startlist.size(); j++) {
            startpoint=new org.opencv.core.Point[startlist.size()];
            startpoint[j]= new org.opencv.core.Point(startlist.get(j).x,startlist.get(j).y);
            stoppoint=new org.opencv.core.Point[startlist.size()];
            stoppoint[j]= new org.opencv.core.Point(stoplist.get(j).x,stoplist.get(j).y);

            Imgproc.line(matInput,startpoint[j],stoppoint[j], new Scalar(255,0, 0), 2);
            Log.i("잇1",startpoint[j].toString());
            Log.i("잇2",stoppoint[j].toString());
        }
        Long time = System.currentTimeMillis(); //시간 받기
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        //포멧 변환  형식 만들기
        Date dd =new Date(time); //받은 시간을 Date 형식으로 바꾸기
        String strTime = sdf.format(dd); //Data 정보를 포멧 변환하기
        Utils.matToBitmap(matInput,bitmap);
        filename= saveBitmapToJpeg(bitmap,strTime,filedir);
    }
    private String saveBitmapToJpeg(Bitmap bitmap, String name, File dir) {

        //내부저장소 캐시 경로를 받아옵니다.
        File storage = dir;
        //저장할 파일 이름
        String fileName = name + " (2).jpg";

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
