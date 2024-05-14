package org.healthtracker.api;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;

public class YouTubeApiService {
    // Constants for the YouTube API
    private static final String APPLICATION_NAME = "Health Tracker App";
    private static final String API_KEY = "ABC";//Replace with your api key

    // Method to search for exercise videos on YouTube and print URLs
    public static void searchExerciseVideos(String queryTerm) {
        // Build YouTube object with required parameters
        YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), request -> {})
                .setApplicationName(APPLICATION_NAME).build();

        try {
            // Create a search request for videos
            YouTube.Search.List search = youtube.search().list("id,snippet");
            search.setKey(API_KEY);
            search.setQ(queryTerm);
            search.setType("video");
            search.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
            search.setMaxResults(5L);
            // Execute the search request
            SearchListResponse searchResponse = search.execute();

            // Display Search Results
            for (SearchResult result : searchResponse.getItems()) {
                String videoId = result.getId().getVideoId();
                String videoUrl = "https://www.youtube.com/watch?v=" + videoId;
                System.out.println("Video ID: " + videoId);
                System.out.println("Title: " + result.getSnippet().getTitle());
                System.out.println("URL: " + videoUrl);
                System.out.println("Thumbnail: " + result.getSnippet().getThumbnails().getDefault().getUrl());
                System.out.println("\n-------------------------------------------------------------\n");
            }
        } catch (IOException e) {
            System.err.println("Could not initialize youtube: " + e);
        }
    }
    public static void main(String[] args) {
        YouTubeApiService youtubeApiService = new YouTubeApiService();
        youtubeApiService.searchExerciseVideos("exercise");
    }

}



