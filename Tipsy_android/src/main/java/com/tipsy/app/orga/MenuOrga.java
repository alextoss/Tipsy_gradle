package com.tipsy.app.orga;

import android.app.Activity;

import com.tipsy.app.MenuUser;
import com.tipsy.app.R;

/**
 * Created by Alexandre on 18/12/13.
 */
public class MenuOrga extends MenuUser {
    public final static int ACCUEIL = 0;
    public final static int MON_COMPTE = 1;
    public final static int EVENEMENTS = 2;
    public final static int AIDE = 3;
    public final static int DECONNEXION = 4;

    public MenuOrga(Activity a) {
        super(a);
        titres_menu = a.getResources().getStringArray(R.array.menu_organisateur);
        setTitre(titres_menu[ACCUEIL]);
    }
}
