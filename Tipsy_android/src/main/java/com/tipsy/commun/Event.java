package com.tipsy.commun;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

import java.util.ArrayList;
import java.util.Date;

import com.tipsy.commun.billetterie.Billet;
import com.tipsy.commun.billetterie.Billetterie;

/**
 * Created by Valentin on 07/12/13.
 */
public class Event extends StackMobModel implements Parcelable {

    private Billetterie<Billet> billetterie = new Billetterie<Billet>();
    private Date debut = new Date();
    private String lieu = null;
    private String nom = null;

    public Event() {
        super(Event.class);
    }

    public Event(String nom) {
        super(Event.class);
        this.nom = nom;
    }

    public Billetterie<Billet> getBilletterie() {
        return billetterie;
    }

    public void setBilletterie(ArrayList<Billet> billets) {
        this.billetterie.setBillets(billetterie);
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getID());
        dest.writeSerializable(debut);
        dest.writeString(lieu);
        dest.writeString(nom);
    }

    public Event(Parcel in) {
        super(Event.class);
        setID(in.readString());
        debut = (Date) in.readSerializable();
        lieu = in.readString();
        nom = in.readString();
    }

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

}
