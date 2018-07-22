package com.example.sunil.lingo_assignment;

import com.example.sunil.lingo_assignment.model.LessonList;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiRequest {
    String BASE_URL = "https://www.multibhashi.com/";
    @GET("getData.php")
    Call<LessonList> getLessonList();

    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
}
