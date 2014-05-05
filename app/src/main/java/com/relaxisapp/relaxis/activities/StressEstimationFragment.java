package com.relaxisapp.relaxis.activities;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.daos.StressScoresDao;
import com.relaxisapp.relaxis.models.StressScore;
import com.relaxisapp.relaxis.models.UserModel;
import com.relaxisapp.relaxis.widgets.SectionsPagerAdapter;
import com.relaxisapp.relaxis.utils.BtConnection;
import com.relaxisapp.relaxis.utils.Const;

public class StressEstimationFragment extends Fragment {

	static Timer timeUpdateTimer = new Timer();
	static TimeUpdateTimerTask timeUpdateTimerTask;
	
	private Handler timeLeftUpdateHandler = new Handler();
	
	static int timeLeft = Const.TIME_STRESS_SECONDS;

	public static double stressLevel;
	static Boolean updateScore = false;
	
	static TextView stressLevelTextView;
	private TextView stressLevelDescTextView;
	private TextView timeLeftTextView;
	private Button startStressEstimationButton;
	private boolean isStopped = true;

    private StressScoresDao stressScoresDao;

    private UserModel userModel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        stressScoresDao = new StressScoresDao();

        userModel = UserModel.getInstance();

		View view = inflater.inflate(R.layout.fragment_stress_estimation, container, false);
		
		setupViews(view);
		
		// TODO check if the timer is cleared when the back button is pressed
		// and then the activity is started again
		// graphUpdateTimer.scheduleAtFixedRate(graphUpdateTimerTask, 0, 1000);
		
		return view;
	}

	private void setupViews(View view) {
		startStressEstimationButton = (Button) view.findViewById(R.id.startStressEstimationButton);
		startStressEstimationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				handleStartStressEstimationButtonClick((Button) view);
			}
		});
		
		timeLeftTextView = (TextView) view.findViewById(R.id.stressTimeLeftTextView);
		timeLeftTextView.setText(String.valueOf(timeLeft));
		
		stressLevelDescTextView = (TextView) view.findViewById(R.id.stressLevelDescTextView);
		
		stressLevelTextView = (TextView) view.findViewById(R.id.stressLevelTextView);
	}
	
	private void handleStartStressEstimationButtonClick(Button button) {
		if (isStopped) {
			start(button);
		} else {
			stop(button);
		}
	}
	
	private void start(Button button) {
		if (HomeFragment.connectionState != 2) {
			MainActivity.viewPager.setCurrentItem(SectionsPagerAdapter.HOME_FRAGMENT);
			Toast.makeText(getActivity(), "Please connect to HxM", Toast.LENGTH_SHORT).show();
		} else {
			if (timeLeft == 0) {
				timeLeft = Const.TIME_STRESS_SECONDS;
			}
			updateScore = true;
			timeUpdateTimer = new Timer();
			timeUpdateTimerTask = new TimeUpdateTimerTask();
			timeUpdateTimer.scheduleAtFixedRate(
					timeUpdateTimerTask, 0,
					1000);
			isStopped = false;
			changeButtonIconStop(button);
			showStressLevel();
		}
	}
	
	private void stop(Button button) {
		isStopped = true;
		updateScore = false;
		changeButtonIconStart(button);
		hideStressLevel();
		timeUpdateTimerTask.cancel();
		timeUpdateTimer.cancel();
	}
	
	private void changeButtonIconStart(Button button) {
		button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_play, 0, 0, 0);
		button.setText(R.string.start);
	}
	
	private void changeButtonIconStop(Button button) {
		button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_stop, 0, 0, 0);
		button.setText(R.string.stop);
	}
	
	private void showStressLevel() {
		stressLevelDescTextView.setVisibility(View.VISIBLE);
		stressLevelTextView.setVisibility(View.VISIBLE);
	}
	
	private void hideStressLevel() {
		stressLevelDescTextView.setVisibility(View.INVISIBLE);
		stressLevelTextView.setVisibility(View.INVISIBLE);
	}
	
	private class TimeUpdateTimerTask extends TimerTask {

		@Override
		public void run() {
			timeLeftUpdateHandler.post(new Runnable() {
				@Override
				public void run() {
					StressEstimationFragment.stressLevelTextView
					.setText(String.valueOf(new DecimalFormat("#0.00").format(stressLevel*100)));
					updateTimeLeft();
					System.out.println("test"+ BtConnection.nnCount);
				}
			});
		}

	}

	private void updateTimeLeft() {
		timeLeftTextView.setText(String.valueOf(timeLeft));
		if (timeLeft == 0)
		{
			if (userModel.getUserId() > 0) {
                MainActivity.dalHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        stressScoresDao.create(new StressScore(stressLevel * 10));
                    }
                });

				Toast.makeText(getActivity(), "Stress level saved: " +
				StressEstimationFragment.stressLevel * 10, Toast.LENGTH_SHORT).show();
			}
			// startStressEstimationButton.callOnClick(); TODO find alternative method
			return;
		}
		timeLeft--;
	}

}
