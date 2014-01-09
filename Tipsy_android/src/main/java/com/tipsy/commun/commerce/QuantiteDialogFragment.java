package com.tipsy.commun.commerce;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;

/**
 * Created by valoo on 04/01/14.
 */
// Dialog Fragment permettant de choisir le nombre d'item voulu
public class QuantiteDialogFragment extends DialogFragment {

    private Item item;
    private ItemArrayAdapter adapter;
    private NumberPicker quantite = null;

    public QuantiteDialogFragment(ItemArrayAdapter adapter, Item i) {
        super();
        item = i;
        this.adapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setRetainInstance(true);
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Definition du titre du Dialog
        builder.setTitle("Quantité");
        // Definition du contenu du Dialog
        View view = inflater.inflate(R.layout.frag_item_quantite_picker, null);
        quantite = (NumberPicker) view.findViewById(R.id.quantite);
        quantite.setMinValue(0);
        quantite.setMaxValue(99);
        quantite.setValue(item.getQuantite());
        builder.setView(view);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                TipsyApp app = (TipsyApp) getActivity().getApplication();
                Panier panier = app.getPanier();
                panier.remove(item);
                item.setQuantite(quantite.getValue());

                if (quantite.getValue() > 0) {
                    panier.add(new Item(item.getProduit(), quantite.getValue()));
                }
                adapter.notifyDataSetChanged();
                panier.notifyItemsUpdated();
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
