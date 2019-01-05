package usth.edu.vn.twitterclient.client;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

import usth.edu.vn.twitterclient.listener.ServiceListeners;


public class MyTwitterApiClient  extends TwitterApiClient {

    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    /**
     * Provide CustomService with defined endpoints
     */
    public ServiceListeners getCustomTwitterService() {
        return getService(ServiceListeners.class);
    }
}
