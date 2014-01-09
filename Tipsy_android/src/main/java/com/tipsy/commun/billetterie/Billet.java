package com.tipsy.commun.billetterie;

import android.os.Parcel;
import android.os.Parcelable;

import com.tipsy.commun.commerce.Produit;

/**
 * Created by valoo on 27/12/13.
 */
public class Billet extends Produit implements ParametresBillet.ParamBillet {

    public Billet() {
        super();
        this.typeProduit = Produit.BILLET;
        this.parametresBillet = new ParametresBillet();
    }


    /* Raccourci vers les propriétés du billet */
    public int getNbMax() {
        return parametresBillet.getNbMax();
    }

    public void setNbMax(int nbMax) {
        this.parametresBillet.setNbMax(nbMax);
    }


    // Nom du schema dans Stackmob
    public static String overrideSchemaName() {
        return "produit";
    }

    /* Implementation Parcelable */
    public Billet(Parcel in) {
        super(in);
    }

    public static final Parcelable.Creator<Billet> CREATOR = new Parcelable.Creator<Billet>() {
        @Override
        public Billet createFromParcel(Parcel source) {
            return new Billet(source);
        }

        @Override
        public Billet[] newArray(int size) {
            return new Billet[size];
        }
    };
}
