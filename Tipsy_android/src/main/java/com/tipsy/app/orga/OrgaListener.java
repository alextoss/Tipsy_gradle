package com.tipsy.app.orga;

import com.tipsy.app.MenuListener;
import com.tipsy.commun.Event;

/**
 * Created by valoo on 22/12/13.
 */
public interface OrgaListener extends MenuListener {
    public void onBilletterieEdit(Event e);

    public void onClickResumeEvent(Event e);

    public void onEventEdit(Event e, boolean create);

    public void onEventEdited();

    public void goToTableauDeBord(boolean addToBackStack);

    public void goToAccount();

    public void goToEvents();
}
