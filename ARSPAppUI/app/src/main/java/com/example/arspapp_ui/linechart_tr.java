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

public class linechart_tr extends Fragment {

    public linechart_tr() {}
    private LineChart linechart_tr;
    RelativeLayout layout;
    Map<String, String> data_tr = new HashMap<String, String>();
    ViewGroup viewGroup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_linechart_tr, container, false);


        linechart_tr = viewGroup.findViewById(R.id.linechart_tr);


        data_tr = get_rank(getContext(), "trapping");

        ArrayList<Entry> values_tr = new ArrayList<>();


        final Set tr_key = data_tr.keySet();


        Iterator iterator_tr = tr_key.iterator();

        XAxis xAxis_tr = linechart_tr.getXAxis();
        YAxis yLAxis_tr = linechart_tr.getAxisLeft();
        YAxis yRAxis_tr = linechart_tr.getAxisRight();


        int keyName_ph = 1;


        while (iterator_tr.hasNext()) {
            String keyName_sph = (String) iterator_tr.next();
            String valueName_sph = (String) data_tr.get(keyName_sph);
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

            values_tr.add(new Entry(keyName_ph, valueName_ph));
            keyName_ph++;


            LineDataSet set_tr;

            set_tr = new LineDataSet(values_tr, "최근 30번 트래핑 결과");

            ArrayList<ILineDataSet> dataSets_tr = new ArrayList<>();


            dataSets_tr.add(set_tr); // add the data sets // create a data object with the data sets


            LineData data_tr = new LineData(dataSets_tr); // black lines and points


            set_tr.setColor(Color.RED);


            linechart_tr.setBackgroundColor(Color.WHITE); // 그래프 배경 색 설정


            set_tr.setDrawFilled(true); // 차트 아래 fill(채우기) 설정


            set_tr.setFillColor(Color.RED); // 차트 아래 채우기 색 설정


            set_tr.setCircleColor(Color.parseColor("RED"));


            xAxis_tr.setPosition(XAxis.XAxisPosition.TOP);


            xAxis_tr.setDrawGridLines(false);


            xAxis_tr.setAxisMinimum(0);
            xAxis_tr.setAxisMaximum(30);


            xAxis_tr.setLabelCount(30, true);


            yLAxis_tr.setAxisMaximum(60);
            yLAxis_tr.setAxisMinimum(0);


            xAxis_tr.setTextColor(Color.BLACK);

            // xAxis_ph.setDrawLabels(false);
            xAxis_tr.setDrawAxisLine(false);
            xAxis_tr.setDrawGridLines(false);


            yRAxis_tr.setEnabled(false);


            linechart_tr.setAutoScaleMinMaxEnabled(true);
            linechart_tr.setData(data_tr);




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

