package usth.edu.vn.twitterclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.twitter.sdk.android.tweetcomposer.TweetUploadService;

public class TweetResultReceiver extends BroadcastReceiver{
    private static final String TAG = TweetResultReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (TweetUploadService.UPLOAD_SUCCESS.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            final Long tweetId = bundle.getLong(TweetUploadService.EXTRA_TWEET_ID);
            Toast.makeText(context, "Tweet uploaded successfully with Tweet ID : " + tweetId, Toast.LENGTH_SHORT).show();
        }
        else if (TweetUploadService.UPLOAD_FAILURE.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            final Intent retryIntent = bundle.getParcelable(TweetUploadService.EXTRA_RETRY_INTENT);
            Toast.makeText(context, "Failed to uploaded tweet.", Toast.LENGTH_SHORT).show();
        }
        else if (TweetUploadService.TWEET_COMPOSE_CANCEL.equals(intent.getAction())) {
            Toast.makeText(context, "User cancelled Tweet compose..", Toast.LENGTH_SHORT).show();
        }
    }
}
