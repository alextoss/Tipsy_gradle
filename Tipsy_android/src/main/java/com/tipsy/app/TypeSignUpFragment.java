package com.tipsy.app;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.plus.PlusClient;

import com.stackmob.sdk.api.StackMobQuery;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.callback.StackMobQueryCallback;
import com.stackmob.sdk.exception.StackMobException;
import com.tipsy.commun.Membre;
import com.tipsy.commun.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandre on 04/01/14.
 */
public class TypeSignUpFragment extends Fragment implements ConnectionCallbacks, OnConnectionFailedListener {

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private Button mSignInButton;
    private ConnectionResult mConnectionResult;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_type_signup, container, false);

        mPlusClient = new PlusClient.Builder(getActivity(), this, this)
                .setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .build();
        // Progress bar to be displayed if the connection failure is not resolved.
        mConnectionProgressDialog = new ProgressDialog(getActivity());
        mConnectionProgressDialog.setMessage("Connexion en cours...");

        mSignInButton = (Button) view.findViewById(R.id.sign_in_button);
        mSignInButton.findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (v.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
                    if (mConnectionResult == null) {
                        mConnectionProgressDialog.show();
                    } else {
                        try {
                            mConnectionResult.startResolutionForResult(getActivity(), REQUEST_CODE_RESOLVE_ERR);
                            StackMobQuery query = new StackMobQuery().fieldIsEqualTo("username", mPlusClient.getAccountName());

                            Membre membre = new Membre(
                                    mPlusClient.getAccountName(),
                                    "social",
                                    mPlusClient.getCurrentPerson().getName().getFamilyName(),
                                    mPlusClient.getCurrentPerson().getName().getGivenName()
                            );
                            /*membre.query(TypeSignUpFragment.class,query, new StackMobQueryCallback<>(){


                                @Override
                                public void success(List<TypeSignUpFragment> typeSignUpFragments) {

                                }

                                @Override
                                public void failure(StackMobException e) {

                                }
                            });*/
                            User.rememberMe(getActivity(), mPlusClient.getAccountName(), "social");
                            signUpUser(membre);
                        } catch (IntentSender.SendIntentException e) {
                            // Try connecting again.
                            mConnectionResult = null;
                            mPlusClient.connect();
                        }
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPlusClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPlusClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mConnectionProgressDialog.isShowing()) {
            // The user clicked the sign-in button already. Start to resolve
            // connection errors. Wait until onConnected() to dismiss the
            // connection dialog.
            if (result.hasResolution()) {
                try {
                    result.startResolutionForResult(getActivity(), REQUEST_CODE_RESOLVE_ERR);
                } catch (IntentSender.SendIntentException e) {
                    mPlusClient.connect();
                }
            }
        }

        // Save the intent so that we can start an activity when the user clicks
        // the sign-in button.
        mConnectionResult = result;
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == getActivity().RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mConnectionProgressDialog.dismiss();
        //Toast.makeText(getActivity(), mPlusClient.getAccountName() + " est connecté.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisconnected() {
        Log.d("Signup", "disconnected");
    }

    protected void signUpUser(final User.TipsyUser tipsyUser) {
        // Inscription du User
        tipsyUser.getUser().setTemp_pwd(true);
        tipsyUser.getUser().save(new StackMobModelCallback() {
            //Connexion auto
            @Override
            public void success() {
                tipsyUser.getUser().login(new StackMobModelCallback() {
                    //Enregistrement du tipsyUser
                    @Override
                    public void success() {
                        tipsyUser.save(new StackMobModelCallback() {
                            // Direction page d'accueil
                            @Override
                            public void success() {
                                User.keepCalmAndWaitForGoingHome(getActivity(), tipsyUser.getUser());
                            }

                            @Override
                            public void failure(StackMobException e) {
                                Log.d("TOUTAFAIT", "save orga/membre" + e.getMessage());
                            }

                        });
                    }

                    @Override
                    public void failure(StackMobException e) {
                        Log.d("TOUTAFAIT", "login" + e.getMessage());
                    }
                });
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT", "save user" + e.getMessage());
            }

        });
    }
}
