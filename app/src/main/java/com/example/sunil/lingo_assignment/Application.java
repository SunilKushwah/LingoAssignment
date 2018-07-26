package com.example.sunil.lingo_assignment;

import android.content.Context;

public class Application extends android.app.Application {
    private static Context mContext;

    public Application(){
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }
}