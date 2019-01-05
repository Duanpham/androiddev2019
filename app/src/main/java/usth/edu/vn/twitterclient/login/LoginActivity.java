package usth.edu.vn.twitterclient.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import usth.edu.vn.twitterclient.MainActivity;
import usth.edu.vn.twitterclient.R;

import static usth.edu.vn.twitterclient.MainActivity.k;

public class LoginActivity extends AppCompatActivity {

    private TwitterLoginButton twitterLoginButton;
    //twitter auth client required for custom login
    private TwitterAuthClient client;
    private MyPreferenceManager myPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myPreferenceManager = new MyPreferenceManager(this);

        //check if user is already login or not
        if (myPreferenceManager.getUserId() != 0) {
            sendUserToMainActivity();
            return;
        }
        if(k==2) {

        }
        //initialize twitter auth client
//        client = new TwitterAuthClient();

        twitterLoginButton = findViewById(R.id.default_twitter_login_button);
//        defaultLoginTwitter();
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession twitterSession = result.data;

                //if twitter session is not null then save user data to shared preference
                if (twitterSession != null) {

                    myPreferenceManager.saveUserId(twitterSession.getUserId());//save user id
                    myPreferenceManager.saveScreenName(twitterSession.getUserName());//save user screen name


                    sendUserToMainActivity();
                    Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(LoginActivity.this, "Failed to do Login. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, "Failed to do Login. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the twitterAuthClient.
//        if (client != null)
//            client.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }


    private void sendUserToMainActivity() {
        Intent mainIntent= new Intent(LoginActivity.this,MainActivity.class);
         //add the validation that is  by pressing  the back button, not allow to come back login activity unless logout
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}
