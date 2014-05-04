package com.relaxisapp.relaxis;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import android.os.AsyncTask;
import android.util.Log;

import com.relaxisapp.relaxis.activities.BreathingFragment;
import com.relaxisapp.relaxis.activities.StressEstimationFragment;
import com.relaxisapp.relaxis.models.BreathingScore;
import com.relaxisapp.relaxis.models.StressScore;
import com.relaxisapp.relaxis.models.User;

public class ApiConnection {
	public static String FbUserId;
	public static String FbUserName;
	public static int UserId;
	public static ArrayList<BreathingScore> currentUserBreathingScores = null;
	public static ArrayList<StressScore> currentUserStressScores = null;
}
