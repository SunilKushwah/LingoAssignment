package com.example.sunil.lingo_assignment;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DownloadService extends IntentService {
    public static final String TAG = "DownloadService";
    private ArrayList<String> urlList;
    private ArrayList<String> concept;
    private ArrayList<String> lessonType;
    public DownloadService() {
        super("Download Service");
    }
    private int totalFileSize;
    int count = 0;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("DownLoadService","Start");
        urlList = intent.getStringArrayListExtra("UrlList");
        concept = intent.getStringArrayListExtra("Concept");
        lessonType = intent.getStringArrayListExtra("LessonType");
        Log.d("DownLoadService","urlList "+urlList.toString());
        Log.d("DownLoadService","concept "+concept.toString());
        Log.d("DownLoadService","lessonType "+lessonType.toString());

        initDownload();

    }

    private void initDownload(){
        Log.d(TAG,"initDownload");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(" https://s3.ap-south-1.amazonaws.com/multibhashi-data/audio/english2kannada/")
                .build();
        ApiRequest api = retrofit.create(ApiRequest.class);

        for(int i=0;i<urlList.size();i++) {

            Call<ResponseBody> request = api.downloadFileWithDynamicUrlSync(urlList.get(i));
            try {
                downloadFile(request.execute().body(), lessonType.get(i),concept.get(i),i);
            } catch (IOException e) {

                e.printStackTrace();
                Log.d("Error",e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }


    private void downloadFile(ResponseBody body,String type, String concept,int i) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile;
        if(type.equals("learn")){
            outputFile = new File(getExternalFilesDir(null) + File.separator + type + "_lesson_"+concept+".aac");
        }else{
            outputFile = new File(getExternalFilesDir(null) + File.separator + type + "_lesson_"+concept+".aac");
        }
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {

            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));
            int progress = (int) ((total * 100) / fileSize);
            long currentTime = System.currentTimeMillis() - startTime;
            Download download = new Download();
            download.setTotalFileSize(totalFileSize);

            if (currentTime > 1000 * timeCount) {
                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        if((urlList.size()-1)==i) {
            onDownloadComplete();
        }
        output.flush();
        output.close();
        bis.close();

    }



    private void sendIntent(Download download){

        Intent intent = new Intent(Splash.MESSAGE_PROGRESS);
        intent.putExtra("download",download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(){
        Download download = new Download();
        download.setProgress(100);
        sendIntent(download);
    }

}
