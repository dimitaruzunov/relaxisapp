package com.relaxisapp.relaxis.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.models.HomeModel;
import com.relaxisapp.relaxis.views.HomeView;


public class HomeFragment extends Fragment {

	public final static String SECTION_TITLE = "section title";

    public static int connectionState = 0;

    private HomeModel model;
    private HomeView view;
    private Handler handler;

    AudioManager audioManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        model = HomeModel.getInstance();
        view = (HomeView) inflater.inflate(R.layout.fragment_home, container, false);
        view.setViewListener(viewListener);

        handler = new Handler();

		return view;
	}

    private HomeView.ViewListener viewListener = new HomeView.ViewListener() {

        @Override
        public void onConnectButtonClick() {
            switch (model.getConnectionState()) {
                case 0:
                    ((MainActivity) getActivity()).executeConnect();
                    break;
                case 1:
                    ((MainActivity) getActivity()).cancelConnecting();
                    break;
                case 2:
                    ((MainActivity) getActivity()).executeDisconnect();
                    break;
            }
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        @Override
        public void onMusicButtonClick() {
            // TODO make a check if the user has played something
            model.setMusicPlayed(true);

            Intent intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_MUSIC);
            startActivity(intent);
        }

        @Override
        public void onPrevButtonClick() {
            Intent i = new Intent("com.android.music.musicservicecommand");
            i.putExtra("command", "previous");
            getActivity().sendBroadcast(i);
        }

        @Override
        public void onPauseButtonClick() {
            audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.isMusicActive()) {
                model.setMusicPlayed(false);
            } else {
                model.setMusicPlayed(true);
            }

            Intent i = new Intent("com.android.music.musicservicecommand");
            i.putExtra("command", "togglepause");
            getActivity().sendBroadcast(i);
        }

        @Override
        public void onStopButtonClick() {
            audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            if(audioManager.isMusicActive()) {
                model.setMusicPlayed(false);

                Intent i = new Intent("com.android.music.musicservicecommand");
                i.putExtra("command", "stop");
                getActivity().sendBroadcast(i);
            }
        }

        @Override
        public void onNextButtonClick() {
            Intent i = new Intent("com.android.music.musicservicecommand");
            i.putExtra("command", "next");
            getActivity().sendBroadcast(i);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        view.destroy();
    }
}
