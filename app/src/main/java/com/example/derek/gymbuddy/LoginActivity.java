package com.example.derek.gymbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.derek.gymbuddy.models.User;
import com.example.derek.gymbuddy.models.UserProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private static final String TAG = "LoginActivity";
    private String email, password, userId, username;
    private EditText txtEmail, txtPassword;
    private Button btnSignIn, btnSignUp;
    private int points = 0;
    private boolean signUpFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.fieldEmail);
        txtPassword = findViewById(R.id.fieldPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        //hide the action bar
        try {
            this.getSupportActionBar().hide();
        }//end try
        catch (NullPointerException e){
            Log.e(TAG,"Could not hide action bar");
        }//end catch

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
       // mAuth.signOut();
    }//end onCreate

    @Override
    public void onStart() {
        super.onStart();
        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }//end if
    }//end onStart

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }//end if
    }//end onStop

    public void buttonListener(View view) {
        if (view.getId() == R.id.btnSignIn) {
            signIn();
        }//end if
        else if (view.getId() == R.id.btnSignUp) {
            toastMessage("Signing Up...");
            signUp();
        }//end else if
    }//end button listener

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        if (signUpFlag) {
            //add the user to the leaderboard
            addUserDataToLeaderBoard(user.getUid(), username);
        }//end if

        // Go to MainActivity
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }//end onAuthSuccess

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        }//end if
        else {
            return email;
        }//end else
    }//end usernameFromEmail

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(txtEmail.getText().toString())) {
            txtEmail.setError("Required");
            result = false;
        }//end if
        else {
            txtEmail.setError(null);
        }//end else

        if (TextUtils.isEmpty(txtPassword.getText().toString())) {
            txtPassword.setError("Required");
            result = false;
        }//end if
        else {
            txtPassword.setError(null);
        }//end else
        return result;
    }//end validateForm

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }//end writeNewUser


    private void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }//end if

        showProgressDialog();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        }//end if
                        else {
                            toastMessage("Email or Password incorrect");
                            txtPassword.setText("");
                        }//end else
                    }
                });
    }//end signIn

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }//end if

        showProgressDialog();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            signUpFlag = true;
                            onAuthSuccess(task.getResult().getUser());
                        }//end if
                        else {
                            toastMessage("Invalid Email Address");
                        }//end else
                    }
                });
    }//end signUp

    public void addUserDataToLeaderBoard(String id, String name){

        userId = id;
        username = name;
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("leaderboard").child(getUid()).hasChild(username)) {
                    toastMessage("User Already Exists");
                }//end if
                else {
                    UserProfile userProfile = new UserProfile(userId, username, points);
                    Map<String, Object> postValues = userProfile.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/leaderboard/" + userId, postValues);
                    mDatabase.updateChildren(childUpdates);
                }//end else
            }//end onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }//end onCancelled
        });
    }//end addUserDataToLeaderboard
}//end class
