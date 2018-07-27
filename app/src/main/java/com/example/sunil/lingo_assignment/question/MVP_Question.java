package com.example.sunil.lingo_assignment.question;

import android.content.Context;

import com.example.sunil.lingo_assignment.data.Lesson;

public interface MVP_Question {

    interface RequiredViewOps {
        Context getAppContext();
        Context getActivityContext();
        void setLessonData(Lesson lesson);
        void promptSpeech();

    }

    interface ProvidedPresenterOps {
        // Presenter operations permitted to View
        int getLessonsCount();
        Lesson getLessonData();
        void clickNextFab(String concept);
        void clickPlayFab(String path);
        void clickMicFab();
    }

    interface RequiredPresenterOps {
        // Presenter operations permitted to Model
        Context getAppContext();
        Context getActivityContext();
    }

    interface ProvidedModelOps {
        // Model operations permitted to Presenter
        Lesson getLesson();
        void updateLessonStatus(boolean isCompleted);
        boolean loadLessonData();
        int getLessonsCount();
        int currentLessonIndex();
    }
}

