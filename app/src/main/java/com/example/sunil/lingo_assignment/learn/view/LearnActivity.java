package com.example.sunil.lingo_assignment.learn.view;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.sunil.lingo_assignment.learn.model.LearnModel;
import com.example.sunil.lingo_assignment.learn.presenter.LearnPresenter;
import com.example.sunil.lingo_assignment.learn.MVP_Learn;
import com.example.sunil.lingo_assignment.R;
import com.example.sunil.lingo_assignment.data.Lesson;

import java.io.File;

public class LearnActivity extends AppCompatActivity implements MVP_Learn.RequiredViewOps {

    public static final String TAG = "LearnActivity";

    TextView conceptNameTextView,pronounciationTextView,targetScriptTextView;
    FloatingActionButton playFab,nextFab;
    Toolbar toolbar;
    String type,concept,fileName;
    private MVP_Learn.ProvidedPresenterOps mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        initViews();
        setupMVP();

        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.clickNextFab(concept);
            }
        });


        playFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getExternalFilesDir(null) + File.separator + fileName; // your URL here
                mPresenter.clickPlayFab(url);
            }
        });
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

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        conceptNameTextView = findViewById(R.id.concept_name_tv);
        pronounciationTextView = findViewById(R.id.pronounciation_tv);
        targetScriptTextView = findViewById(R.id.target_script_tv);
        playFab = findViewById(R.id.play_fab);
        nextFab = findViewById(R.id.next_fab);
    }

    private void setupMVP() {
            LearnPresenter presenter = new LearnPresenter(this);
            LearnModel model = new LearnModel(presenter);
            presenter.setModel(model);
            mPresenter = presenter;
    }


}
