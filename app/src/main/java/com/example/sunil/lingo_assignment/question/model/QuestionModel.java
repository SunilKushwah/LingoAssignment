package com.example.sunil.lingo_assignment.question.model;

import com.example.sunil.lingo_assignment.question.MVP_Question;
import com.example.sunil.lingo_assignment.data.DataManager;
import com.example.sunil.lingo_assignment.data.Lesson;
import com.example.sunil.lingo_assignment.data.LessonAndStatus;

import java.util.List;

public class QuestionModel implements MVP_Question.ProvidedModelOps{
    // Presenter reference
    private MVP_Question.RequiredPresenterOps mPresenter;
    private DataManager mDataManager;
    public Lesson lesson;
    public int currentLessonIndex;
    public LessonAndStatus lessonAndStatus;
    public List<LessonAndStatus> lessons;


    public QuestionModel(MVP_Question.RequiredPresenterOps presenter) {
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

    @Override
    public int currentLessonIndex() {
        return currentLessonIndex;
    }
}

