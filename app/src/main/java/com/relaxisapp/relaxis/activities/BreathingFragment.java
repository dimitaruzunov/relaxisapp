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

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.daos.BreathingScoresDao;
import com.relaxisapp.relaxis.models.BreathingScore;
import com.relaxisapp.relaxis.models.UserModel;
import com.relaxisapp.relaxis.widgets.SectionsPagerAdapter;
import com.relaxisapp.relaxis.events.Event;
import com.relaxisapp.relaxis.events.EventListener;
import com.relaxisapp.relaxis.models.BreathingModel;
import com.relaxisapp.relaxis.models.HomeModel;
import com.relaxisapp.relaxis.utils.BtConnection;
import com.relaxisapp.relaxis.utils.Const;
import com.relaxisapp.relaxis.views.BreathingView;

public class BreathingFragment extends Fragment {

	// TODO find out why it is breaking at some point

	private static Timer graphUpdateTimer = new Timer();
    private static Timer timeUpdateTimer = new Timer();
    private static GraphUpdateTimerTask graphUpdateTimerTask;
    private static TimeLeftUpdateTimerTask timeLeftUpdateTimerTask;

	private static Handler handler = new Handler();

	static int idealMinHR = Const.IDEAL_MID_HR - Const.IDEAL_HR_DEVIATION;
	static int idealMaxHR = Const.IDEAL_MID_HR + Const.IDEAL_HR_DEVIATION;

	static int avgMaxHR = idealMaxHR;
	static int avgMinHR = idealMinHR;
	static int newMaxHR = avgMaxHR;
	static int newMinHR = avgMinHR;
	static double tAvgHR = (avgMaxHR + avgMinHR) / 2.0;
	static double tDeviation;
	static double tIdealHR;

	static Boolean updateScore = false;
	static int consecutivePoints = 0;
	static int multiplier = 1;

	public static int beatsCount = 0;
	public static int timerCounter = 0;

    private HomeModel homeModel;
    private BreathingModel breathingModel;
    private UserModel userModel;
    private BreathingView view;

    private BreathingScoresDao breathingScoresDao;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        breathingScoresDao = new BreathingScoresDao();

        homeModel = HomeModel.getInstance();
        breathingModel = BreathingModel.getInstance();
        userModel = UserModel.getInstance();
        view = (BreathingView) inflater.inflate(R.layout.fragment_breathing, container, false);
        view.setViewListener(viewListener);

        breathingModel.addListener(BreathingModel.ChangeEvent.GRAPH_UPDATE_STARTED_STATE_CHANGED, graphUpdateStartListener);

        updateDummyGraph(idealMinHR);
        updateDummyGraph(idealMaxHR);

		return view;
	}

    private EventListener graphUpdateStartListener = new EventListener() {
        @Override
        public void onEvent(Event event) {
            if (breathingModel.getGraphUpdateStartedState()) {
                startInstantHRGraphTimer();
            }
            else {
                stopInstantHRGraphTimer();
                beatsCount = timerCounter / Const.TIMER_TICKS_PER_SECOND;
            }
        }
    };

    // onButtonClick
    private BreathingView.ViewListener viewListener = new BreathingView.ViewListener() {
        @Override
        public void onStartButtonClick() {
            if (breathingModel.getStartedState()) {
                stop();
            } else {
                start();
            }
        }
    };

    private void startInstantHRGraphTimer() {
        // TODO check if the timer is cleared when the back button is
        // pressed
        // and then the activity is started again
        graphUpdateTimerTask =
                new GraphUpdateTimerTask();
        graphUpdateTimer.scheduleAtFixedRate(
                graphUpdateTimerTask,
                (timerCounter % Const.TIMER_TICKS_PER_SECOND) * 1000 / Const.TIMER_TICKS_PER_SECOND,
                1000 / Const.TIMER_TICKS_PER_SECOND);
    }

    private void stopInstantHRGraphTimer() {
        graphUpdateTimerTask.cancel();
    }

	private void start() {
		if (homeModel.getConnectionState() != 2) {
			MainActivity.viewPager
					.setCurrentItem(SectionsPagerAdapter.HOME_FRAGMENT);
			Toast.makeText(getActivity(), "Please connect to HxM",
					Toast.LENGTH_SHORT).show();
		} else {
			timeUpdateTimer = new Timer();
			timeLeftUpdateTimerTask = new TimeLeftUpdateTimerTask();
			timeUpdateTimer.scheduleAtFixedRate(
                    timeLeftUpdateTimerTask, 1000,
					1000);
			updateScore = true;
			breathingModel.setStartedState(true);
		}
	}

	private void stop() {
		updateScore = false;
		breathingModel.setStartedState(false);
		timeLeftUpdateTimerTask.cancel();
		timeUpdateTimer.cancel();
        resetScoreAndTime();
	}

	private void resetScoreAndTime() {
        multiplier = 1;
		breathingModel.setScore(0);
		breathingModel.setTimeLeft(Const.TIME_BREATHING_SECONDS);
	}

	public class GraphUpdateTimerTask extends TimerTask {

		@Override
		public void run() {
			handler.post(new Runnable() {

                @Override
                public void run() {
                    updateIdealHR();
                    updateDummyGraph((timerCounter % 2 == 0) ? idealMinHR : idealMaxHR);
                }
            });
            timerCounter++;
		}

	}

	private void updateIdealHR() {
		if (Math.sin(timerCounter * Math.PI
				/ (6 * Const.TIMER_TICKS_PER_SECOND)) == 1) {
			avgMinHR = newMinHR;
		} else if (Math.sin(timerCounter * Math.PI
				/ (6 * Const.TIMER_TICKS_PER_SECOND)) == -1) {
			avgMaxHR = newMaxHR;
		}
		tAvgHR = (avgMaxHR + avgMinHR) / 2.0;
		tDeviation = avgMaxHR - tAvgHR;
		tIdealHR = tAvgHR
				+ Math.sin(timerCounter * Math.PI
						/ (6 * Const.TIMER_TICKS_PER_SECOND)) * tDeviation;

        breathingModel.addIdealGraphData(new GraphViewData(
                timerCounter * 1.0 / Const.TIMER_TICKS_PER_SECOND,
                tIdealHR));
    }

	private void updateDummyGraph(int value) {
		// keep the viewport 2 seconds forward and zoom it to the min/max ideal
		// HR
		BtConnection.dummySeries.appendData(new GraphViewData(
				(timerCounter * 1.0 / Const.TIMER_TICKS_PER_SECOND) + 2,
				value), true, 2);
	}

	class TimeLeftUpdateTimerTask extends TimerTask {

		@Override
		public void run() {
			handler.post(new Runnable() {
                @Override
                public void run() {
                    updateTimeLeft();
                }
            });
		}

	}

	private void updateTimeLeft() {
		if (breathingModel.getTimeLeft() == 0) {
            final int scoreToSave = breathingModel.getScore();
			if (userModel.getUserId() > 0 && scoreToSave > 0) {
                MainActivity.dalHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        breathingScoresDao.create(new BreathingScore(userModel.getUserId(), scoreToSave));
                        userModel.addBreathingScore(scoreToSave);
                    }
                });

                try {
                    Toast.makeText(getActivity(), "Breathing score saved: " +
                            breathingModel.getScore(), Toast.LENGTH_SHORT).show();
                }
                catch (NullPointerException e) {
                    // fragment is not initialized and getActivity() is not valid
                }
			}
            stop();
			return;
		}
        breathingModel.setTimeLeft(breathingModel.getTimeLeft() - 1);
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        view.destroy();
        breathingModel.removeListener(BreathingModel.ChangeEvent.GRAPH_UPDATE_STARTED_STATE_CHANGED, graphUpdateStartListener);
    }
}
