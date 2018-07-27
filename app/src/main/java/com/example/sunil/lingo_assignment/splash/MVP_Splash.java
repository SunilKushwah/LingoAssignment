package com.example.sunil.lingo_assignment.splash;

import android.content.Context;

import com.example.sunil.lingo_assignment.data.LessonAndStatus;

import java.util.List;

public interface MVP_Splash {
    interface RequiredViewOps {
        // View operations permitted to Presenter
        Context getAppContext();
        Context getActivityContext();
    }


    interface ProvidedPresenterOps {
        // Presenter operations permitted to View
        void getLessonData();
        void downLoadAudio();
        void registerBroadcastReceiver();
    }

    interface RequiredPresenterOps {
        // Presenter operations permitted to Model
        Context getAppContext();
        Context getActivityContext();
    }

    interface ProvidedModelOps {
        // Model operations permitted to Presenter
        void saveLessonData(List<LessonAndStatus> lessons);
    }
}
