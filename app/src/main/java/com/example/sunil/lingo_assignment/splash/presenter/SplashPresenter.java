package com.example.sunil.lingo_assignment.splash.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.sunil.lingo_assignment.ApiRequest;
import com.example.sunil.lingo_assignment.Download;
import com.example.sunil.lingo_assignment.DownloadService;
import com.example.sunil.lingo_assignment.learn.view.LearnActivity;
import com.example.sunil.lingo_assignment.data.Lesson;
import com.example.sunil.lingo_assignment.data.LessonAndStatus;
import com.example.sunil.lingo_assignment.data.LessonList;
import com.example.sunil.lingo_assignment.splash.MVP_Splash;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashPresenter implements MVP_Splash.ProvidedPresenterOps, MVP_Splash.RequiredPresenterOps {
    private WeakReference<MVP_Splash.RequiredViewOps> mView;
    private MVP_Splash.ProvidedModelOps mModel;
    ArrayList<String> urlList = new ArrayList<>();
    ArrayList<String> concept = new ArrayList<>();
    ArrayList<String> lessonType = new ArrayList<>();
    public static final String MESSAGE_PROGRESS = "downloading....";
    private static final int PERMISSION_REQUEST_CODE = 1;

    public SplashPresenter(MVP_Splash.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    private MVP_Splash.RequiredViewOps getView() throws NullPointerException{
        if ( mView != null )
            return mView.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    public void setModel(MVP_Splash.ProvidedModelOps model) {
        mModel = model;
        registerBroadcastReceiver();
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
    public void getLessonData() {
        getLessonDataApiCall();
    }

    @Override
    public void downLoadAudio() {
        startDownload();
    }

    @Override
    public void registerBroadcastReceiver() {
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(getActivityContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void getLessonDataApiCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRequest.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRequest api = retrofit.create(ApiRequest.class);

        Call<LessonList> call = api.getLessonList();

        call.enqueue(new Callback<LessonList>() {
            @Override
            public void onResponse(Call<LessonList> call, Response<LessonList> response) {
                Log.d("Response", response.toString());
                LessonList lessonList = response.body();
                List<Lesson> lessons = lessonList.getLessons();
                List<LessonAndStatus> lessonAndStatus = new ArrayList<>();
                for (int i = 0; i < lessons.size(); i++) {
                    lessonAndStatus.add(new LessonAndStatus(lessons.get(i),false));
                    String type = lessons.get(i).getType();
                    String conceptName = lessons.get(i).getConceptName();
                    String pronunciation = lessons.get(i).getPronunciation();
                    String targetScript = lessons.get(i).getTargetScript();
                    String audioUrl = lessons.get(i).getAudioUrl();
                    urlList.add(audioUrl);
                    lessonType.add(type);
                    concept.add(conceptName);
                    Log.d("Result", type + " " + conceptName + " " + pronunciation + " " + targetScript + " " + audioUrl);
                }
                mModel.saveLessonData(lessonAndStatus);
                if(startAudioDownLoad()){
                    downLoadAudio();
                }else{
                    requestPermission();
                }
            }

            @Override
            public void onFailure(Call<LessonList> call, Throwable t) {
                Log.d("Error", t.getMessage());
                Toast.makeText(getAppContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        });
    }

    public boolean startAudioDownLoad() {
        if(checkPermission()){
            return true;

        } else {
            return false;
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivityContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(((Activity)(getActivityContext())),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

    }

    private void startDownload(){
        Intent intent = new Intent(getActivityContext(),DownloadService.class);
        intent.putStringArrayListExtra("UrlList",urlList);
        intent.putStringArrayListExtra("LessonType", lessonType);
        intent.putStringArrayListExtra("Concept", concept);
        getActivityContext().startService(intent);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(MESSAGE_PROGRESS)){

                Download download = intent.getParcelableExtra("download");
                if(download.getProgress() == 100){
                    Toast.makeText(getAppContext(),"File Download Complete",Toast.LENGTH_LONG).show();
                    callLearnActivity();
                }
            }
        }
    };
    private void callLearnActivity() {
        Intent intent = new Intent(getActivityContext(), LearnActivity.class);
        getActivityContext().startActivity(intent);
        ((Activity)(getActivityContext())).finish();
    }
}

