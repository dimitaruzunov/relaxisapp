package com.relaxisapp.relaxis.daos;

import android.util.Log;

import com.relaxisapp.relaxis.models.BreathingScore;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class BreathingScoresDao implements IScoresDao<BreathingScore> {

    @Override
    public boolean create(BreathingScore score) {

        final String url = "http://relaxisapp.com/api/breathingscores/";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization-Token", "$kG7j2lr&");
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("Host", "relaxisapp.com");

        Calendar cal = Calendar.getInstance();

        BreathingScore body = new BreathingScore(score.getUserId(),
                score.getScore(),
                SimpleDateFormat.getDateTimeInstance().format(cal.getTime()));
        HttpEntity<BreathingScore> httpEntity = new HttpEntity<BreathingScore>(body, requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(
                new MappingJackson2HttpMessageConverter());

        try {
            restTemplate.exchange(url, HttpMethod.POST, httpEntity, BreathingScore.class);
        } catch (Exception e) {
            Log.e("CreateBreathingScore", e.getMessage(), e);
            return false;
        }

        return true;
    }

    @Override
    public BreathingScore[] read(int userId) {

        final String url = "http://relaxisapp.com/api/users/"+ userId +"/breathingscores";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization-Token", "$kG7j2lr&");
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("Host", "relaxisapp.com");
        HttpEntity<?> httpEntity = new HttpEntity<Object>(requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(
                new MappingJackson2HttpMessageConverter());

        BreathingScore[] scores;
        try {
            scores = restTemplate.exchange(url, HttpMethod.GET, httpEntity, BreathingScore[].class).getBody();
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
            return null;
        }

        return scores;
    }

    @Override
    public void update(BreathingScore score) {

    }

    @Override
    public void delete(BreathingScore score) {

    }
}
