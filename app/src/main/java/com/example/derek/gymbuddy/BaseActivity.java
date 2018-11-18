package com.example.derek.gymbuddy;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }

    /**
     * Method to create toast messages
     * @param message Sentence to be passed
     */
    void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }//end toastMessage
}
