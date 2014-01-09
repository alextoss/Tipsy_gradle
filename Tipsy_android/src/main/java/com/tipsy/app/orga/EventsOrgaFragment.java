package com.tipsy.app.orga;

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
public class EventsOrgaFragment extends Fragment {

    private OrgaListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OrgaListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.frag_orga_events, container, false);

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setMenuTitle(MenuOrga.EVENEMENTS);
    }
}
