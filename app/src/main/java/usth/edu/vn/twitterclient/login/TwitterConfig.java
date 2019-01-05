package usth.edu.vn.twitterclient.login;

import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import usth.edu.vn.twitterclient.R;

public class TwitterConfig extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        com.twitter.sdk.android.core.TwitterConfig config = new com.twitter.sdk.android.core.TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.CONSUMER_KEY), getResources().getString(R.string.CONSUMER_SECRET)))
                .debug(true)//enable debug mode
                .build();

        //finally initialize twitter with created configs
        Twitter.initialize(config);
    }
}
