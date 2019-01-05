package usth.edu.vn.twitterclient;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import usth.edu.vn.twitterclient.client.MyTwitterApiClient;
import usth.edu.vn.twitterclient.model.FriendsResponseModel;
import usth.edu.vn.twitterclient.model.TwitterFriends;

public class SendMessageAcitivity extends AppCompatActivity {


    private SendMessageAcitivity activity = this;

    TwitterSession twitterSession;
    TwitterAuthToken twitterAuthToken;

    long loggedUserTwitterId;

    Button buttonTwitterLogin;
    ListView mainListView;

    private ArrayAdapter listAdapter ;

    List<TwitterFriends> twitterFriends;

    ArrayList<String> friendsList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message_acitivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("")

        buttonTwitterLogin = (Button) findViewById(R.id.buttonLogin);
        mainListView = (ListView) findViewById( R.id.mainListView );

        listAdapter = new ArrayAdapter(this, R.layout.simplerow, friendsList);
        mainListView.setAdapter(listAdapter );
        // ListView Item Click Listener
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (twitterFriends.get(position).getId() != 0){
                    sendMsg(twitterFriends.get(position).getId(),twitterFriends.get(position).getScreenName(),"Hello, This is test msg");
                }

            }

        });


//        loadTwitterFriends();

//        initTwitter();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =  item.getItemId();
        if(id == android.R.id.home) {
            sendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }
    private void sendUserToMainActivity() {
        Intent intent = new Intent(SendMessageAcitivity.this,MainActivity.class);
        startActivity(intent);
    }



    public void initTwitter(){

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.CONSUMER_KEY),getString(R.string.CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);

        twitterAuthClient = new TwitterAuthClient();
    }



    TwitterAuthClient twitterAuthClient;
    public void twitterLogin(View view){

        twitterAuthClient.authorize(activity, new Callback<TwitterSession>() {
            @Override
            public void success(Result result) {
                twitterSession = TwitterCore.getInstance().getSessionManager().getActiveSession();
                twitterAuthToken = twitterSession.getAuthToken();

                TwitterSession twitterSession = (TwitterSession) result.data;

                buttonTwitterLogin.setText("Logged as "+ twitterSession.getUserName());

                Log.e("success",twitterSession.getUserName());

                loggedUserTwitterId = twitterSession.getId();

                loadTwitterFriends();

            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }

    private void loadTwitterFriends() {
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(twitterSession);
        myTwitterApiClient.getCustomTwitterService().list(loggedUserTwitterId).enqueue(new retrofit2.Callback<FriendsResponseModel>() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.e("onResponse",response.toString());
                twitterFriends = fetchResults(response);

                Log.e("onResponse","twitterfriends:"+twitterFriends.size());

                for (int k=0;k<twitterFriends.size();k++){
                    friendsList.add(twitterFriends.get(k).getName());
                    Log.e("Twitter Friends","Id:"+twitterFriends.get(k).getId()+" Name:"+twitterFriends.get(k).getName()+" pickUrl:"+twitterFriends.get(k).getProfilePictureUrl());
                }
                listAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("onFailure",t.toString());
            }

        });
    }

    private List fetchResults(Response response) {
        FriendsResponseModel responseModel = (FriendsResponseModel) response.body();
        return responseModel.getResults();
    }


//    public void sendDirectMsg(View view){
//        sendMsg(341925762,"saddm_ruet","Hello, this is test msg from demo app.");
//    }

    public void sendMsg(long userId,String replyName,String msg){
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(session);
        Call call = myTwitterApiClient.getCustomTwitterService().sendPrivateMessage(userId,replyName,msg);
        call.enqueue(new Callback() {
            @Override
            public void success(Result result) {
                Toast.makeText(activity,"Message sent", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(activity, exception.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }



}
