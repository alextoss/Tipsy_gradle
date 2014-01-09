package com.tipsy.commun;


import com.stackmob.sdk.api.StackMobFile;
import com.stackmob.sdk.model.StackMobModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by valoo on 07/12/13.
 */
public class Organisateur extends StackMobModel implements User.TipsyUser {

    private ArrayList<Event> events;
    private String nom;
    private String telephone;
    private User user;
    private StackMobFile avatar;

    public Organisateur() {
        super(Organisateur.class);
        this.events = new ArrayList<Event>();
    }

    public Organisateur(String username, String password, String nom) {
        super(Organisateur.class);
        user = new User(username, password, TypeUser.ORGANISATEUR);
        this.events = new ArrayList<Event>();
        this.nom = nom;
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

    public ArrayList<Event> getEvents() {
        return events;
    }

    public Event creerEvent(String nom) {
        Event e = new Event(nom);
        events.add(e);
        return e;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getType() {
        return TypeUser.ORGANISATEUR;
    }

    public User getUser() {
        return user;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Event> getEventsByDate() {
        Collections.sort(events, new SortEventByDate());
        return events;
    }

    // Retourne l'event à venir le plus proche, ou null si aucun event n'est à venir
    public Event getUpcomingEvent() {
        // Tri les events par date croissante
        // Et retourne le premier dont la date est "future"
        ArrayList<Event> evs = getEventsByDate();
        Date today = new Date(); // date courante
        for (int i = 0; i < evs.size(); i++) {
            if (evs.get(i).getDebut().compareTo(today) > -1) {
                return evs.get(i);
            }
        }
        return null;
    }


    public class SortEventByDate implements Comparator<Event> {
        public int compare(Event a, Event b) {
            return a.getDebut().compareTo(b.getDebut());
        }
    }
}
