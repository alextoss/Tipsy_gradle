package com.tipsy.app;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by Alexandre on 18/12/13.
 */
public abstract class UserActivity extends FragmentActivity implements MenuListener {
    protected MenuUser menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard(UserActivity.this);
                return false;
            }
        });
    }

    protected void onStart() {
        super.onStart();
        setTitle(this.menu.getTitre());
        this.menu.getDrawerList().setItemChecked(MenuUser.ACCUEIL, true);
    }

    // Called whenever we call invalidateOptionsMenu()
    @Override
    public boolean onPrepareOptionsMenu(android.view.Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (this.menu.getDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        } else {
            return false;
        }
    }

    protected void selectItem(int position) {

    }

    // The click listener for ListView in the navigation drawer
    public class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    @Override
    public void setTitle(CharSequence title) {
        menu.setTitre(title);
        getActionBar().setTitle(title);
    }

    public void setMenuTitle(int titleId) {
        setTitle(this.menu.getTitres_menu()[titleId]);
        this.menu.getDrawerList().setItemChecked(titleId, true);
    }

    public void setMenuTitle(String title) {
        setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        this.menu.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        this.menu.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
