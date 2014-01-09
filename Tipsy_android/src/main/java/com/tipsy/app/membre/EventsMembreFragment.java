package com.tipsy.app.membre;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tipsy.app.R;

/**
 * Created by Alexandre on 23/12/13.
 */
public class EventsMembreFragment extends Fragment {

    private MembreListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MembreListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_membre_event, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setMenuTitle(MenuMembre.EVENEMENTS);
    }
}
