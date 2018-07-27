package com.example.sunil.lingo_assignment.learn.model;

import com.example.sunil.lingo_assignment.learn.MVP_Learn;
import com.example.sunil.lingo_assignment.data.DataManager;
import com.example.sunil.lingo_assignment.data.Lesson;
import com.example.sunil.lingo_assignment.data.LessonAndStatus;

import java.util.List;

public class LearnModel implements MVP_Learn.ProvidedModelOps {
    // Presenter reference
    private MVP_Learn.RequiredPresenterOps mPresenter;
    private DataManager mDataManager;
    public Lesson lesson;
    public int currentLessonIndex;
    public LessonAndStatus lessonAndStatus;
    public List<LessonAndStatus> lessons;


    public LearnModel(MVP_Learn.RequiredPresenterOps presenter) {
        this.mPresenter = presenter;
        mDataManager =DataManager.newInstance( mPresenter.getAppContext() );
    }


    @Override
    public boolean loadLessonData() {
        lessons = mDataManager.getLessonList();
        return lessons != null;
    }

    @Override
    public Lesson getLesson() {
        loadLessonData();
        for(int i=0; i<lessons.size();i++) {
            if (lessons.get(i).isCompleted() == false) {
                currentLessonIndex = i;
                lesson = lessons.get(i).getLesson();
                break;
            }
        }
        return lesson;
    }

    @Override
    public void updateLessonStatus(boolean isCompleted) {
        mDataManager.updateLessonStatus(currentLessonIndex,isCompleted);
    }

    @Override
    public int getLessonsCount() {
        if ( lessons != null )
            return lessons.size();
        return 0;
    }
}
