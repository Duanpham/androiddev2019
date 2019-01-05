package usth.edu.vn.twitterclient;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import usth.edu.vn.twitterclient.login.LoginActivity;

public class Beginning extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beginning);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                sendUserToLoginActivity();
            }
        }, 3000);   //3 seconds

    }
    private void sendUserToLoginActivity() {
        Intent mainIntent= new Intent(Beginning.this,LoginActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

}
