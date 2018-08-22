package com.ritigala.app.ritigala_dakma;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.ByteArrayOutputStream;

public class panividaFragment extends Fragment {

    RecyclerView panividaRecyclerView;
    FirebaseDatabase fireDB;
    DatabaseReference dRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ViewHolder viewHolder = super.onCreateViewHolder(parent,viewType);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView title_TV = view.findViewById(R.id.textView_one_panivida);
                        ImageView panivida_IV = view.findViewById(R.id.imageView_one_panivida);

                        String titileSTR = title_TV.getText().toString();
                        Drawable panividaImageDR = panivida_IV.getDrawable();

                        Bitmap bitmap = ((BitmapDrawable)panividaImageDR).getBitmap();

                        Intent intent = new Intent(view.getContext(),PostDetailActivity.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                        byte [] bytes = stream.toByteArray();
                        intent.putExtra("image",bytes);
                        intent.putExtra("title",titileSTR);
                        startActivity(intent);


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolder;
            }
        };

        panividaRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void firebaseSearch(String searchText){
        Query firebaseSearchQuery = dRef.orderByChild("title").startAt(searchText).endAt(searchText+"\uf8ff");

        FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(
                Model.class,
                R.layout.one_panivida,
                ViewHolder.class,
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                viewHolder.setDetail(getActivity(),model.title,model.getImage());
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ViewHolder viewHolder = super.onCreateViewHolder(parent,viewType);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView title_TV = view.findViewById(R.id.textView_one_panivida);
                        ImageView panivida_IV = view.findViewById(R.id.imageView_one_panivida);

                        String titileSTR = title_TV.getText().toString();
                        Drawable panividaImageDR = panivida_IV.getDrawable();

                        Bitmap bitmap = ((BitmapDrawable)panividaImageDR).getBitmap();

                        Intent intent = new Intent(view.getContext(),PostDetailActivity.class);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                        byte [] bytes = stream.toByteArray();
                        intent.putExtra("image",bytes);
                        intent.putExtra("title",titileSTR);
                        startActivity(intent);


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolder;
            }

        };

        panividaRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem item = menu.findItem(R.id.search_panivida);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
