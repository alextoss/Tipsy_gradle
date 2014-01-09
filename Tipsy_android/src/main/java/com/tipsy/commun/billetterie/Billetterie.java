package com.tipsy.commun.billetterie;

import java.util.ArrayList;
import java.util.Iterator;

import com.tipsy.commun.commerce.Item;
import com.tipsy.commun.commerce.Panier;
import com.tipsy.commun.commerce.Produit;

/**
 * Created by valoo on 27/12/13.
 */
public class Billetterie<Billet> extends ArrayList<Billet> {

    public void setBillets(ArrayList<Billet> billets) {
        Iterator b = billets.iterator();
        while (b.hasNext()) {
            add((Billet) b.next());
        }
    }

    /*
    Retourne la liste de billets convertis en items.
    La quantité de chaque item est définie à zéro, sauf si l'item est présent dans le panier de l'utilisateur
    auquel cas, on définie la quantité par celle définie dans le panier
    */

    public ArrayList<Item> getItems(Panier panier) {
        ArrayList<Item> items = new ArrayList<Item>();
        Item item;
        Iterator b = iterator();
        while (b.hasNext()) {
            // Quantité définie à zéro
            item = new Item((Produit) b.next(), 0);
            // Si des billets sont déjà dans le panier de l'utilisateur
            if (panier.contains(item)) {
                // On récupère l'item du panier pour avoir la quantité correspondante
                Iterator i = panier.iterator();
                Item itemCourant;
                while (i.hasNext()) {
                    itemCourant = (Item) i.next();
                    if (itemCourant.equals(item)) {
                        item.setQuantite(itemCourant.getQuantite());
                        break;
                    }
                }
            }
            items.add(item);
        }
        return items;
    }
}
