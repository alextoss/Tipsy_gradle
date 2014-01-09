package com.tipsy.app.orga;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.tipsy.app.R;
import com.tipsy.commun.Event;

/**
 * Created by Valoo on 05/12/13.
 */

public class EditEventDateFragment extends Fragment {

    private Event event;
    private TextView inputDateDebut;
    private TextView inputTimeDebut;
    public static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm");


    public static EditEventDateFragment init(Event e) {
        EditEventDateFragment frag = new EditEventDateFragment();
        Bundle args = new Bundle();
        args.putParcelable("Event", e);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_edit_event_date, container, false);
        event = (Event) getArguments().getParcelable("Event");


        /* DATE DEBUT EVENT */
        inputDateDebut = (TextView) view.findViewById(R.id.input_date_debut);
        final DatePickerDialog.OnDateSetListener dpDebut = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                GregorianCalendar cal = new GregorianCalendar(year, month, day);
                inputDateDebut.setText(dateFormatter.format(new Date(cal.getTimeInMillis())));
            }

        };
        inputDateDebut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(event.getDebut());
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getActivity(), dpDebut, year, month, day).show();
            }
        });


        /* HORAIRES DEBUT EVENT */
        inputTimeDebut = (TextView) view.findViewById(R.id.input_time_debut);
        final TimePickerDialog.OnTimeSetListener tpDebut = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int min) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, min);
                inputTimeDebut.setText(timeFormatter.format(new Date(cal.getTimeInMillis())));
            }

        };
        // Liaison des Date/Time Pickers aux inputs
        inputTimeDebut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(event.getDebut());
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int min = cal.get(Calendar.MINUTE);
                new TimePickerDialog(getActivity(), tpDebut, hour, min, true).show();
            }
        });

        ((EditEventFragment) getParentFragment()).onDateFragCreated(view);

        return view;
    }

}
