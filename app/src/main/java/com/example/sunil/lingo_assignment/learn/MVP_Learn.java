package com.example.sunil.lingo_assignment.learn;

import android.content.Context;

import com.example.sunil.lingo_assignment.data.Lesson;

public interface MVP_Learn {

    interface RequiredViewOps {
        // View operations permitted to Presenter
        Context getAppContext();
        Context getActivityContext();
        void setLessonData(Lesson lesson);
    }


    interface ProvidedPresenterOps {
        // Presenter operations permitted to View
        int getLessonsCount();
        Lesson getLessonData();
        void clickNextFab(String concept);
        void clickPlayFab(String path);
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
    }
}
