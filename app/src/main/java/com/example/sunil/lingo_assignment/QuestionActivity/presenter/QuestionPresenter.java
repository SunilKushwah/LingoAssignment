package com.example.sunil.lingo_assignment.QuestionActivity.presenter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.speech.RecognizerIntent;
import android.widget.Toast;

import com.example.sunil.lingo_assignment.LearnActivity.view.LearnActivity;
import com.example.sunil.lingo_assignment.QuestionActivity.MVP_Question;
import com.example.sunil.lingo_assignment.QuestionActivity.view.QuestionActivity;
import com.example.sunil.lingo_assignment.R;
import com.example.sunil.lingo_assignment.model.Lesson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Locale;

public class QuestionPresenter  implements MVP_Question.ProvidedPresenterOps, MVP_Question.RequiredPresenterOps{
    private WeakReference<MVP_Question.RequiredViewOps> mView;
    private MVP_Question.ProvidedModelOps mModel;

    public QuestionPresenter(MVP_Question.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    private MVP_Question.RequiredViewOps getView() throws NullPointerException{
        if ( mView != null )
            return mView.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    public void setModel(MVP_Question.ProvidedModelOps model) {
        mModel = model;
        loadData();
    }

    private void loadData() {
        getView().setLessonData(mModel.getLesson());
    }

    @Override
    public int getLessonsCount() {
        return mModel.getLessonsCount();
    }

    @Override
    public Context getAppContext() {
        try {
            return getView().getAppContext();
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public Context getActivityContext() {
        try {
            return getView().getActivityContext();
        } catch (NullPointerException e) {
            return null;
        }
    }


    @Override
    public Lesson getLessonData() {
        return mModel.getLesson();
    }

    @Override
    public void clickNextFab(String concept) {
        mModel.updateLessonStatus(true);
        Intent intent = new Intent(getActivityContext(),LearnActivity.class);
        getActivityContext().startActivity(intent);

    }

    @Override
    public void clickPlayFab(String path) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    @Override
    public void clickMicFab() {
        getView().promptSpeech();
    }
}
