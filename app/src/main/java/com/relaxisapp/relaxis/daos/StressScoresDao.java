package com.relaxisapp.relaxis.daos;

import android.util.Log;

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

        StressScore body = new StressScore(score.getUserId(),
                score.getScore(),
                SimpleDateFormat.getDateTimeInstance().format(cal.getTime()));
        HttpEntity<StressScore> httpEntity = new HttpEntity<StressScore>(body, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(
                new MappingJackson2HttpMessageConverter());

        try {
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, StressScore.class);
        } catch (Exception e) {
            Log.e("StressScoresDao", e.getMessage(), e);
            return false;
        }

        return true;
    }

    @Override
    public StressScore[] read(int userId) {
        final String url = "http://relaxisapp.com/api/users/"+ userId +"/stressscores";
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
            Log.e("StressScoresDao", e.getMessage(), e);
            return null;
        }

        return scores;
    }

    @Override
    public void update(StressScore score) {

    }

    @Override
    public void delete(StressScore score) {

    }
}
