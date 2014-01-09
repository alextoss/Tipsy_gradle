package com.tipsy.commun.commerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.tipsy.app.R;

/**
 * Created by valoo on 04/01/14.
 */

// Adapter Item
public class ItemArrayAdapter extends ArrayAdapter<Item> {
    private Context context;
    private ArrayList<Item> items;

    public ItemArrayAdapter(Context context, ArrayList<Item> items) {
        super(context, R.layout.frag_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Définition de la vue
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.frag_item, parent, false);

        // Initialisation des conteneurs
        TextView nom = (TextView) view.findViewById(R.id.nom);
        TextView prix = (TextView) view.findViewById(R.id.prix);
        TextView quantite = (TextView) view.findViewById(R.id.quantite);

        // Item courant
        Item item = items.get(position);

        // Définition des valeurs
        nom.setText(item.getProduit().getNom());
        prix.setText(Commerce.prixToString(item.getProduit().getPrix(), item.getProduit().getDevise()));
        quantite.setText(Integer.toString(item.getQuantite()));

        return view;
    }
}
