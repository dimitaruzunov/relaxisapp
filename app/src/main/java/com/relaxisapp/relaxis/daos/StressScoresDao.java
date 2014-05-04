package com.relaxisapp.relaxis.daos;

import android.util.Log;

import com.relaxisapp.relaxis.ApiConnection;
import com.relaxisapp.relaxis.models.BreathingScore;
import com.relaxisapp.relaxis.models.StressScore;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StressScoresDao implements IScoresDao<StressScore> {

    @Override
    public boolean create(StressScore score) {

        final String url = "http://relaxisapp.com/api/stressscores/";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization-Token", "$kG7j2lr&");
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("Host", "relaxisapp.com");

        Calendar cal = Calendar.getInstance();

        StressScore body = new StressScore(ApiConnection.UserId,
                score.getScore(),
                SimpleDateFormat.getDateTimeInstance().format(cal.getTime()));
        HttpEntity<StressScore> httpEntity = new HttpEntity<StressScore>(body, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(
                new MappingJackson2HttpMessageConverter());

        try {
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, StressScore.class);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
            return false;
        }

        return true;
    }

    @Override
    public StressScore[] read() {
        final String url = "http://relaxisapp.com/api/users/"+ ApiConnection.UserId +"/stressscores";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization-Token", "$kG7j2lr&");
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("Host", "relaxisapp.com");
        HttpEntity<?> httpEntity = new HttpEntity<Object>(requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(
                new MappingJackson2HttpMessageConverter());
        StressScore[] scores;
        try {
            scores = restTemplate.exchange(url, HttpMethod.GET, httpEntity, StressScore[].class).getBody();
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
            return null;
        }

        ApiConnection.currentUserStressScores = new ArrayList<StressScore>();
        for (int i = 0; i < scores.length; i++) {
            ApiConnection.currentUserStressScores.add(scores[i]);
        }


        return scores;
    }

    @Override
    public StressScore read(int id) {
        return null;
    }

    @Override
    public void update(StressScore score) {

    }

    @Override
    public void delete(StressScore score) {

    }
}
