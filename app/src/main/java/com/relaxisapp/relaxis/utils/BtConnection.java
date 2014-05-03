package com.relaxisapp.relaxis.utils;

import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.relaxisapp.relaxis.ConnectionListener;

import zephyr.android.HxMBT.BTClient;
import zephyr.android.HxMBT.ZephyrProtocol;
import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;

public class BtConnection {

    public static BluetoothAdapter adapter = null;
    public static BTClient _bt;
    public static ZephyrProtocol _protocol;
    public static ConnectionListener _NConnListener;
    public static ConnectionListener instantHRListener;
    public static ConnectionListener stressLevelListener;
    public static String BhMacID;
    public static String deviceName;

    public static int [] recentInstantHR;

    public static int [] recentNn50;
    public static int nnCount;

    public static double SDSum = 0;
    public static double SDCount = 0;

    public static GraphViewSeries instantHRSeries = new GraphViewSeries(
			"Instant HR Curve", new GraphViewSeriesStyle(
					Color.rgb(20, 20, 255), 5), new GraphViewData[] {});
    public static GraphViewSeries idealBreathingCycle = new GraphViewSeries(
			"Ideal Breathing Curve", new GraphViewSeriesStyle(
					Color.rgb(255, 20, 20), 5), new GraphViewData[] {});
	
	// HACK: used to move the viewport the way we want
    public static GraphViewSeries dummySeries = new GraphViewSeries(
			"Ideal Breathing Curve", new GraphViewSeriesStyle(
					Color.rgb(225, 225, 225), 0), new GraphViewData[] {});
}
