package com.tipsy.app.orga;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.commun.Event;

/**
 * Created by valoo on 22/12/13.
 */
public class EditEventFragment extends Fragment implements ActionBar.TabListener, Validator.ValidationListener {

    private OrgaListener callback;
    private Event event;
    private boolean createEvent;

    private static final int NUM_ITEMS = 4;
    private final static int DESC = 0;
    private final static int LIEU = 1;
    private final static int DATE = 2;
    private final static int PARAMS = 3;
    private static final int[] icones = {
            R.drawable.ic_action_about,
            R.drawable.ic_action_place,
            R.drawable.ic_action_go_to_today,
            R.drawable.ic_action_settings
    };


    private ActionBar actionBar;
    private EditEventAdapter mAdapter;
    private ViewPager mPager;

    @Required(order = 1)
    private EditText inputNom;
    @Required(order = 2)
    private EditText inputLieu;

    private TextView inputDateDebut;
    private TextView inputTimeDebut;

    private Validator validator;


    public static EditEventFragment init(Event e, boolean create) {
        EditEventFragment frag = new EditEventFragment();
        Bundle args = new Bundle();
        args.putParcelable("Event", e);
        args.putBoolean("createEvent", create);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (OrgaListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_orga_edit_event, container, false);
        event = getArguments().getParcelable("Event");
        createEvent = getArguments().getBoolean("createEvent");
        mAdapter = new EditEventAdapter(getChildFragmentManager());
        mPager = (ViewPager) view.findViewById(R.id.event_pager);
        mPager.setAdapter(mAdapter);

        // DEFINITION DES TABS
        actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.removeAllTabs();
        for (int icon : icones) {
            actionBar.addTab(actionBar.newTab().setIcon(icon).setTabListener(this));
        }

        mPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the corresponding tab.
                        actionBar.setSelectedNavigationItem(position);
                    }
                }
        );

        return view;
    }


    // Redéfinition de l'actionBar: Bouton de validation
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_orga_edit_event, menu);
    }

    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_validate_event:
                validator = new Validator(EditEventFragment.this);
                validator.setValidationListener(EditEventFragment.this);
                // Validation du formulaire d'inscription
                validator.validate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Envoi de la demande de sauvegarde de l'événement à l'activité
    public void onValidationSucceeded() {
        // Si c'est une création d'event, on initialise l'event
        if (createEvent) {
            TipsyApp app = (TipsyApp) getActivity().getApplication();
            event = app.getOrga().creerEvent("");
        }
        if (inputNom != null) {
            event.setNom(inputNom.getText().toString());
        }
        if (inputLieu != null) {
            event.setLieu(inputLieu.getText().toString());
        }
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy kk:mm");
        String dateDebut = inputDateDebut.getText().toString() + " " + inputTimeDebut.getText().toString();
        try {
            event.setDebut(f.parse(dateDebut));
        } catch (ParseException e) {
        }
        callback.onEventEdited();
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    // Listener des tabs
    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
    }

    // Gestionnaire des Fragments
    public class EditEventAdapter extends FragmentPagerAdapter {
        public EditEventAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        // Affiche le fragment voulu en fonction de la position
        public Fragment getItem(int position) {
            // Dans l'ordre de gauche à droite
            switch (position) {
                case DESC:
                    return new EditEventDescFragment();
                case LIEU:
                    return new EditEventLocFragment();
                case DATE:
                    return EditEventDateFragment.init(event);
                default:
                    return new EditEventSettingsFragment();
            }
        }
    }


    // initialisation des inputs lors de leur affichage pour les rendre accessible au Validator

    // inputs partie description
    public void onDescFragCreated(View v) {
        inputNom = (EditText) v.findViewById(R.id.input_nom);
        inputNom.setText(event.getNom());
    }

    // inputs partie lieu
    public void onLocFragCreated(View v) {
        inputLieu = (EditText) v.findViewById(R.id.input_lieu);
        inputLieu.setText(event.getLieu());
    }

    // inputs partie date
    public void onDateFragCreated(View v) {
        inputDateDebut = (TextView) v.findViewById(R.id.input_date_debut);
        inputTimeDebut = (TextView) v.findViewById(R.id.input_time_debut);

        // Initialisation des dates de debut et de fin
        inputDateDebut.setText(EditEventDateFragment.dateFormatter.format(event.getDebut()));
        inputTimeDebut.setText(EditEventDateFragment.timeFormatter.format(event.getDebut()));
    }

}
