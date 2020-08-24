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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class linechart_pi extends Fragment {

    public linechart_pi() {
    }

    PieChart piechart;
    Map<String, String> data_ph = new HashMap<String, String>();
    Map<String, String> data_sp = new HashMap<String, String>();
    Map<String, String> data_sh = new HashMap<String, String>();
    Map<String, String> data_tr = new HashMap<String, String>();
    int[] colorArray  = new int[]{Color.BLUE, Color.GREEN, Color.RED, Color.MAGENTA};
    ViewGroup viewGroup;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup  = (ViewGroup) inflater.inflate(R.layout.activity_linechart_pi, container, false);

        piechart = viewGroup.findViewById(R.id.linechart_pi);

        data_ph = get_rank(getContext(),"physical");
        data_sp = get_rank(getContext(),"quick");
        data_sh = get_rank(getContext(),"shooting");
        data_tr = get_rank(getContext(),"trapping");

        final Set ph_key = data_ph.keySet();
        final Set qu_key = data_ph.keySet();
        final Set sh_key = data_sh.keySet();
        final Set tr_key = data_tr.keySet();









        piechart.setUsePercentValues(true);
        piechart.setHoleColor(Color.WHITE);

        piechart.setUsePercentValues(true);
        piechart.setExtraOffsets(5,10,5,5);
        //piechart.setTransparentCircleRadius(61f);
        // piechart.setHoleRadius(30);
        // piechart.setDragDecelerationFrictionCoef(0.95f);

        ArrayList<PieEntry> pieValues = new ArrayList<PieEntry>();
        float ph_size = ph_key.size();
        float qu_size = qu_key.size();
        float sh_size = sh_key.size();
        float tr_size = tr_key.size();

        float size_sum = ph_size + sh_size+ tr_size + qu_size;

        float ph_f = ph_size/size_sum*100;
        float sh_f = sh_size/size_sum*100;
        float tr_f = tr_size/size_sum*100;
        float qu_f = qu_size/size_sum*100;



        if(ph_f != 0) {
            pieValues.add(new PieEntry(ph_f, "피지컬"));
        }

        if (qu_f != 0) {
            pieValues.add(new PieEntry(qu_f,"순발력"));
        }

        if (sh_f != 0) {
            pieValues.add(new PieEntry(sh_f,"슈팅"));
        }


        if (tr_f != 0) {
            pieValues.add(new PieEntry(tr_f,"트래핑"));
        }






        Description description = new Description();
        description.setText("단위 (%)"); //라벨
        description.setTextSize(20);
        piechart.setDescription(description);


        PieDataSet dataSet = new PieDataSet(pieValues,"train");
        dataSet.setColors(colorArray);
        dataSet.setSliceSpace(5f);
        dataSet.setSelectionShift(5f);


        PieData data = new PieData((dataSet));
        data.setValueTextSize(10);
        data.setValueTextColor(Color.BLACK);
        piechart.setDrawEntryLabels(true);
        int colorBlack = Color.parseColor("BLACK");
        piechart.setEntryLabelColor(colorBlack);
        piechart.setEntryLabelTextSize(15);
        piechart.setDrawHoleEnabled(false);

        piechart.setData(data);



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
