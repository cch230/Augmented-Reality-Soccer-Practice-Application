package com.example.arspapp_ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class linechart_sp extends Fragment {



    Map<String, String> data_sp = new HashMap<String, String>();
    private LineChart linechart_sp;
    ViewGroup viewGroup;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_linechart_sp, container, false);





        linechart_sp = viewGroup.findViewById(R.id.linechart_sp);


        data_sp = get_rank(getContext(), "quick");

        ArrayList<Entry> values_sp = new ArrayList<>();


        final Set sp_key = data_sp.keySet();


        Iterator iterator_sp = sp_key.iterator();

        XAxis xAxis_sp = linechart_sp.getXAxis();
        YAxis yLAxis_sp = linechart_sp.getAxisLeft();
        YAxis yRAxis_sp = linechart_sp.getAxisRight();


        int keyName_ph = 1;


        while (iterator_sp.hasNext()) {
            String keyName_sph = (String) iterator_sp.next();
            String valueName_sph = (String) data_sp.get(keyName_sph);
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

            values_sp.add(new Entry(keyName_ph, valueName_ph));
            keyName_ph++;


            LineDataSet set_sp;

            set_sp = new LineDataSet(values_sp, "최근 30번 순발력 결과");

            ArrayList<ILineDataSet> dataSets_sp = new ArrayList<>();


            dataSets_sp.add(set_sp); // add the data sets // create a data object with the data sets


            LineData data_sp = new LineData(dataSets_sp); // black lines and points


            set_sp.setColor(Color.BLUE);


            linechart_sp.setBackgroundColor(Color.WHITE); // 그래프 배경 색 설정


            set_sp.setDrawFilled(true); // 차트 아래 fill(채우기) 설정


            set_sp.setFillColor(Color.BLUE); // 차트 아래 채우기 색 설정


            set_sp.setCircleColor(Color.parseColor("BLUE"));


            xAxis_sp.setPosition(XAxis.XAxisPosition.TOP);


            xAxis_sp.setDrawGridLines(false);


            xAxis_sp.setAxisMinimum(0);
            xAxis_sp.setAxisMaximum(30);


            xAxis_sp.setLabelCount(30, true);


            yLAxis_sp.setAxisMaximum(60);
            yLAxis_sp.setAxisMinimum(0);


            xAxis_sp.setTextColor(Color.BLACK);

            // xAxis_ph.setDrawLabels(false);
            xAxis_sp.setDrawAxisLine(false);
            xAxis_sp.setDrawGridLines(false);


            yRAxis_sp.setEnabled(false);


            linechart_sp.setAutoScaleMinMaxEnabled(true);
            linechart_sp.setData(data_sp);


        }
        return viewGroup;
    }


    Map<String, String> get_rank(Context context, String train_sort) {
        int i = 0;
        Map<String, String> map = new Map<String, String>() {
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


        if (train_sort.equals("shooting")) {
            SharedPreferences prefs = context.getSharedPreferences("shooting", 0);
            if (prefs.getAll().size() > 30) {
                Map<String, String> prefrences = (Map<String, String>) prefs.getAll();
                Iterator<String> iterator = prefrences.keySet().iterator();
                while (iterator.hasNext() && i < 30) {
                    String str_time = (String) iterator.next();
                    String grade = prefrences.get(str_time);
                    map.put(str_time, grade);
                    i++;
                }
                return map;
            } else {
                Map<String, String> prefrences = (Map<String, String>) prefs.getAll();
                return prefrences;
            }
        } else if (train_sort.equals("trapping")) {
            SharedPreferences prefs = context.getSharedPreferences("trapping", 0);
            if (prefs.getAll().size() > 30) {
                Map<String, String> prefrences = (Map<String, String>) prefs.getAll();
                Iterator<String> iterator = prefrences.keySet().iterator();
                while (iterator.hasNext() && i < 30) {
                    String str_time = (String) iterator.next();
                    String grade = prefrences.get(str_time);
                    map.put(str_time, grade);
                    i++;
                }
                return map;
            } else {
                Map<String, String> prefrences = (Map<String, String>) prefs.getAll();
                map = null;
                return prefrences;
            }
        } else if (train_sort.equals("quick")) {
            SharedPreferences prefs = context.getSharedPreferences("quick", 0);
            if (prefs.getAll().size() > 30) {
                Map<String, String> prefrences = (Map<String, String>) prefs.getAll();
                Iterator<String> iterator = prefrences.keySet().iterator();
                while (iterator.hasNext() && i < 30) {
                    String str_time = (String) iterator.next();
                    String grade = prefrences.get(str_time);
                    map.put(str_time, grade);
                    i++;
                }
                return map;
            } else {
                Map<String, String> prefrences = (Map<String, String>) prefs.getAll();
                map = null;
                return prefrences;
            }
        } else {
            SharedPreferences prefs = context.getSharedPreferences("physical", 0);
            if (prefs.getAll().size() > 30) {
                Map<String, String> prefrences = (Map<String, String>) prefs.getAll();
                Iterator<String> iterator = prefrences.keySet().iterator();
                while (iterator.hasNext() && i < 30) {
                    String str_time = (String) iterator.next();
                    String grade = prefrences.get(str_time);
                    map.put(str_time, grade);
                    i++;
                }
                return map;
            } else {
                Map<String, String> prefrences = (Map<String, String>) prefs.getAll();
                map = null;
                return prefrences;
            }
        }
    }

}

