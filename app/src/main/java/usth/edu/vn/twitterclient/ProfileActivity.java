package usth.edu.vn.twitterclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import usth.edu.vn.twitterclient.profile.Likes;
import usth.edu.vn.twitterclient.profile.Media;
import usth.edu.vn.twitterclient.profile.TweetsOfProfile;
import usth.edu.vn.twitterclient.profile.TweetsAndReplies;

public class ProfileActivity extends AppCompatActivity {

    private TwitterAuthClient client;
    private String currentUserId;
    private Toolbar mToolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;



    private TextView usernameProfile, fullnameProfile;
    private CircleImageView userProfileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        mToolbar=findViewById(R.id.profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
//
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        fullnameProfile = findViewById(R.id.fullname_profile);
        usernameProfile = findViewById(R.id.username_profile);
        userProfileImage = findViewById(R.id.i_profile);
        fetchTwitterImage();
        fetchTwitterName( getTwitterSession());

    }
    public void fetchTwitterName(final TwitterSession twitterSession) {
        String username =twitterSession.getUserName();
        usernameProfile.setText(username);

    }
    public void fetchTwitterImage() {
        //check if user is already authenticated or not
        if (getTwitterSession() != null) {

            //fetch twitter image with other information if user is already authenticated
            //initialize twitter api client
            TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();

            //pass includeEmail : true if you want to fetch Email as well
            Call<User> call = twitterApiClient.getAccountService().verifyCredentials(true, false, true);
            call.enqueue(new Callback<User>() {
                @Override
                public void success(Result<User> result) {
                    User user = result.data;
                    String imageProfileUrl = user.profileImageUrl;
                    String name= user.name;
                    fullnameProfile.setText(name);
                    imageProfileUrl = imageProfileUrl.replace("_normal", "");
                    Glide.with(ProfileActivity.this).load(imageProfileUrl).into(userProfileImage);
                }
                @Override
                public void failure(TwitterException exception) {
                    Toast.makeText(ProfileActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //if user is not authenticated first ask user to do authentication
            Toast.makeText(this, "First to Twitter auth to Verify Credentials.", Toast.LENGTH_SHORT).show();
        }

    }
    private TwitterSession getTwitterSession() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        return session;
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
        Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(intent);
    }

    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TweetsOfProfile(), "Tweets");
        adapter.addFrag(new TweetsAndReplies(),"Tweets&  replies");
        adapter.addFrag(new Media(),"Media");
        adapter.addFrag(new Likes(),"Likes");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int postion){
            return mFragmentList.get(postion);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
