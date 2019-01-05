package usth.edu.vn.twitterclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private FloatingActionButton fab;
//    private RecyclerView recyclerView;
//    private RecyclerView.Adapter adapter;
//    private RecyclerView.LayoutManager mLayoutManager;

    private Context context;
    private RecyclerView searchTimelineRecyclerView;
    private EditText searchQuery;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TweetTimelineRecyclerViewAdapter adapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_search, container, false);
        fab=(FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToTweetActivity();
            }
        });

//        recyclerView = view.findViewById(R.id.all_list_search);
//        recyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(mLayoutManager);
//
//        recyclerView.setAdapter(adapter);
        return  view;
    }
    private void sendUserToTweetActivity() {
        Intent intent3 =new Intent(getActivity(),TweetActivity.class);
        startActivity(intent3);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSwipeRefreshLayout(view);
        setUpRecyclerView(view);
        setUpSearchQuery(view);
    }


    private void setUpRecyclerView(View view) {
        searchTimelineRecyclerView = view.findViewById(R.id.search_timeline_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        searchTimelineRecyclerView.setLayoutManager(linearLayoutManager);
    }


    private void setUpSearchQuery(View view) {
        searchQuery = view.findViewById(R.id.enter_search_query);

        //implement editor action listener to trigger query when user click on search icon from Keyboard
        searchQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                //check if user clicked on Search icon or not
                if (i == EditorInfo.IME_ACTION_SEARCH) {

                    //get the text from edit text
                    String searchQuery = textView.getText().toString().trim();
                    //check if query should not empty
                    if (!TextUtils.isEmpty(searchQuery)) {
                        hideKeyboard(textView);
                        doTwitterSearch(searchQuery);

                    } else {
                        Toast.makeText(context, "Please enter something to search.", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
                return false;
            }
        });
    }


    private void doTwitterSearch(String query) {
        SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(query)
                .languageCode(Locale.ENGLISH.getLanguage())
                .maxItemsPerRequest(50)
                .build();

        //create adapter for RecyclerView
        adapter = new TweetTimelineRecyclerViewAdapter.Builder(context)
                .setTimeline(searchTimeline)
                //action callback to listen when user like/unlike the tweet
                .setOnActionCallback(new Callback<Tweet>() {
                    @Override
                    public void success(Result<Tweet> result) {
                    }

                    @Override
                    public void failure(TwitterException exception) {
                    }
                })
                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
                .build();

        searchTimelineRecyclerView.setAdapter(adapter);
    }


    private void setUpSwipeRefreshLayout(View view) {

        swipeRefreshLayout = view.findViewById(R.id.search_swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //return if adapter is null
                if (adapter == null)
                    return;

                swipeRefreshLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        //on success response make refreshing false
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(context, "Tweets refreshed.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(context, "Failed to refresh tweets.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
