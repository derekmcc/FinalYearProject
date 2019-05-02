package com.example.derek.gymbuddy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

/**
 * The base activity all other activities extend from
 */
public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    /**
     * Show the loading progress
     */
    public void showProgressDialog() {
        //if progress dialog is not already in use
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }//end if

        mProgressDialog.show();
    }//end showProgressDialog method

    /**
     * Close the progress bar
     */
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }//end if
    }//end hideProgressDialog method

    public String getUid() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }//end getUid method

    /**
     * Method to get the users unique ID
     * @return The users unique ID
     */
    public String getEmail() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
    }//end getEmail method

    /**
     * Method to extract the username from the email address
     * @param email String value of the users email
     * @return The username
     */
    public String getUsernameFromEmail(String email) {
        //if there is a @ symbol in the email remove it and the trailing characters
        if (email.contains("@")) {
            return email.split("@")[0];
        }//end if
        else {
            return email;
        }//end else
    }//end usernameFromEmail

    /**
     * Method to create toast messages
     * @param message Sentence to be passed
     */
    void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }//end toastMessage

    /**
     * Method to create an options menu
     * @param menu Menu item
     * @return The menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }//end onCreateOptionsMenu method

    /**
     * Method to listen to click on menu items
     * @param item The item in the menu
     * @return The chosen action
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItem = item.getItemId();

        //check which item was selected
        if (selectedItem == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }//end if
        else {
            return super.onOptionsItemSelected(item);
        }//end else
    }//end onOptionsItemSelected
}//end class
