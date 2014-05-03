package com.relaxisapp.relaxis.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.events.Event;
import com.relaxisapp.relaxis.events.EventListener;
import com.relaxisapp.relaxis.models.HomeModel;
import com.relaxisapp.relaxis.views.HomeView;


public class HomeFragment extends Fragment {

	public final static String SECTION_TITLE = "section title";
	
	public static int connectionState = 0;

    private HomeModel model;
    private HomeView view;
    private Handler handler;

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

        @Override
        public void onMusicButtonClick() {
            // Intent intent=Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_MUSIC); TODO find alternative
            // startActivity(intent);
        }
    };

}
