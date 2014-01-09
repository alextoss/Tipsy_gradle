package com.tipsy.app.membre.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.commun.commerce.Commande;
import com.tipsy.commun.commerce.Commerce;
import com.tipsy.commun.commerce.ItemArrayAdapter;

/**
 * Created by Valentin on 30/12/13.
 */
public class WalletCommandeFragment extends Fragment {

    private Commande commande;
    private WalletListener callback;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (WalletListener) activity;
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        commande = new Commande(app.getPanier());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_commande, container, false);

        /* On récupère le contenu de la commande */
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        commande = new Commande(app.getPanier());

        ItemArrayAdapter adapter = new ItemArrayAdapter(getActivity(), commande.getItems());
        ListView listView = (ListView) view.findViewById(R.id.list);
        listView.setAdapter(adapter);

        TextView viewPrixTotal = (TextView) view.findViewById(R.id.prix_total);
        viewPrixTotal.setText(Commerce.prixToString(commande.getPrixTotal(), commande.getDevise()));

        Button buttonPay = (Button) view.findViewById(R.id.button_pay);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        return view;
    }


    // Redéfinition de l'actionBar: Bouton de validation
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_wallet, menu);
        LinearLayout walletLayout = (LinearLayout) menu.findItem(R.id.action_wallet).getActionView();
        TextView montant = (TextView) walletLayout.findViewById(R.id.montant);
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        montant.setText(
                Commerce.prixToString(app.getWallet().getSolde(), app.getWallet().getDevise())
        );
    }

    // Gestion du click sur le bouton de validation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_wallet:
                Toast.makeText(getActivity(), "Hello World !", Toast.LENGTH_SHORT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
