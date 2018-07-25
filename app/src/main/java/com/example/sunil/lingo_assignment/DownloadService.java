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

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;
    int count = 0;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("DownLoadService","Start");
        urlList = intent.getStringArrayListExtra("UrlList");
        concept = intent.getStringArrayListExtra("Concept");
        lessonType = intent.getStringArrayListExtra("LessonType");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_download)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());

        initDownload();

    }

    private void initDownload(){
        Log.d("C","initDownload");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(" https://s3.ap-south-1.amazonaws.com/multibhashi-data/audio/english2kannada/")
                .build();
        ApiRequest api = retrofit.create(ApiRequest.class);

        for(int i=0;i<urlList.size();i++) {
            count = i;
            Call<ResponseBody> call = api.downloadFileWithDynamicUrlSync(urlList.get(i));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        Log.d("initDownload", "server contacted and has file");
                        //downloadFile(response.body(), lessonType.get(count),concept.get(count));
                        writeResponseBodyToDisk(response.body(),lessonType.get(count),concept.get(count));
                        Log.d(TAG, "file download was a success ");
                        Log.d(TAG, "server contact failed");

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "error");
                }
            });
            /*Call<ResponseBody> request = api.downloadFileWithDynamicUrlSync(urlList.get(i));
            try {
                downloadFile(request.execute().body(), lessonType.get(i),concept.get(i));
            } catch (IOException e) {

                e.printStackTrace();
                Log.d("Error",e.getMessage());
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }*/
        }
    }
    private boolean writeResponseBodyToDisk(ResponseBody body,String type,String concept) {
        try {
            File audioFile;
            if(type.equals("learn")) {
                audioFile = new File(getExternalFilesDir(null) + File.separator + type + "_lesson_"+concept+".aac");
            }else{
                audioFile = new File(getExternalFilesDir(null) + File.separator + type + "_lesson_"+concept+".aac");
            }
            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(audioFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void downloadFile(ResponseBody body,String lessonType, String concept) throws IOException {

        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
        //File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "lesson_audio"+i+".aac");
        File outputFile;
        if(lessonType.equals("learn")){
            outputFile = new File(getFilesDir(), concept+"_learn_audio.aac");
        }else{
            outputFile = new File(getFilesDir(), concept+"_question_audio.aac");
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
                sendNotification(download);
                timeCount++;
            }

            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

    }

    private void sendNotification(Download download){

        sendIntent(download);
        notificationBuilder.setProgress(100,download.getProgress(),false);
        notificationBuilder.setContentText("Downloading file "+ download.getCurrentFileSize() +"/"+totalFileSize +" MB");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(Download download){

        Intent intent = new Intent(LearnActivity.MESSAGE_PROGRESS);
        intent.putExtra("download",download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(){

        Download download = new Download();
        download.setProgress(100);
        sendIntent(download);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText("File Downloaded");
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

}
