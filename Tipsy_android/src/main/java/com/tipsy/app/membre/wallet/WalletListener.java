package com.tipsy.app.membre.wallet;

import com.tipsy.commun.commerce.Transaction;

/**
 * Created by valoo on 04/01/14.
 */
public interface WalletListener {
    public void goToResume(boolean addToBackStack);

    public void goToCredit();

    public void goToDetailsTransaction(Transaction t);

    public void goToCommande(boolean addToBackStack);
}
