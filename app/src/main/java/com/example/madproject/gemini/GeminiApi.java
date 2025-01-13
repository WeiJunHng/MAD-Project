package com.example.madproject.gemini;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GeminiApi {
    @POST("chat") // Gemini's chat endpoint
    Call<ChatResponse> chatWithGemini(
            @Header("Authorization") String apiKey,
            @Body ChatRequest chatRequest
    );
}
