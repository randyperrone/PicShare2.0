package com.example.randy.picshare20.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.randy.picshare20.PictureGalleryAdapter;
import com.example.randy.picshare20.R;
import com.example.randy.picshare20.Model.Upload;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyPictures#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPictures extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String DATABASE_PATH_UPLOADS = "Photos";
    private static final String DATABASE_PATH_UPLOADS_ROOT = "users";
    String uploadId;
    View layView;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private PictureGalleryAdapter mAdapter;
    private ArrayList<Upload> URL;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MyPictures() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyPictures.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPictures newInstance() {
        MyPictures fragment = new MyPictures();
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
        layView = inflater.inflate(R.layout.fragment_my_pictures, container, false);

        URL = new ArrayList<>();

        uploadId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS_ROOT + "/" + uploadId + "/" + DATABASE_PATH_UPLOADS);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    URL.add(upload);
                }

                recyclerView = (RecyclerView)layView.findViewById(R.id.recycle_view_mypictures);
                mAdapter = new PictureGalleryAdapter(getActivity().getApplicationContext(), URL);
                recyclerView.setAdapter(mAdapter);
                GridLayoutManager layout = new GridLayoutManager(getActivity(), 4);
                layout.setOrientation(GridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layout);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return layView;
    }

}
