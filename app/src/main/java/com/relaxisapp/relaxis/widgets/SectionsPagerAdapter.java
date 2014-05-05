package com.relaxisapp.relaxis.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;

import com.relaxisapp.relaxis.R;
import com.relaxisapp.relaxis.activities.BreathingFragment;
import com.relaxisapp.relaxis.activities.HomeFragment;
import com.relaxisapp.relaxis.activities.LoginFragment;
import com.relaxisapp.relaxis.activities.StressEstimationFragment;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
	
	public final static int LOGIN_FRAGMENT = 0;
	public final static int HOME_FRAGMENT = 1;
	public final static int BREATHING_FRAGMENT = 2;
	public final static int STRESS_FRAGMENT = 3;
	
	String[] sectionTitles;
	
	public SectionsPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		
		Resources resources = context.getResources();
        sectionTitles = resources.getStringArray(R.array.section_titles);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = null;
		
		switch (position) {
		case LOGIN_FRAGMENT:
			fragment = new LoginFragment();
			break;
		case HOME_FRAGMENT:
			fragment = new HomeFragment();
			break;
		case BREATHING_FRAGMENT:
			fragment = new BreathingFragment();
			break;
		case STRESS_FRAGMENT:
			fragment = new StressEstimationFragment();
			break;
		}
		
		return fragment;
	}

	@Override
	public int getCount() {
		return sectionTitles.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return sectionTitles[position];
	}
	
	public void setFragment(int position, ViewPager viewPager) {
		switch (position) {
		case NavigationDrawerListAdapter.HOME_OPTION_ITEM:
			viewPager.setCurrentItem(HOME_FRAGMENT);
			break;
		case NavigationDrawerListAdapter.BREATHING_OPTION_ITEM:
			viewPager.setCurrentItem(BREATHING_FRAGMENT);
			break;
		case NavigationDrawerListAdapter.STRESS_OPTION_ITEM:
			viewPager.setCurrentItem(STRESS_FRAGMENT);
			break;
		case NavigationDrawerListAdapter.LOGIN_OPTION_ITEM:
			viewPager.setCurrentItem(LOGIN_FRAGMENT);
			break;
		}
	}

}
