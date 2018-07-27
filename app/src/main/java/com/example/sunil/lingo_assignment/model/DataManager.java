package com.example.sunil.lingo_assignment.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "LingoPref";

    // All Shared Preferences Keys
    public static final String KEY_LESSON_LIST = "LessonList";


    // Constructor
    public static DataManager newInstance(Context context) {
        DataManager fragment = new DataManager(context);
        return fragment;
    }
    public DataManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clearDataManagerPref(){
        editor.clear();
        editor.commit();
    }

    public void saveLessonList(List<LessonAndStatus> lessons) {
        Gson gson = new Gson();
        String json = gson.toJson(lessons);
        editor.putString(KEY_LESSON_LIST, json);
        editor.commit();
    }

    public void updateLessonStatus(int index ,boolean status){
        List<LessonAndStatus> lessons ;
        Gson gson = new Gson();
        String json = pref.getString(KEY_LESSON_LIST, "");
        if (json.isEmpty()) {
            lessons = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<LessonAndStatus>>() {
            }.getType();
            lessons = gson.fromJson(json, type);
        }
        lessons.get(index).setCompleted(status);
        saveLessonList(lessons);
    }

    public  List<LessonAndStatus> getLessonList() {
        List<LessonAndStatus> lessons ;
        Gson gson = new Gson();
        String json = pref.getString(KEY_LESSON_LIST, "");
        if (json.isEmpty()) {
            lessons = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<LessonAndStatus>>() {
            }.getType();
            lessons = gson.fromJson(json, type);
        }
        return lessons;
    }


}
