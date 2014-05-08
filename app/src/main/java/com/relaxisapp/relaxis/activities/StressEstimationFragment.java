package com.relaxisapp.relaxis.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.daos.StressScoresDao;
import com.relaxisapp.relaxis.events.Event;
import com.relaxisapp.relaxis.events.EventListener;
import com.relaxisapp.relaxis.models.BreathingModel;
import com.relaxisapp.relaxis.models.HomeModel;
import com.relaxisapp.relaxis.models.StressModel;
import com.relaxisapp.relaxis.models.StressScore;
import com.relaxisapp.relaxis.models.UserModel;
import com.relaxisapp.relaxis.views.StressView;
import com.relaxisapp.relaxis.widgets.SectionsPagerAdapter;
import com.relaxisapp.relaxis.utils.BtConnection;
import com.relaxisapp.relaxis.utils.Const;

public class StressEstimationFragment extends Fragment {

    private StressScoresDao stressScoresDao;

    private StressView view;
    private HomeModel homeModel;
    private StressModel stressModel;
    private UserModel userModel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        stressScoresDao = new StressScoresDao();

        homeModel = HomeModel.getInstance();
        stressModel = StressModel.getInstance();
        userModel = UserModel.getInstance();

		view = (StressView) inflater.inflate(R.layout.fragment_stress_estimation, container, false);

        homeModel.addListener(HomeModel.ChangeEvent.NN_COUNT_CHANGED, nnCountListener);
		
		// TODO check if the timer is cleared when the back button is pressed
		// and then the activity is started again
		// graphUpdateTimer.scheduleAtFixedRate(graphUpdateTimerTask, 0, 1000);
		
		return view;
	}

    private EventListener nnCountListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            if (homeModel.getNnCount() % Const.TIME_STRESS_SECONDS == 0 && homeModel.getConnectionState() == 2) {
                saveStressScore();
            }
        }
    };

    private boolean saveStressScore() {
        if (userModel.getUserId() > 0) {
            MainActivity.dalHandler.post(new Runnable() {
                @Override
                public void run() {
                    stressScoresDao.create(new StressScore(userModel.getUserId(), stressModel.getStressLevel() * 100));
                    userModel.addStressScore(stressModel.getStressLevel() * 100);
                }
            });

            Toast.makeText(getActivity(), "Stress level saved: " +
                    stressModel.getStressLevel() * 10, Toast.LENGTH_SHORT).show();
        }

        // TODO return whether the save is successful
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view.destroy();
    }
}
