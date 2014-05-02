package com.relaxisapp.relaxis;

public class NavigationDrawerItem {

	private String title;
    private int color;
	private int icon;
	
	public NavigationDrawerItem() {}
	
	public NavigationDrawerItem(String title, int color, int icon) {
		this.title = title;
        this.color = color;
        this.icon = icon;
	}
	
	public String getTitle() {
        return this.title;
    }

    public int getColor() {
        return this.color;
    }
     
    public int getIcon() {
        return this.icon;
    }
	
    public void setTitle(String title) {
        this.title = title;
    }

    public void setColor(int color) {
        this.color = color;
    }
     
    public void setIcon(int icon) {
        this.icon = icon;
    }
    
}
