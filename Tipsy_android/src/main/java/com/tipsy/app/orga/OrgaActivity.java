package com.tipsy.app.orga;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.stackmob.sdk.api.StackMobOptions;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import com.tipsy.app.HelpActivity;
import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.app.UserActivity;
import com.tipsy.app.billetterie.BilletterieActivity;
import com.tipsy.commun.Event;
import com.tipsy.commun.Organisateur;

/**
 * Created by Valoo on 05/12/13.
 */
public class OrgaActivity extends UserActivity implements OrgaListener {

    private TipsyApp app;
    private Organisateur orga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.act_user);
        super.onCreate(savedInstanceState);
        this.menu = new MenuOrga(this);
        menu.initAdapter(new UserActivity.DrawerItemClickListener());

        app = (TipsyApp) getApplication();
        orga = app.getOrga();
        if (savedInstanceState == null) {
            goToTableauDeBord(false);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_orga, menu);
        return super.onCreateOptionsMenu(menu);
    }


    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    protected void selectItem(int position) {
        // update selected item and title, then close the drawer
        this.menu.getDrawerList().setItemChecked(position, true);
        this.menu.getDrawerList().setSelection(position);
        this.menu.getDrawerLayout().closeDrawer(this.menu.getDrawerList());
        switch (position) {
            case MenuOrga.ACCUEIL:
                goToTableauDeBord(true);
                break;
            case MenuOrga.MON_COMPTE:
                goToAccount();
                break;
            case MenuOrga.EVENEMENTS:
                goToEvents();
                break;
            case MenuOrga.AIDE:
                Intent intent = new Intent(this, HelpActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("Connected", true);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case MenuOrga.DECONNEXION:
                app.logout(this);
                break;
        }

    }


    // IMPLEMENTATIONS DES LISTENERS DU MODULE ORGANISATEUR

    // clique sur le bouton de la Billetterie
    public void onBilletterieEdit(Event e) {
        Intent intent = new Intent(this, BilletterieActivity.class);
        Bundle b = new Bundle();
        b.putParcelable("Event", e);
        intent.putExtras(b);
        startActivity(intent);
    }

    // Clique sur le bouton "Créer un événement"
    public void onClickResumeEvent(Event e) {
        EventHomeFragment frag = EventHomeFragment.init(e);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, frag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    // Clique sur le bouton "Créer un événement" ou Modifications de l'event
    public void onEventEdit(Event e, boolean create) {
        EditEventFragment frag = EditEventFragment.init(e, create);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, frag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }

    // Création/Modification d'un événement terminée
    public void onEventEdited() {
        orga.save(StackMobOptions.depthOf(2), new StackMobModelCallback() {
            @Override
            public void success() {
                goToTableauDeBord(true);
            }

            @Override
            public void failure(StackMobException e) {
                Log.e("TOUTAFAIT", "erreur sauvegarde Event:" + e.getMessage());
            }
        });
    }

    public void goToTableauDeBord(boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new HomeOrgaFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (addToBackStack)
            ft.addToBackStack(null);
        ft.commit();
    }

    public void goToAccount() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.content, new AccountOrgaFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }


    public void goToEvents() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.content, new EventsOrgaFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }


}
