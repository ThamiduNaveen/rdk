package com.ritigala.app.ritigala_dakma;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class bawanaFragment extends Fragment {

    FirebaseDatabase fireDB;
    DatabaseReference dRef;

    RecyclerView bavanaRecyclerView;
    LinearLayoutManager bavanaLinearLayoutManager;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_bavana, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fireDB =FirebaseDatabase.getInstance();
        dRef =fireDB.getReference("Bavana");
        dRef.keepSynced(true);




//        //       StorageReference storageRef = FirebaseStorage.getInstance().getReference();

//        for(int i =1;i<6;i++){
//            //dRef.child("0"+String.valueOf(i)).child("image").setValue("1");
//            //dRef.child("0"+String.valueOf(i)).child("date").setValue(String.valueOf(i)+" රිටිගලින් පණිවිඩයක්");
//            //dRef.child("0"+String.valueOf(i)).child("location").setValue(String.valueOf(i)+" රිටිගලින් පණිවිඩයක්");
//            //dRef.child("0"+String.valueOf(i)).child("contact").setValue(String.valueOf(i)+" රිටිගලින් පණිවිඩයක්");
//            dRef.child("0"+String.valueOf(i)).child("address").setValue(String.valueOf(i)+" address");
////            final int finalI = i;
////            storageRef.child("ritigala_panivida/"+String.valueOf(i)+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                @Override
////                public void onSuccess(Uri uri) {
////                    // Got the download URL for 'users/me/profile.png'
////                    dRef.child(String.valueOf(finalI)).child("image").setValue(uri.toString());
////                }
////            });
//        }

        bavanaRecyclerView = getActivity().findViewById(R.id.RecyclerView_fragmentBavana);
        bavanaRecyclerView.setHasFixedSize(true);

        bavanaLinearLayoutManager =new LinearLayoutManager(getActivity());
//        bavanaLinearLayoutManager.setReverseLayout(true);
//        bavanaLinearLayoutManager.setStackFromEnd(true);

        bavanaRecyclerView.setLayoutManager(bavanaLinearLayoutManager);



    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ModelBavana,ViewHolderBavana> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelBavana, ViewHolderBavana>(
                ModelBavana.class,
                R.layout.one_bavana,
                ViewHolderBavana.class,
                dRef
        ) {
            @Override
            protected void populateViewHolder(ViewHolderBavana viewHolder, ModelBavana model, int position) {
                viewHolder.setDetail(getActivity(), model.getDate(),model.getImage(),model.getAddress(),model.getContact(),model.getLocation());
            }

            @Override
            public ViewHolderBavana onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolderBavana viewHolderBavana = super.onCreateViewHolder(parent, viewType);
                viewHolderBavana.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolderBavana;
            }
        };
        bavanaRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

}
