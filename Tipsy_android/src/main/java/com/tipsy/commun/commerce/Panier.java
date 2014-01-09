package com.tipsy.commun.commerce;

import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by valoo on 04/01/14.
 */
public class Panier<E> extends HashSet<E> {

    protected int devise = Commerce.Devise.EURO;
    private TextView viewPrixTotal = null;

    public int getDevise() {
        return devise;
    }

    public void setDevise(int devise) {
        this.devise = devise;
    }

    @Override
    public boolean add(E object) {
        boolean res = super.add(object);
        return res;
    }

    public int getPrixTotal() {
        Iterator it = iterator();
        int prixTotal = 0;
        Item item;
        while (it.hasNext()) {
            item = (Item) it.next();
            prixTotal += item.getPrixTotal();
        }
        return prixTotal;
    }

    public void setPrixTotalView(TextView prixTotal) {
        this.viewPrixTotal = prixTotal;
    }

    public void notifyItemsUpdated() {
        if (viewPrixTotal != null) {
            viewPrixTotal.setText(Commerce.prixToString(getPrixTotal(), devise));
        }
    }
}
