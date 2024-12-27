package com.example.madproject.ui.news;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.madproject.ui.news.Models.NewsApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class NewsRequestManager {
    public static final String API_KEY = "887ae6858ab3422cbda2f44c8251f610";
    private final Context context;
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public NewsRequestManager(Context context) {
        this.context = context;
    }

    public void getNewsHeadlines(NewsViewModel newsViewModel, String category, String query) {
        CallNewsApi callNewsApi = retrofit.create(CallNewsApi.class);
//        Call<NewsApiResponse> call = callNewsApi.callHeadlines("us", category, query, context.getString(R.string.api_key));
        Call<NewsApiResponse> call = callNewsApi.callHeadlines("us", category, query, API_KEY);

        try {
            call.enqueue(new Callback<NewsApiResponse>() {
                @Override
                public void onResponse(@NonNull Call<NewsApiResponse> call, @NonNull Response<NewsApiResponse> response) {
                    if(!response.isSuccessful()) {
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(response.body() == null) {
                        Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    newsViewModel.setHeadlinesList(response.body().getArticles());
                }

                @Override
                public void onFailure(@NonNull Call<NewsApiResponse> call, @NonNull Throwable throwable) {
//                    listener.onError("Request Failed!");
                    Toast.makeText(context, "Request Failed!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public interface CallNewsApi {
        @GET("top-headlines")
        Call<NewsApiResponse> callHeadlines(
                @Query("country") String country,
                @Query("category") String category,
                @Query("q") String query,
                @Query("apiKey") String api_key
        );
    }
}
