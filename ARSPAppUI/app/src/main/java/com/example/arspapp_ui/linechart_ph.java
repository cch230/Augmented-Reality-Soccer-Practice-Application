package com.example.arspapp_ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class linechart_ph extends Fragment {


    public linechart_ph() {
    }

    private LineChart linechart_ph;
    Map<String, String> data_ph = new HashMap<String, String>();
    private RelativeLayout layout;
    ViewGroup viewGroup;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_linechart_ph, container, false);


        linechart_ph = viewGroup.findViewById(R.id.linechart_ph);


        data_ph = get_rank(getContext(), "physical");

        ArrayList<Entry> values_ph = new ArrayList<>();


        final Set ph_key = data_ph.keySet();


        Iterator iterator_ph = ph_key.iterator();

        XAxis xAxis_ph = linechart_ph.getXAxis();
        YAxis yLAxis_ph = linechart_ph.getAxisLeft();
        YAxis yRAxis_ph = linechart_ph.getAxisRight();


        int keyName_ph = 1;


        while (iterator_ph.hasNext()) {
            String keyName_sph = (String) iterator_ph.next();
            String valueName_sph = (String) data_ph.get(keyName_sph);
            //int keyName = Integer.parseInt(keyName_s);


            float valueName_ph = 0f;

            switch (valueName_sph) {
                case "X":
                    valueName_ph = 0f;
                    break;
                case "F":
                    valueName_ph = 10f;
                    break;
                case "E":
                    valueName_ph = 20f;
                    break;
                case "D":
                    valueName_ph = 30f;
                    break;
                case "C":
                    valueName_ph = 40f;
                    break;
                case "B":
                    valueName_ph = 50f;
                    break;
                case "A":
                    valueName_ph = 60f;
                default:
                    System.out.println("스위치문 오류");
            }

            values_ph.add(new Entry(keyName_ph, valueName_ph));
            keyName_ph++;


            LineDataSet set_ph;

            set_ph = new LineDataSet(values_ph, "최근 30번 피지컬 결과");

            ArrayList<ILineDataSet> dataSets_ph = new ArrayList<>();


            dataSets_ph.add(set_ph); // add the data sets // create a data object with the data sets


            LineData data_ph = new LineData(dataSets_ph); // black lines and points


            set_ph.setColor(Color.BLUE);


            linechart_ph.setBackgroundColor(Color.WHITE); // 그래프 배경 색 설정


            set_ph.setDrawFilled(true); // 차트 아래 fill(채우기) 설정


            set_ph.setFillColor(Color.BLUE); // 차트 아래 채우기 색 설정


            set_ph.setCircleColor(Color.parseColor("BLUE"));


            xAxis_ph.setPosition(XAxis.XAxisPosition.TOP);


            xAxis_ph.setDrawGridLines(false);


            xAxis_ph.setAxisMinimum(0);
            xAxis_ph.setAxisMaximum(30);


            xAxis_ph.setLabelCount(30, true);


            yLAxis_ph.setAxisMaximum(60);
            yLAxis_ph.setAxisMinimum(0);


            xAxis_ph.setTextColor(Color.BLACK);

            // xAxis_ph.setDrawLabels(false);
            xAxis_ph.setDrawAxisLine(false);
            xAxis_ph.setDrawGridLines(false);


            yRAxis_ph.setEnabled(false);


            linechart_ph.setAutoScaleMinMaxEnabled(true);
            linechart_ph.setData(data_ph);




        }

        return viewGroup;
    }












    Map<String,String> get_rank(Context context, String train_sort) {
        int i=0;
        Map<String,String> map = new Map<String, String>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean containsKey(@Nullable Object key) {
                return false;
            }

            @Override
            public boolean containsValue(@Nullable Object value) {
                return false;
            }

            @Nullable
            @Override
            public String get(@Nullable Object key) {
                return null;
            }

            @Nullable
            @Override
            public String put(@NonNull String key, @NonNull String value) {
                return null;
            }

            @Nullable
            @Override
            public String remove(@Nullable Object key) {
                return null;
            }

            @Override
            public void putAll(@NonNull Map<? extends String, ? extends String> m) {

            }

            @Override
            public void clear() {

            }

            @NonNull
            @Override
            public Set<String> keySet() {
                return null;
            }

            @NonNull
            @Override
            public Collection<String> values() {
                return null;
            }

            @NonNull
            @Override
            public Set<Entry<String, String>> entrySet() {
                return null;
            }
        };


        if(train_sort.equals("shooting")){
            SharedPreferences prefs = context.getSharedPreferences("shooting", 0);
            if(prefs.getAll().size()>30){
                Map<String,String> prefrences= (Map<String, String>) prefs.getAll();
                Iterator<String> iterator = prefrences.keySet().iterator();
                while (iterator.hasNext()&&i<30){
                    String str_time=(String) iterator.next();
                    String grade=prefrences.get(str_time);
                    map.put(str_time,grade);
                    i++;
                }
                return map;
            }
            else{
                Map<String,String> prefrences= (Map<String, String>) prefs.getAll();
                return prefrences;
            }
        }
        else if(train_sort.equals("trapping")){
            SharedPreferences prefs = context.getSharedPreferences("trapping", 0);
            if(prefs.getAll().size()>30){
                Map<String,String> prefrences= (Map<String, String>) prefs.getAll();
                Iterator<String> iterator = prefrences.keySet().iterator();
                while (iterator.hasNext()&&i<30){
                    String str_time=(String) iterator.next();
                    String grade=prefrences.get(str_time);
                    map.put(str_time,grade);
                    i++;
                }
                return map;
            }
            else{
                Map<String,String> prefrences= (Map<String, String>) prefs.getAll();
                map=null;
                return prefrences;
            }
        }
        else if(train_sort.equals("quick")){
            SharedPreferences prefs = context.getSharedPreferences("quick", 0);
            if(prefs.getAll().size()>30){
                Map<String,String> prefrences= (Map<String, String>) prefs.getAll();
                Iterator<String> iterator = prefrences.keySet().iterator();
                while (iterator.hasNext()&&i<30){
                    String str_time=(String) iterator.next();
                    String grade=prefrences.get(str_time);
                    map.put(str_time,grade);
                    i++;
                }
                return map;
            }
            else{
                Map<String,String> prefrences= (Map<String, String>) prefs.getAll();
                map=null;
                return prefrences;
            }
        }
        else{
            SharedPreferences prefs = context.getSharedPreferences("physical", 0);
            if(prefs.getAll().size()>30){
                Map<String,String> prefrences= (Map<String, String>) prefs.getAll();
                Iterator<String> iterator = prefrences.keySet().iterator();
                while (iterator.hasNext()&&i<30){
                    String str_time=(String) iterator.next();
                    String grade=prefrences.get(str_time);
                    map.put(str_time,grade);
                    i++;
                }
                return map;
            }
            else{
                Map<String,String> prefrences= (Map<String, String>) prefs.getAll();
                map=null;
                return prefrences;
            }
        }
    }
}
