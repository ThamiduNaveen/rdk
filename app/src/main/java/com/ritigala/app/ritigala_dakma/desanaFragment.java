package com.ritigala.app.ritigala_dakma;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class desanaFragment extends Fragment {

    RecyclerView desanaRecyclerView;
    FirebaseDatabase fireDB;
    DatabaseReference dRef;
    private ArrayList<String> desanaLinks;

    //LinearLayoutManager desanaLinearLayoutManager;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_desana, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        desanaRecyclerView = getActivity().findViewById(R.id.RecyclerView_fragmentDesana);
        desanaRecyclerView.setHasFixedSize(true);

        desanaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fireDB = FirebaseDatabase.getInstance();
        dRef = fireDB.getReference("desana_audio");
        dRef.keepSynced(true);

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                desanaLinks = new ArrayList<String>();

                for (DataSnapshot desanaSnapshot: dataSnapshot.getChildren()) {
                    String desanaLink = desanaSnapshot.child("link").getValue(String.class);
                    desanaLinks.add(desanaLink);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ModelDesana,ViewHolderDesana> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ModelDesana, ViewHolderDesana>(
                ModelDesana.class,
                R.layout.one_desana,
                ViewHolderDesana.class,
                dRef
        ) {
            @Override
            protected void populateViewHolder(ViewHolderDesana viewHolder, ModelDesana model, int position) {
                viewHolder.setDetail(getActivity(),model.title,model.description);

            }

            @Override
            public ViewHolderDesana onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolderDesana viewHolderDesana = super.onCreateViewHolder(parent, viewType);

                viewHolderDesana.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(view.getContext(),desanaActivity.class);

                        TextView title_TV = view.findViewById(R.id.textView_one_desana);
                        String titileSTR = title_TV.getText().toString();
                        intent.putExtra("title",titileSTR);

                        TextView description_TV = view.findViewById(R.id.textView_one_desana_description);
                        String descriptionSTR = description_TV.getText().toString();
                        intent.putExtra("description",descriptionSTR);

                        intent.putExtra("desanaLink",desanaLinks.get(position));
                        startActivity(intent);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolderDesana;
            }
        };

        desanaRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}


