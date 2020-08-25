package com.example.arspapp_ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class rank_search {

    Map<String,String> get_rank(Context context,String train_sort) {
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
                return map;
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