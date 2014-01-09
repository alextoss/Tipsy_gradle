package com.tipsy.commun.commerce;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

import com.tipsy.commun.billetterie.ParametresBillet;

/**
 * Created by valoo on 04/01/14.
 */


public abstract class Produit extends StackMobModel implements Parcelable {

    protected int devise = Commerce.Devise.EURO;
    private String nom = "Mon Tarif";
    protected ParametresBillet parametresBillet = null;
    protected int prix = 0;
    protected int typeProduit;

    public static int BILLET = 0;
    public static int TICKET = 1;
    public static int CONSO = 2;

    protected Produit() {
        super(Produit.class);
    }

    public int getDevise() {
        return devise;
    }

    public void setDevise(int devise) {
        this.devise = devise;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    @Override
    public boolean equals(Object o) {
        return (this.getID() == ((Produit) o).getID());
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getID());
        dest.writeInt(devise);
        dest.writeString(nom);
        dest.writeParcelable(parametresBillet, flags);
        dest.writeInt(prix);
        dest.writeInt(typeProduit);
    }

    public Produit(Parcel in) {
        super(Produit.class);
        setID(in.readString());
        devise = in.readInt();
        nom = in.readString();
        parametresBillet = in.readParcelable(ParametresBillet.class.getClassLoader());
        prix = in.readInt();
        typeProduit = in.readInt();
    }
}
