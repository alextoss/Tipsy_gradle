package com.tipsy.app.membre;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.app.TipsyApp;
import com.tipsy.commun.commerce.Commerce;

/**
 * Created by Alexandre on 23/12/13.
 */
public class HomeMembreFragment extends Fragment {

    private MembreListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (MembreListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_membre_home, container, false);

        /* Initialisation du montant du solde du Wallet Membre */
        TextView viewSolde = (TextView) view.findViewById(R.id.solde);
        TipsyApp app = (TipsyApp) getActivity().getApplication();
        viewSolde.setText(Commerce.prixToString(app.getWallet().getSolde(), app.getWallet().getDevise()));

        LinearLayout buttonAccount = (LinearLayout) view.findViewById(R.id.button_account);
        buttonAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToAccount();
            }
        });

        LinearLayout buttonWallet = (LinearLayout) view.findViewById(R.id.button_wallet);
        buttonWallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callback.goToWallet();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setMenuTitle(MenuMembre.ACCUEIL);
    }
}