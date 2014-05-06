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
import com.relaxisapp.relaxis.models.StressModel;
import com.relaxisapp.relaxis.models.StressScore;
import com.relaxisapp.relaxis.models.UserModel;
import com.relaxisapp.relaxis.views.HomeView;
import com.relaxisapp.relaxis.views.StressView;
import com.relaxisapp.relaxis.widgets.SectionsPagerAdapter;
import com.relaxisapp.relaxis.utils.BtConnection;
import com.relaxisapp.relaxis.utils.Const;

public class StressEstimationFragment extends Fragment {

	static Timer timeUpdateTimer = new Timer();
	static TimeUpdateTimerTask timeUpdateTimerTask;
	
	private Handler timeLeftUpdateHandler = new Handler();
	
	static int timeLeft = Const.TIME_STRESS_SECONDS;

    private StressScoresDao stressScoresDao;

    private StressView view;
    private StressModel stressModel;
    private UserModel userModel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        stressScoresDao = new StressScoresDao();

        stressModel = StressModel.getInstance();
        userModel = UserModel.getInstance();

		view = (StressView) inflater.inflate(R.layout.fragment_stress_estimation, container, false);
		
		// TODO check if the timer is cleared when the back button is pressed
		// and then the activity is started again
		// graphUpdateTimer.scheduleAtFixedRate(graphUpdateTimerTask, 0, 1000);
		
		return view;
	}
	
	private void start() {
		if (HomeFragment.connectionState != 2) {
			MainActivity.viewPager.setCurrentItem(SectionsPagerAdapter.HOME_FRAGMENT);
			Toast.makeText(getActivity(), "Please connect to HxM", Toast.LENGTH_SHORT).show();
		} else {
			if (timeLeft == 0) {
				timeLeft = Const.TIME_STRESS_SECONDS;
			}
			timeUpdateTimer = new Timer();
			timeUpdateTimerTask = new TimeUpdateTimerTask();
			timeUpdateTimer.scheduleAtFixedRate(
					timeUpdateTimerTask, 0,
					1000);
		}
	}
	
	private void stop() {
		timeUpdateTimerTask.cancel();
		timeUpdateTimer.cancel();
	}
	
	private class TimeUpdateTimerTask extends TimerTask {

		@Override
		public void run() {
			timeLeftUpdateHandler.post(new Runnable() {
				@Override
				public void run() {
					updateTimeLeft();
					System.out.println("test"+ BtConnection.nnCount);
				}
			});
		}

	}

	private void updateTimeLeft() {
		if (timeLeft == 0)
		{
			if (userModel.getUserId() > 0) {
                MainActivity.dalHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        stressScoresDao.create(new StressScore(userModel.getUserId(), stressModel.getStressLevel() * 10));
                    }
                });

				Toast.makeText(getActivity(), "Stress level saved: " +
				stressModel.getStressLevel() * 10, Toast.LENGTH_SHORT).show();
			}
			// startStressEstimationButton.callOnClick(); TODO find alternative method
			return;
		}
		timeLeft--;
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        view.destroy();
    }
}
