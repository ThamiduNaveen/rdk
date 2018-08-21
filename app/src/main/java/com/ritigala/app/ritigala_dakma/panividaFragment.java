package com.ritigala.app.ritigala_dakma;


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

public class panividaFragment extends Fragment {

    RecyclerView panividaRecyclerView;
    FirebaseDatabase fireDB;
    DatabaseReference dRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panivida,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        panividaRecyclerView = getActivity().findViewById(R.id.RecyclerView_fragmentPanivida);
        panividaRecyclerView.setHasFixedSize(true);

        panividaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fireDB = FirebaseDatabase.getInstance();
        dRef = fireDB.getReference("Data");

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(
                Model.class,
                R.layout.one_panivida,
                ViewHolder.class,
                dRef
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                viewHolder.setDetail(getActivity(),model.title,model.getImage());
            }
        };

        panividaRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
