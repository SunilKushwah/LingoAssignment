package com.example.sunil.lingo_assignment.splash.model;

import com.example.sunil.lingo_assignment.data.DataManager;
import com.example.sunil.lingo_assignment.data.LessonAndStatus;
import com.example.sunil.lingo_assignment.splash.MVP_Splash;

import java.util.List;

public class SplashModel implements MVP_Splash.ProvidedModelOps{
    // Presenter reference
    private MVP_Splash.RequiredPresenterOps mPresenter;
    private DataManager mDataManager;


    public SplashModel(MVP_Splash.RequiredPresenterOps presenter) {
        this.mPresenter = presenter;
        mDataManager =DataManager.newInstance( mPresenter.getAppContext() );
    }

    @Override
    public void saveLessonData(List<LessonAndStatus> lessons) {
        mDataManager.saveLessonList(lessons);
    }
}

