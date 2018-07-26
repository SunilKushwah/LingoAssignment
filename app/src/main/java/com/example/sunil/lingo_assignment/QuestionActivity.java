package com.example.sunil.lingo_assignment;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunil.lingo_assignment.R;
import com.example.sunil.lingo_assignment.model.DataManager;
import com.example.sunil.lingo_assignment.model.LessonAndStatus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuestionActivity extends AppCompatActivity {

    public static final String TAG = "QuestionActivity";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    DataManager dataManager;
    List<LessonAndStatus> lessons;
    TextView conceptNameTextView,pronounciationTextView,targetScriptTextView;
    FloatingActionButton playFab,nextFab,micFab;
    Toolbar toolbar;
    String type,concept,fileName;
    LessonAndStatus currentLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        checkPermission();
        dataManager = DataManager.newInstance(this);
        initViews();
        setLessonData();

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
        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QuestionActivity.this,LearnActivity.class);
                startActivity(intent);
            }
        });
        micFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });
    }

    private void setLessonData() {
        concept = getIntent().getStringExtra("Concept");
        lessons = dataManager.getLessonList();
        for(int i=0; i<lessons.size();i++) {
            if (lessons.get(i).isCompleted() == false &&
                    lessons.get(i).getLesson().getConceptName().equals(concept)&&
                    lessons.get(i).getLesson().getType().equals("question")) {
                currentLesson = lessons.get(i);
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

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d(TAG,"pronounciation :"+pronounciationTextView.getText().toString());
                    Log.d(TAG,"speech :"+result.get(0));
                    if(pronounciationTextView.getText().toString().equalsIgnoreCase(result.get(0))){
                        Toast.makeText(this,"Matched",Toast.LENGTH_SHORT).show();
                        currentLesson.setCompleted(true);
                    }else{
                        Toast.makeText(this," Not Matched",Toast.LENGTH_SHORT).show();
                    }
                }
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
        micFab = findViewById(R.id.mic_fab);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

}
