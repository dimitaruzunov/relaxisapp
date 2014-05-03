package com.relaxisapp.relaxis.widgets;

import java.util.ArrayList;

import android.support.v7.app.ActionBar;
import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.relaxisapp.relaxis.R;

public class NavigationDrawerListAdapter extends BaseAdapter {

	public final static int HOME_OPTION_ITEM = 0;
	public final static int BREATHING_OPTION_ITEM = 1;
	public final static int STRESS_OPTION_ITEM = 2;
	public final static int LOGIN_OPTION_ITEM = 3;
	
	private Context context;
    private ArrayList<NavigationDrawerItem> navigationDrawerItems;
	private DrawerLayout drawerLayout;
	private ListView drawerListView;
	private ActionBarDrawerToggle drawerToggle;
	
	public NavigationDrawerListAdapter(Context context, ArrayList<NavigationDrawerItem> navigationDrawerItems) {
        this.context = context;
        this.navigationDrawerItems = navigationDrawerItems;
    }
	
	@Override
	public int getCount() {
		return navigationDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navigationDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(ActionBarActivity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.drawer_option_item, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

        title.setText(navigationDrawerItems.get(position).getTitle());
        icon.setBackgroundColor(navigationDrawerItems.get(position).getColor());
        icon.setImageResource(navigationDrawerItems.get(position).getIcon());
         
        return convertView;
	}

    public void setup(ActionBarActivity activity, ListView.OnItemClickListener listener) {
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawerListView = (ListView) activity.findViewById(R.id.left_drawer);
        
        drawerListView.setOnItemClickListener(listener);
        
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        setupActionBar(activity);
    }
    
    private void setupActionBar(ActionBarActivity activity) {
    	final ActionBarActivity activityConst = activity;
    	ActionBar actionBar = activity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        drawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, R.drawable.ic_navigation_drawer,
    			R.string.open_drawer_message, R.string.close_drawer_message) {
    		@Override
    		public void onDrawerOpened(View drawerView) {
    			activityConst.supportInvalidateOptionsMenu();
    			super.onDrawerOpened(drawerView);
    		}
    		
    		@Override
    		public void onDrawerClosed(View drawerView) {
    			activityConst.supportInvalidateOptionsMenu();
    			super.onDrawerClosed(drawerView);
    		}
    	};
    }

    public void handleSelect(int option) {
    	switch (option) {
		case SectionsPagerAdapter.HOME_FRAGMENT:
			setSelection(HOME_OPTION_ITEM);
			break;
		case SectionsPagerAdapter.BREATHING_FRAGMENT:
			setSelection(BREATHING_OPTION_ITEM);
			break;
		case SectionsPagerAdapter.STRESS_FRAGMENT:
			setSelection(STRESS_OPTION_ITEM);
			break;
		case SectionsPagerAdapter.LOGIN_FRAGMENT:
			setSelection(LOGIN_OPTION_ITEM);
			break;
		default:
			// Drawer option doesn't exist
			break;
		}
    }
    
    public void closeDrawer() {
    	drawerLayout.closeDrawer(drawerListView);
    }
    
    public void handleOnPrepareOptionsMenu(Menu menu) {
    	boolean itemVisible = !drawerLayout.isDrawerOpen(drawerListView);
    	
    	for (int index = 0; index < menu.size(); index++) {
    		MenuItem item = menu.getItem(index);
    		item.setEnabled(itemVisible);
    	}
    }
    
    public void handleOnOptionsItemSelected(MenuItem item) {
    	drawerToggle.onOptionsItemSelected(item);
    }
    
    public void syncState() {
    	drawerToggle.syncState();
    }
    
    public void setSelection(int option) {
    	drawerListView.setItemChecked(option, true);
    }
	
}
