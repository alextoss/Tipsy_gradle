package com.tipsy.app.orga;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tipsy.app.R;

/**
 * Created by Valoo on 05/12/13.
 */

public class EditEventDescFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_edit_event_desc, container, false);
        ((EditEventFragment) getParentFragment()).onDescFragCreated(view);
        return view;
    }
}
