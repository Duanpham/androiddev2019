package usth.edu.vn.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import usth.edu.vn.twitterclient.notification.AllofNotification;
import usth.edu.vn.twitterclient.notification.Metions;


public class NotificationFragment extends Fragment {
    private FloatingActionButton fab;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    public NotificationFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_notification, container, false);
        fab=(FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToTweetActivity();
            }
        });
        viewPager = view.findViewById(R.id.viewpager_notification);
        addTabs(viewPager);

        tabLayout = view.findViewById(R.id.tabs_notification);
        tabLayout.setupWithViewPager(viewPager);

        return  view;
    }
    private void sendUserToTweetActivity() {
        Intent intent3 =new Intent(getActivity(),TweetActivity.class);
        startActivity(intent3);
    }



    private void addTabs(ViewPager viewPager) {
//        ProfileActivity.ViewPagerAdapter adapter = new ProfileActivity.ViewPagerAdapter(getSupportFragmentManager());
        NotificationFragment.ViewPagerAdapter adapter =new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new AllofNotification(),"All");
        adapter.addFrag(new Metions(),"Mentions");
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
