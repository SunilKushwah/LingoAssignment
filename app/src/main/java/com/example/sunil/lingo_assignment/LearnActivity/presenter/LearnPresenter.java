package com.example.sunil.lingo_assignment.LearnActivity.presenter;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.example.sunil.lingo_assignment.LearnActivity.MVP_Learn;
import com.example.sunil.lingo_assignment.QuestionActivity.view.QuestionActivity;
import com.example.sunil.lingo_assignment.model.Lesson;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class LearnPresenter implements MVP_Learn.ProvidedPresenterOps, MVP_Learn.RequiredPresenterOps{


    private WeakReference<MVP_Learn.RequiredViewOps> mView;
    private MVP_Learn.ProvidedModelOps mModel;

    public LearnPresenter(MVP_Learn.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    private MVP_Learn.RequiredViewOps getView() throws NullPointerException{
        if ( mView != null )
            return mView.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    public void setModel(MVP_Learn.ProvidedModelOps model) {
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
        Intent intent = new Intent(getActivityContext(),QuestionActivity.class);
        intent.putExtra("Concept",concept);
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
}
