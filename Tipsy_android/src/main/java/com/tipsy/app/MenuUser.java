package com.tipsy.app;

import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Alexandre on 15/12/13.
 */
public abstract class MenuUser {
    protected static int ACCUEIL = 0;
    protected DrawerLayout drawerLayout;
    protected ListView drawerList;
    protected ActionBarDrawerToggle drawerToggle;
    protected Activity activity;
    protected CharSequence drawerTitle;
    protected CharSequence titre;
    protected String[] titres_menu;

    public MenuUser(Activity a) {
        activity = a;
        activity.overridePendingTransition(R.animator.right_to_left, R.animator.activity_close_scale);

        drawerLayout = (DrawerLayout) activity.findViewById(R.id.drawer_Layout);
        drawerList = (ListView) activity.findViewById(R.id.left_drawer);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        activity.getActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = new ActionBarDrawerToggle(
                activity,                  // host Activity
                drawerLayout,         // DrawerLayout object
                R.drawable.ic_drawer,  // nav drawer image to replace 'Up' caret
                R.string.drawer_open,  // "open drawer" description for accessibility
                R.string.drawer_close  // "close drawer" description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                activity.getActionBar().setTitle(titre);
                activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                activity.getActionBar().setTitle(titre);
                activity.invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    public void initAdapter(UserActivity.DrawerItemClickListener listener) {
        // set up the drawer's list view with items and click listener
        getDrawerList().setAdapter(new ArrayAdapter<String>(activity,
                R.layout.frag_drawer_list_item, titres_menu));
        getDrawerList().setOnItemClickListener(listener);
    }

    public boolean isDrawerOpen() {
        return getDrawerLayout().isDrawerOpen(getDrawerList());
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public ListView getDrawerList() {
        return drawerList;
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    public String[] getTitres_menu() {
        return titres_menu;
    }

    public CharSequence getTitre() {
        return titre;
    }

    public void setDrawerTitle(CharSequence drawerTitle) {
        this.drawerTitle = drawerTitle;
    }

    public CharSequence getDrawerTitle() {
        return drawerTitle;
    }

    public void setTitre(CharSequence titre) {
        this.titre = titre;
    }
}
