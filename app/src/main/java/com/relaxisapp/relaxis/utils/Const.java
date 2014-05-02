package com.relaxisapp.relaxis.utils;

public class Const {
	
	public static final int REQUEST_ENABLE_BT = 1000;
	
	// sensor communication code constants
    public static final int HEART_RATE = 0x100;
    public static final int INSTANT_SPEED = 0x101;
    public static final int RR_INTERVAL = 0x102;
    public static final int INSTANT_HR = 0x103;
    public static final int PNN50 = 0x104;

	// settings constants
    public static final int SAVED_HR_COUNT = 24;
    public static final int SAVED_NN50_COUNT = 180;
    public static final int TIMER_TICKS_PER_SECOND = 10;
	
	// breathing graph settings
    public static final int IDEAL_MID_HR = 73;
    public static final int IDEAL_HR_DEVIATION = 10;

    public static final int VIEWPORT_WIDTH = 24;

    public static final int POINT_BARRIER = 15;

    public static final int TIME_BREATHING_SECONDS = 30;
    public static final int TIME_STRESS_SECONDS = 120;

}
