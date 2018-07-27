package com.example.sunil.lingo_assignment.splash.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sunil.lingo_assignment.data.DataManager;
import com.example.sunil.lingo_assignment.splash.MVP_Splash;
import com.example.sunil.lingo_assignment.splash.model.SplashModel;
import com.example.sunil.lingo_assignment.splash.presenter.SplashPresenter;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity implements MVP_Splash.RequiredViewOps{
    public static final String TAG = "SplashActivity";

    public static final String MESSAGE_PROGRESS = "downloading....";
    private static final int PERMISSION_REQUEST_CODE = 1;
    ProgressBar mProgressBar;
    ArrayList<String> urlList = new ArrayList<>();
    ArrayList<String> concept = new ArrayList<>();
    ArrayList<String> lessonType = new ArrayList<>();
    DataManager dataManager;
    private MVP_Splash.ProvidedPresenterOps mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // registerReceiver();
        setupMVP();
    }

    /*private void getLessonData() {

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
                dataManager.saveLessonList(lessonAndStatus);
                downloadFile();
            }

            @Override
            public void onFailure(Call<LessonList> call, Throwable t) {
                Log.d("Error", t.getMessage());
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        });
    }*/
   /* public void downloadFile(){

        if(checkPermission()){
            mPresenter.downLoadAudio();
           //startDownload();
        } else {
            requestPermission();
        }
    }

    private void registerReceiver(){

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(MESSAGE_PROGRESS)){

                Download download = intent.getParcelableExtra("download");
                if(download.getProgress() == 100){
                    Toast.makeText(getApplicationContext(),"File Download Complete",Toast.LENGTH_LONG).show();
                    callLearnActivity();
                }
            }
        }
    };
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.downLoadAudio();
                    //startDownload();
                } else {
                    Toast.makeText(this,"Permission Denied, Please allow to proceed !",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /*private void callLearnActivity() {
        Intent intent = new Intent(SplashActivity.this, LearnActivity.class);
        startActivity(intent);
        finish();
    }*/


    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    private void setupMVP() {
        SplashPresenter presenter = new SplashPresenter(this);
        SplashModel model = new SplashModel(presenter);
        presenter.setModel(model);
        mPresenter = presenter;
        mPresenter.getLessonData();
    }


}
