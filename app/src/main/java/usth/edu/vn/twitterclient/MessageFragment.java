package usth.edu.vn.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import usth.edu.vn.twitterclient.chat.Chat;


public class MessageFragment extends Fragment {
    private Button findFriend;
    private FloatingActionButton fab;

    public MessageFragment() {
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
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        findFriend= view.findViewById(R.id.button_find_friend);
        findFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToSendMessageActivity();
            }
        });
        fab=(FloatingActionButton) view.findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToChatActivity();
            }
        });
        return view;
    }

    private void sendUserToSendMessageActivity() {
        Intent intent =new Intent(getActivity(),SendMessageAcitivity.class);
        startActivity(intent);
    }

    private void sendUserToChatActivity() {
        Intent intent2 =new Intent(getActivity(),Chat.class);
        startActivity(intent2);
    }


}
