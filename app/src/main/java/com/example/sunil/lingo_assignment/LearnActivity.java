package com.example.sunil.lingo_assignment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sunil.lingo_assignment.model.Lesson;
import com.example.sunil.lingo_assignment.model.LessonList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LearnActivity extends AppCompatActivity {

    public static final String TAG = "LearnActivity";
    public static final String MESSAGE_PROGRESS = "downloading....";
    private static final int PERMISSION_REQUEST_CODE = 1;
    ProgressBar mProgressBar;
    ArrayList<String> urlList = new ArrayList<>();
    ArrayList<String> concept = new ArrayList<>();
    ArrayList<String> lessonType = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = findViewById(R.id.progress);
        registerReceiver();
        FloatingActionButton nextFab = findViewById(R.id.next_fab);
        nextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLessonData();

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton playFab = findViewById(R.id.play_fab);
        playFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ""; // your URL here
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare(); // might take long! (for buffering, etc)
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
            }
        });
    }


    private void getLessonData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRequest.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRequest api = retrofit.create(ApiRequest.class);

        Call<LessonList> call = api.getLessonList();

        call.enqueue(new Callback<LessonList>() {
            @Override
            public void onResponse(Call<LessonList> call, Response<LessonList> response) {
                Log.d("Response",response.toString());
                LessonList lessonList = response.body();
                List<Lesson> lessons = lessonList.getLessons();
                //String[] heroes = new String[lessonList.size()];
                for (int i = 0; i < lessons.size(); i++) {
                    String type = lessons.get(i).getType();
                    String conceptName = lessons.get(i).getConceptName();
                    String pronunciation = lessons.get(i).getPronunciation();
                    String targetScript = lessons.get(i).getTargetScript();
                    String audioUrl = lessons.get(i).getAudioUrl();
                    urlList.add(audioUrl);
                    lessonType.add(type);
                    concept.add(conceptName);
                    Log.d("Result",type+" "+conceptName+" "+pronunciation+" "+targetScript+" "+audioUrl);
                }
                downloadFile();
            }

            @Override
            public void onFailure(Call<LessonList> call, Throwable t) {
                Log.d("Error",t.getMessage());
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
            }

        });


    }

    public void downloadFile(){

        if(checkPermission()){
            startDownload();
        } else {
            requestPermission();
        }
    }

    private void startDownload(){
        Intent intent = new Intent(this,DownloadService.class);
        intent.putStringArrayListExtra("UrlList",urlList);
        intent.putStringArrayListExtra("LessonType", lessonType);
        intent.putStringArrayListExtra("Concept", concept);
        startService(intent);
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
                mProgressBar.setProgress(download.getProgress());
                /*if(download.getProgress() == 100){
                    Toast.makeText(this,"File Download Complete",Toast.LENGTH_LONG).show();

                    mProgressText.setText("File Download Complete");

                } else {
                    Toast.makeText(this,"File Download Complete",Toast.LENGTH_LONG).show();
                    mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));

                }*/
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

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startDownload();
                } else {
                    Toast.makeText(this,"Permission Denied, Please allow to proceed !",Toast.LENGTH_LONG).show();
                    //Snackbar.make(findViewById(R.id.coordinatorLayout),"Permission Denied, Please allow to proceed !", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }


}
