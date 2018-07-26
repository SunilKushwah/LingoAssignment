package com.example.sunil.lingo_assignment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunil.lingo_assignment.model.DataManager;
import com.example.sunil.lingo_assignment.model.Lesson;
import com.example.sunil.lingo_assignment.model.LessonAndStatus;
import com.example.sunil.lingo_assignment.model.LessonList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LearnActivity extends AppCompatActivity {

    public static final String TAG = "LearnActivity";
    DataManager dataManager;
    List<LessonAndStatus> lessons;
    TextView conceptNameTextView,pronounciationTextView,targetScriptTextView;
    FloatingActionButton playFab,nextFab;
    Toolbar toolbar;
    String type,concept,fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        dataManager = DataManager.newInstance(this);
        initViews();
        setLessonData();
        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LearnActivity.this,QuestionActivity.class);
                intent.putExtra("Concept",concept);
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        playFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getExternalFilesDir(null) + File.separator + fileName; // your URL here
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });
    }

    private void setLessonData() {
        lessons = dataManager.getLessonList();
        for(int i=0; i<lessons.size();i++) {
           if ((lessons.get(i).isCompleted() == false)) {
               conceptNameTextView.setText(lessons.get(i).getLesson().getConceptName());
               pronounciationTextView.setText(lessons.get(i).getLesson().getPronunciation());
               targetScriptTextView.setText(lessons.get(i).getLesson().getTargetScript());
               type = lessons.get(i).getLesson().getType();
               concept = lessons.get(i).getLesson().getConceptName();
               fileName = type + "_lesson_"+concept+".aac";
               break;
           }
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        conceptNameTextView = findViewById(R.id.concept_name_tv);
        pronounciationTextView = findViewById(R.id.pronounciation_tv);
        targetScriptTextView = findViewById(R.id.target_script_tv);
        playFab = findViewById(R.id.play_fab);
        nextFab = findViewById(R.id.next_fab);
    }

}
