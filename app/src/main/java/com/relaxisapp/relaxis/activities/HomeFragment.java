package com.relaxisapp.relaxis.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.relaxisapp.relaxis.HintHelper;
import com.relaxisapp.relaxis.OnBtConnectionChangeListener;
import com.relaxisapp.relaxis.R;

public class HomeFragment extends Fragment implements OnBtConnectionChangeListener {

	public final static String SECTION_TITLE = "section title";
	
	public static int connectionState = 0;
	Button connectButton;
	Button musicButton;
	
	private Bundle savedState = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		setupViews(view);
		
		if (savedInstanceState != null && savedState == null) {
			savedState = savedInstanceState.getBundle("ConnectButton");
		}
        if(savedState != null) {
        	int connectionState = savedState.getInt("ConnectionState");
        	if (connectionState != 1) {
        		onBtConnectionChange(savedState.getInt("ConnectionState"), connectButton);
        	}
        }
        savedState = null;
		
		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putBundle("ConnectButton", savedState != null ? savedState : saveState());
	}
	
	@Override
    public void onDestroyView() {
        super.onDestroyView();
        
        savedState = saveState();
        connectButton = null;
    }
	
	private Bundle saveState() {
        Bundle state = new Bundle();
        state.putInt("ConnectionState", connectionState);
        
        return state;
    }

	private void setupViews(View view) {
		connectButton = (Button) view.findViewById(R.id.connectButton);
		connectButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				handleConnectButtonClick((Button) view, getActivity());
			}
		});
		
		musicButton = (Button) view.findViewById(R.id.musicButton);
		musicButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				handleMusicButtonClick((Button) view, getActivity());
			}
		});
	}
	
	static void handleConnectButtonClick(Button button, Context activity) {
		if (connectionState == 0) {
			((MainActivity) activity).executeConnect(button);
		}
		else if (connectionState == 2) {
			((MainActivity) activity).executeDisconnect(button);
		}
	}
	
	void handleMusicButtonClick(Button button, Context activity) {
		    Intent intent=Intent.makeMainSelectorActivity(Intent.ACTION_MAIN,
		    Intent.CATEGORY_APP_MUSIC);
		    startActivity(intent);
	}
	
	@Override
	public void onBtConnectionChange(int connectionState, Button button) {
		HomeFragment.connectionState = connectionState;
		
		switch (connectionState) {
		case 0:
			changeBtIconConnect(button);
			break;
		case 1:
			changeBtIconConnecting(button);
			break;
		case 2:
			changeBtIconConnected(button);
			break;
		}
	}
	
	void changeBtIconConnect(Button button) {
		button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_bluetooth, 0, 0, 0);
		button.setText(R.string.action_bluetooth_connect);
	}

	void changeBtIconConnecting(Button button) {
		button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_bluetooth_searching, 0, 0, 0);
		button.setText(R.string.action_bluetooth_connecting);
	}

	void changeBtIconConnected(Button button) {
		button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_bluetooth_connected, 0, 0, 0);
		button.setText(R.string.action_bluetooth_disconnect);
	}

}
