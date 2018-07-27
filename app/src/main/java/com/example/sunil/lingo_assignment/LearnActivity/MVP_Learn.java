package com.example.sunil.lingo_assignment.LearnActivity;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.sunil.lingo_assignment.model.Lesson;

public interface MVP_Learn {

    /**
     * Required View methods available to Presenter.
     * A passive layer, responsible to show data
     * and receive user interactions
     */
    interface RequiredViewOps {
        // View operations permitted to Presenter
        Context getAppContext();
        Context getActivityContext();
        void setLessonData(Lesson lesson);
       /* void notifyItemInserted(int layoutPosition);
        void notifyItemRangeChanged(int positionStart, int itemCount);*/
    }

    /**
     * Operations offered to View to communicate with Presenter.
     * Processes user interactions, sends data requests to Model, etc.
     */
    interface ProvidedPresenterOps {
        // Presenter operations permitted to View
        int getLessonsCount();
        Lesson getLessonData();
        void clickNextFab(String concept);
        void clickPlayFab(String path);
    }

    /**
     * Required Presenter methods available to Model.
     */
    interface RequiredPresenterOps {
        // Presenter operations permitted to Model
        Context getAppContext();
        Context getActivityContext();
    }

    /**
     * Operations offered to Model to communicate with Presenter
     * Handles all data business logic.
     */
    interface ProvidedModelOps {
        // Model operations permitted to Presenter
        Lesson getLesson();
        void updateLessonStatus(boolean isCompleted);
        boolean loadLessonData();
        int getLessonsCount();
    }
}
