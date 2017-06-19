package com.example.student.userphotograph.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student.userphotograph.R;
import com.example.student.userphotograph.activityes.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText mEmailEd;
    private EditText mPasswordEd;
    private Button mSignInbtn;

    public SignInFragment() {
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        findIdAndListeners(rootView);
        mSignInbtn.setClickable(true);
        return rootView;
    }

    private void findIdAndListeners(View rootView) {
        mEmailEd = (EditText) rootView.findViewById(R.id.et_sign_in_email);
        mPasswordEd = (EditText) rootView.findViewById(R.id.et_sign_in_password);
        TextView mForgotTv = (TextView) rootView.findViewById(R.id.tv_forgot_password);
        TextView mSignUpTv = (TextView) rootView.findViewById(R.id.dont_have_sign_up);
        mSignInbtn = (Button) rootView.findViewById(R.id.sign_in_button);
        mSignInbtn.setOnClickListener(this);
        mForgotTv.setOnClickListener(this);
        mSignUpTv.setOnClickListener(this);
    }

    public void signIn(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "invalid email or password", Toast.LENGTH_SHORT).show();
            mSignInbtn.setClickable(true);
        } else if (!email.isEmpty() && !password.isEmpty()){
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        mSignInbtn.setClickable(false);
                        Toast.makeText(getContext(), "Seuccessful sign in", Toast.LENGTH_SHORT).show();
                        Intent goToHomeActivity = new Intent(getContext(), HomeActivity.class);
                        startActivity(goToHomeActivity);
                    } else
                        mSignInbtn.setClickable(true);
                    Toast.makeText(getContext(), "Unseuccessful sign in", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addFragment(Fragment fragment) {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.containerLogin, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button: {
                mSignInbtn.setClickable(false);
                signIn(mEmailEd.getText().toString(), mPasswordEd.getText().toString());
                break;
            }
            case R.id.dont_have_sign_up: {
                addFragment(SignUpFragment.newInstance());
                break;
            }
            case R.id.tv_forgot_password: {
                addFragment(ForgotPasswordFragment.newInstance());
                break;
            }
        }
    }
}
