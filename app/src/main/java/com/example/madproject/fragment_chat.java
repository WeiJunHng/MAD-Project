package com.example.madproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.madproject.gemini.ChatRequest;
import com.example.madproject.gemini.ChatResponse;
import com.example.madproject.gemini.GeminiApi;
import com.example.madproject.gemini.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_chat extends Fragment {

    private TextView chatHistory;
    private EditText userMessage;
    private Button sendButton;
    private String sessionId = "unique_session_id"; // Replace with logic to generate session IDs if needed

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        chatHistory = view.findViewById(R.id.chatHistory);
        userMessage = view.findViewById(R.id.userMessage);
        sendButton = view.findViewById(R.id.sendButton);

        sendButton.setOnClickListener(v -> {
            String message = userMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessageToGemini(message);
                userMessage.setText(""); // Clear input field
            } else {
                Toast.makeText(requireContext(), "Please enter a message", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void sendMessageToGemini(String message) {
        GeminiApi geminiApi = RetrofitClient.getInstance().create(GeminiApi.class);

        // Replace "YOUR_API_KEY" with your actual API key
        String apiKey = "AIzaSyDjdZeDGxk3RUIbsaZiqkGQ1W4_F4hn8kk";
        ChatRequest chatRequest = new ChatRequest(message, sessionId);

        geminiApi.chatWithGemini(apiKey, chatRequest).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update chat history
                    String reply = response.body().getReply();
                    String updatedHistory = chatHistory.getText().toString() + "\nYou: " + message + "\nGemini: " + reply;
                    chatHistory.setText(updatedHistory);
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                Toast.makeText(requireContext(), "Failed to connect to Gemini", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
