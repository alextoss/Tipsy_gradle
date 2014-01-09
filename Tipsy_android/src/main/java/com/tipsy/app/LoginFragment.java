package com.tipsy.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.stackmob.sdk.callback.StackMobModelCallback;
import com.stackmob.sdk.exception.StackMobException;

import com.tipsy.commun.User;

/**
 * Created by Alexandre on 08/01/14.
 */
public class LoginFragment extends Fragment implements Validator.ValidationListener {

    @Required(order = 1, message = "Email manquant")
    @Email(order = 2, message = "Email Incorrect")
    protected EditText inputEmail;
    @Required(order = 3, message = "Mot de passe manquant")
    protected EditText inputPassword;
    protected Validator validator;
    protected Button signin;
    protected Button signup;
    protected Button help;
    protected Button forgetpwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_login, container, false);
        validator = new Validator(this);
        validator.setValidationListener(this);
        inputEmail = (EditText) view.findViewById(R.id.input_email);
        inputPassword = (EditText) view.findViewById(R.id.input_password);
        signin = (Button) view.findViewById(R.id.button_signin);
        signup = (Button) view.findViewById(R.id.button_signup);
        help = (Button) view.findViewById(R.id.button_help);
        forgetpwd = (Button) view.findViewById(R.id.forgotpass);
        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validator.validate();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TypeSignUpActivity.class));
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HelpActivity.class));
            }
        });
        forgetpwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LoginActivity.getPager().setCurrentItem(1, true);
            }
        });
        return view;
    }

    public void onValidationSucceeded() {
        final User user = new User(inputEmail.getText().toString(), inputPassword.getText().toString());
        user.fetch(new StackMobModelCallback() {
            @Override
            public void success() {
                if (user.isTemp_pwd()) {
                    user.loginResettingTemporaryPassword("connard", new StackMobModelCallback() {
                        @Override
                        public void success() {
                            user.setTemp_pwd(false);
                            user.save(new StackMobModelCallback() {
                                @Override
                                public void success() {
                                    // Sauvegarde locale des identifiants pour connexion auto
                                    User.rememberMe(getActivity(), inputEmail.getText().toString(), "connard");
                                    // Redirection en fonction du type utilisateur
                                    User.keepCalmAndWaitForGoingHome(getActivity(), user);
                                }

                                @Override
                                public void failure(StackMobException e) {

                                }
                            });
                        }

                        @Override
                        public void failure(StackMobException e) {
                            //login failed
                        }
                    });
                } else {
                    user.login(new StackMobModelCallback() {
                        @Override
                        public void success() {
                            // Sauvegarde locale des identifiants pour connexion auto
                            User.rememberMe(getActivity(), inputEmail.getText().toString(), inputPassword.getText().toString());
                            // Redirection en fonction du type utilisateur
                            User.keepCalmAndWaitForGoingHome(getActivity(), user);
                        }

                        @Override
                        public void failure(StackMobException e) {
                            Log.d("TOUTAFAIT", "login " + e.getMessage());
                            Toast.makeText(getActivity().getApplicationContext(), "Identifiants incorrects", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void failure(StackMobException e) {
                Log.d("TOUTAFAIT", "forget1" + e.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), "Email non existant", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
