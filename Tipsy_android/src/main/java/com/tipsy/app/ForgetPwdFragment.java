package com.tipsy.app;

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
public class ForgetPwdFragment extends Fragment implements Validator.ValidationListener {

    @Required(order = 1, message = "Email manquant")
    @Email(order = 2, message = "Email Incorrect")
    protected EditText inputForget;
    protected Button reset;
    protected Validator validator_pwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_forget_pwd, container, false);
        validator_pwd = new Validator(this);
        validator_pwd.setValidationListener(this);
        inputForget = (EditText) view.findViewById(R.id.input_email_forget);
        reset = (Button) view.findViewById(R.id.resetpwd);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                validator_pwd.validate();
            }
        });
        return view;
    }

    public void onValidationSucceeded() {
        final User user = new User(inputForget.getText().toString());
        user.fetch(new StackMobModelCallback() {
            @Override
            public void success() {
                user.sentForgotPasswordEmail(inputForget.getText().toString(), new StackMobModelCallback() {
                    @Override
                    public void success() {
                        user.setSocial(true);
                        user.save(new StackMobModelCallback() {
                            @Override
                            public void success() {
                                LoginActivity.getPager().setCurrentItem(0, true);
                            }

                            @Override
                            public void failure(StackMobException e) {

                            }
                        });
                    }

                    @Override
                    public void failure(StackMobException e) {
                        Log.d("TOUTAFAIT", "forget2" + e.getMessage());
                    }
                });
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
