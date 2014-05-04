package com.relaxisapp.relaxis.daos;

import android.util.Log;

import com.relaxisapp.relaxis.models.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by zdravko on 14-5-1.
 */
public class UsersDao {

    public User read(String fbid) {
        final String url = "http://relaxisapp.com/api/users/" + fbid;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization-Token", "$kG7j2lr&");
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("Host", "relaxisapp.com");
        HttpEntity<?> httpEntity = new HttpEntity<Object>(requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(
                new MappingJackson2HttpMessageConverter());
        User user;
        try {
            user = restTemplate.exchange(url, HttpMethod.GET, httpEntity, User.class).getBody();
            return user;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 404) {
                // this facebook user is not yet registered
                // return null;
            } else {
                Log.e("MainActivity", e.getMessage(), e);
            }
        }

        //TODO throw appropriate exceptions
        return null;
    }

    public int create(User user) {
        try {
            final String url = "http://relaxisapp.com/api/users/";
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Authorization-Token", "$kG7j2lr&");
            requestHeaders.add("Content-Type", "application/json");
            requestHeaders.add("Host", "relaxisapp.com");
            HttpEntity<User> httpEntity = new HttpEntity<User>(user, requestHeaders);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(
                    new MappingJackson2HttpMessageConverter());
            User createdUser = restTemplate.exchange(url, HttpMethod.POST, httpEntity, User.class).getBody();
            return createdUser.getUserId();
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }

        //TODO throw appropriate exceptions
        return 0;
    }
}
