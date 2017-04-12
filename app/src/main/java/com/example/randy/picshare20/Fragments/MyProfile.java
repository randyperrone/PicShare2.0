package com.example.randy.picshare20.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.randy.picshare20.Model.Consts;
import com.example.randy.picshare20.Model.User;
import com.example.randy.picshare20.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;

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

    private View layout;
    private DatabaseReference mDatabase;
    private String uploadId;
    private String emailAddress, userName, name, zip, height, weight, education, gender, ethnicity, religion;
    private String partnerWeight, partnerHeight, partnerEducation, partnerGender, partnerEthnicity, partnerReligion;
    private TextView showEmail, showUsername, showName, showZipCode, showHeight, showWeight, showEducation, showGender, showEthnicity, showReligion;
    private TextView showPartnerHeight, showPartnerWeight, showPartnerEducation, showPartnerGender, showPartnerEthnicity, showPartnerReligion;


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

        uploadId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        emailAddress = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        mDatabase = FirebaseDatabase.getInstance().getReference(Consts.DATABASE_PATH_UPLOADS_USER + "/" + uploadId + "/" + Consts.DATABASE_PATH_UPLOADS_PROFILE);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if(emailAddress != null){
                    showEmail = (TextView)layout.findViewById(R.id.emailaddress_show_textbox);
                    showEmail.setText(emailAddress);
                }
                if(user.getUsername() != null){
                    userName = user.getUsername();
                    showUsername = (TextView)layout.findViewById(R.id.username_show_textbox);
                    showUsername.setText(userName);
                }

                dataSnapshot.getChildren();
                Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();

                if(data.get(Consts.DATABASE_PATH_UPLOADS_NAME) != null){
                    name = data.get(Consts.DATABASE_PATH_UPLOADS_NAME).toString();
                    showName = (TextView)layout.findViewById(R.id.profile_display_name);
                    showName.setText(name);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_ZIP) != null){
                    zip = data.get(Consts.DATABASE_PATH_UPLOADS_ZIP).toString();
                    showZipCode = (TextView)layout.findViewById(R.id.profile_display_zipcode);
                    showZipCode.setText(zip);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_HEIGHT) != null){
                    height = data.get(Consts.DATABASE_PATH_UPLOADS_HEIGHT).toString();
                    showHeight = (TextView)layout.findViewById(R.id.profile_display_height);
                    showHeight.setText(height);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_WEIGHT) != null){
                    weight = data.get(Consts.DATABASE_PATH_UPLOADS_WEIGHT).toString();
                    showWeight= (TextView)layout.findViewById(R.id.profile_display_weight);
                    showWeight.setText(weight);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_EDUCATION) != null){
                    education = data.get(Consts.DATABASE_PATH_UPLOADS_EDUCATION).toString();
                    showEducation= (TextView)layout.findViewById(R.id.profile_display_education);
                    showEducation.setText(education);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_GENDER) != null){
                    gender = data.get(Consts.DATABASE_PATH_UPLOADS_GENDER).toString();
                    showGender= (TextView)layout.findViewById(R.id.profile_display_gender);
                    showGender.setText(gender);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_ETHNICITY) != null){
                    ethnicity = data.get(Consts.DATABASE_PATH_UPLOADS_ETHNICITY).toString();
                    showEthnicity= (TextView)layout.findViewById(R.id.profile_display_ethnicity);
                    showEthnicity.setText(ethnicity);
                }
                if(data.get(Consts.DATABASE_PATH_UPLOADS_RELIGION) != null){
                    religion = data.get(Consts.DATABASE_PATH_UPLOADS_RELIGION).toString();
                    showReligion= (TextView)layout.findViewById(R.id.profile_display_religion);
                    showReligion.setText(religion);
                }

                Map<String, Object> partnerData = (Map<String, Object>) data.get(Consts.DATABASE_PATH_UPLOADS_PARTNER);

                if(partnerData.get(Consts.DATABASE_PATH_UPLOADS_HEIGHT) != null){
                    partnerHeight = partnerData.get(Consts.DATABASE_PATH_UPLOADS_HEIGHT).toString();
                    showPartnerHeight= (TextView)layout.findViewById(R.id.profile_display_partner_height);
                    showPartnerHeight.setText(partnerHeight);
                }
                if(partnerData.get(Consts.DATABASE_PATH_UPLOADS_WEIGHT) != null){
                    partnerWeight = partnerData.get(Consts.DATABASE_PATH_UPLOADS_WEIGHT).toString();
                    showPartnerWeight= (TextView)layout.findViewById(R.id.profile_display_partner_weight);
                    showPartnerWeight.setText(partnerWeight);
                }
                if(partnerData.get(Consts.DATABASE_PATH_UPLOADS_EDUCATION) != null){
                    partnerEducation = partnerData.get(Consts.DATABASE_PATH_UPLOADS_EDUCATION).toString();
                    showPartnerEducation= (TextView)layout.findViewById(R.id.profile_display_partner_education);
                    showPartnerEducation.setText(partnerEducation);
                }
                if(partnerData.get(Consts.DATABASE_PATH_UPLOADS_GENDER) != null){
                    partnerGender = partnerData.get(Consts.DATABASE_PATH_UPLOADS_GENDER).toString();
                    showPartnerGender= (TextView)layout.findViewById(R.id.profile_display_partner_gender);
                    showPartnerGender.setText(partnerGender);
                }
                if(partnerData.get(Consts.DATABASE_PATH_UPLOADS_ETHNICITY) != null){
                    partnerEthnicity = partnerData.get(Consts.DATABASE_PATH_UPLOADS_ETHNICITY).toString();
                    showPartnerEthnicity= (TextView)layout.findViewById(R.id.profile_display_partner_ethnicity);
                    showPartnerEthnicity.setText(partnerEthnicity);
                }
                if(partnerData.get(Consts.DATABASE_PATH_UPLOADS_RELIGION) != null){
                    partnerReligion = partnerData.get(Consts.DATABASE_PATH_UPLOADS_RELIGION).toString();
                    showPartnerReligion= (TextView)layout.findViewById(R.id.profile_display_partner_religion);
                    showPartnerReligion.setText(partnerReligion);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return layout;
    }

}
