package com.tipsy.commun.commerce;


/**
 * Created by valoo on 04/01/14.
 */
public class Item {
    private Produit produit;
    private int quantite;

    public Item(Produit a, int q) {
        produit = a;
        quantite = q;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getPrixTotal() {
        return produit.getPrix() * quantite;
    }

    @Override
    public boolean equals(Object o) {
        return this.produit.equals(((Item) o).getProduit());
    }

    @Override
    public int hashCode() {
        return produit.getID().hashCode();
    }
}
