package com.tipsy.commun;

/**
 * Created by valoo on 07/12/13.
 */

import com.stackmob.sdk.api.StackMobFile;
import com.stackmob.sdk.model.StackMobModel;

import java.util.ArrayList;

import com.tipsy.commun.commerce.Transaction;


public class Membre extends StackMobModel implements User.TipsyUser {

    private StackMobFile avatar = null;
    private String nom;
    private String prenom;
    private User user;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();

    public Membre() {
        super(Membre.class);
    }

    public Membre(String username, String password, String nom, String prenom) {
        super(Membre.class);
        user = new User(username, password, TypeUser.MEMBRE);
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public StackMobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(StackMobFile avatar) {
        this.avatar = avatar;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getType() {
        return TypeUser.MEMBRE;
    }

    public User getUser() {
        return user;
    }

}
