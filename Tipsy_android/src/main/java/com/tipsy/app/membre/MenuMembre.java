package com.tipsy.app.membre;

import android.app.Activity;

import com.tipsy.app.MenuUser;
import com.tipsy.app.R;

/**
 * Created by Alexandre on 18/12/13.
 */
public class MenuMembre extends MenuUser {
    public final static int ACCUEIL = 0;
    public final static int MON_COMPTE = 1;
    public final static int SOLDE = 2;
    public final static int EVENEMENTS = 3;
    public final static int AIDE = 4;
    public final static int DECONNEXION = 5;

    public MenuMembre(Activity a) {
        super(a);
        this.titres_menu = a.getResources().getStringArray(R.array.menu_membre);
        setTitre(titres_menu[ACCUEIL]);
    }
}
