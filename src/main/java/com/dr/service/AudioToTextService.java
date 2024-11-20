package com.dr.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class AudioToTextService {

    @Value("${deepgram.api.key}")
    private String apiKey;
    @Value("${deepgram.api.url}")
    private String apiUrl;
    private static final String DEFAULT_MIMETYPE = "audio/wav"; // Set default MIME type


    /**
     * Converts audio data to text using the Deepgram API.
     *
     * @param audioData byte array containing audio data
     * @return the transcribed text as a String
     * @throws IOException if an I/O error occurs
     */
    public String transcribeAudio(byte[] audioData) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(apiUrl);

            // Set headers
            post.setHeader("Authorization", "Token " + apiKey);
            post.setHeader("Content-Type",DEFAULT_MIMETYPE);  // Adjust if audio format differs

            // Set audio data as the request entity
            HttpEntity audioEntity = new ByteArrayEntity(audioData);
            post.setEntity(audioEntity);

            // Execute the request
            HttpResponse response = httpClient.execute(post);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                // Parse response to extract transcription text
                String jsonString = new String(responseEntity.getContent().readAllBytes(), StandardCharsets.UTF_8);
                JSONObject jsonObject = new JSONObject(jsonString);

                // Assuming response has {"results": {"channels": [{"alternatives": [{"transcript": "text"}]}]}}
                return jsonObject
                        .getJSONObject("results")
                        .getJSONArray("channels")
                        .getJSONObject(0)
                        .getJSONArray("alternatives")
                        .getJSONObject(0)
                        .getString("transcript");
            }
        }

        return null; // return null or an appropriate default value if transcription fails
    }
}

