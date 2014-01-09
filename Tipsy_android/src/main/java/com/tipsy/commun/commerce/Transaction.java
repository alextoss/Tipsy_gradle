package com.tipsy.commun.commerce;

import android.os.Parcel;
import android.os.Parcelable;

import com.stackmob.sdk.model.StackMobModel;

import java.util.Date;

import com.tipsy.commun.User;

/**
 * Created by valoo on 05/01/14.
 */
public class Transaction extends StackMobModel implements Parcelable {

    private User auteur;
    private Date date;
    private User destinataire;
    private int devise;
    private int montant;

    public Transaction() {
        super(Transaction.class);
    }

    public Transaction(int montant, User destinataire) {
        super(Transaction.class);
        this.auteur = null;
        this.date = new Date();
        this.destinataire = destinataire;
        this.montant = montant;
    }

    public Transaction(int montant, User destinataire, User auteur) {
        super(Transaction.class);
        this.auteur = auteur;
        this.date = new Date();
        this.destinataire = destinataire;
        this.montant = montant;
    }

    public User getAuteur() {
        return auteur;
    }

    public void setAuteur(User auteur) {
        this.auteur = auteur;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(User destinataire) {
        this.destinataire = destinataire;
    }

    public int getMontant() {
        return montant;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    /* La transaction est un crédit pour le User u si et seulement s'il en est le destinataire */
    public boolean isCredit(User u) {
        return this.destinataire.getUsername().equals(u.getUsername());
    }

    /* La transaction est un debit pour le User u si et seulement s'il en est l'auteur */
    public boolean isDebit(User u) {
        return this.auteur.getUsername().equals(u.getUsername());
    }

    public String getMontantToString() {
        return Commerce.prixToString(montant, devise);
    }


    // Implémentation de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getID());
        dest.writeParcelable(auteur, flags);
        dest.writeSerializable(date);
        dest.writeParcelable(destinataire, flags);
        dest.writeInt(montant);
    }

    public Transaction(Parcel in) {
        super(Transaction.class);
        setID(in.readString());
        auteur = in.readParcelable(User.class.getClassLoader());
        date = (Date) in.readSerializable();
        destinataire = in.readParcelable(User.class.getClassLoader());
        montant = in.readInt();
    }

    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
}
