package com.example.sunil.lingo_assignment.question.view;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunil.lingo_assignment.question.MVP_Question;
import com.example.sunil.lingo_assignment.question.model.QuestionModel;
import com.example.sunil.lingo_assignment.question.presenter.QuestionPresenter;
import com.example.sunil.lingo_assignment.R;
import com.example.sunil.lingo_assignment.data.Lesson;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class QuestionActivity extends AppCompatActivity implements MVP_Question.RequiredViewOps{

    public static final String TAG = "QuestionActivity";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    TextView conceptNameTextView,pronounciationTextView,targetScriptTextView;
    FloatingActionButton playFab,nextFab,micFab;
    Toolbar toolbar;
    String type,concept,fileName;
    int currentLessonIndex;
    private MVP_Question.ProvidedPresenterOps mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        checkPermission();
        initViews();
        setupMVP();

        playFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getExternalFilesDir(null) + File.separator + fileName; // your URL here
                mPresenter.clickPlayFab(url);
            }
        });
        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.clickNextFab(concept);
            }
        });
        micFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.clickMicFab();
            }
        });
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
                        nextFab.show();
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
        nextFab.show();
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

    private void setupMVP() {
        QuestionPresenter presenter = new QuestionPresenter(this);
        QuestionModel model = new QuestionModel(presenter);
        presenter.setModel(model);
        mPresenter = presenter;
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void setLessonData(Lesson lesson) {
        conceptNameTextView.setText(lesson.getConceptName());
        pronounciationTextView.setText(lesson.getPronunciation());
        targetScriptTextView.setText(lesson.getTargetScript());
        type = lesson.getType();
        concept = lesson.getConceptName();
        fileName = type + "_lesson_"+concept+".aac";
    }

    @Override
    public void promptSpeech() {
        promptSpeechInput();
    }
}
