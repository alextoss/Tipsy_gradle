package com.tipsy.app.membre.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tipsy.app.R;
import com.tipsy.commun.commerce.Transaction;

/**
 * Created by Alexandre on 23/12/13.
 */
public class WalletDetailsTransactionFragment extends Fragment {

    private WalletListener callback;
    private Transaction transaction;


    public static WalletDetailsTransactionFragment init(Transaction t) {
        WalletDetailsTransactionFragment frag = new WalletDetailsTransactionFragment();
        Bundle args = new Bundle();
        args.putParcelable("Transaction", t);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (WalletListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_wallet_details_transaction, container, false);
        transaction = getArguments().getParcelable("Transaction");

        TextView montant = (TextView) view.findViewById(R.id.montant);
        montant.setText(transaction.getMontantToString());
        TextView user = (TextView) view.findViewById(R.id.destinataire);
        user.setText(transaction.getDestinataire().getID());
        return view;
    }
}
