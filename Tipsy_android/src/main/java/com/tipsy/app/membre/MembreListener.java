package com.tipsy.app.membre;

import com.tipsy.app.MenuListener;
import com.tipsy.commun.Event;
import com.tipsy.commun.Membre;
import com.tipsy.commun.billetterie.Billetterie;

/**
 * Created by Valentin on 29/12/13.
 */
public interface MembreListener extends MenuListener {
    public Membre getMembre();

    public void goToTableauDeBord(boolean addToBackStack);

    public void goToAccount();

    public void goToWallet();

    public void goToEvents();

    public void searchEventByKeyword(String query);

    public void goToEvent(Event e);

    public void goToEventBillets(Billetterie b);

    public void goToCommande();

}
