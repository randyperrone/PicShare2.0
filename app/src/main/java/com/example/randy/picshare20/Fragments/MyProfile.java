package com.example.randy.picshare20.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.randy.picshare20.Model.User;
import com.example.randy.picshare20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String DATABASE_PATH_UPLOADS_ROOT = "users";
    private static final String DATABASE_PATH_PROFILE = "profile";
    private View layout;
    private DatabaseReference mDatabase;
    private String uploadId;
    private String emailAddress;
    private String userName;
    private TextView showEmail;
    private TextView showUsername;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MyProfile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfile newInstance() {
        MyProfile fragment = new MyProfile();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.fragment_my_profile, container, false);


        showEmail = (TextView)layout.findViewById(R.id.emailaddress_show_textbox);
        showUsername = (TextView)layout.findViewById(R.id.username_show_textbox);
        uploadId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        emailAddress = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_ROOT + "/" + uploadId + "/" + DATABASE_PATH_PROFILE);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName = user.getUsername();
                showEmail.setText(emailAddress);
                showUsername.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return layout;
    }

}
