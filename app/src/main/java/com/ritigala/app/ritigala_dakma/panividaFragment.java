package com.ritigala.app.ritigala_dakma;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class panividaFragment extends Fragment {

    RecyclerView panividaRecyclerView;
    FirebaseDatabase fireDB;
    DatabaseReference dRef;
    private ArrayList<String> imagesLinks;
    private ArrayList<String> titles;
    //private ArrayList<String> complexPanivida;

    private Map<String, ArrayList<String>> complexPanividaMap;

    LinearLayoutManager panividaLinearLayoutManager;
    SharedPreferences panividaSharedPreferences;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_panivida, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        panividaRecyclerView = getActivity().findViewById(R.id.RecyclerView_fragmentPanivida);
        panividaRecyclerView.setHasFixedSize(true);

        panividaRecyclerView.setLayoutManager(panividaLinearLayoutManager);

        fireDB = FirebaseDatabase.getInstance();
        dRef = fireDB.getReference("Data");
        dRef.keepSynced(true);


 //       StorageReference storageRef = FirebaseStorage.getInstance().getReference();



//        for(int i =229;i<300;i++){
//            //dRef.child(String.valueOf(i)).child("image").setValue("1");
//            //dRef.child(String.valueOf(i)).child("title").setValue(String.valueOf(i)+" රිටිගලින් පණිවිඩයක්");
//            final int finalI = i;
//            storageRef.child("ritigala_panivida/"+String.valueOf(i)+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    // Got the download URL for 'users/me/profile.png'
//                    dRef.child(String.valueOf(finalI)).child("image").setValue(uri.toString());
//                }
//            });
//        }


        panividaSharedPreferences = getActivity().getSharedPreferences("SortSettings", Context.MODE_PRIVATE);
        String panividaSorting = panividaSharedPreferences.getString("Sort", "Newest");

        if (panividaSorting.equals("Newest")) {
            panividaLinearLayoutManager = new LinearLayoutManager(getActivity());
            panividaLinearLayoutManager.setReverseLayout(true);
            panividaLinearLayoutManager.setStackFromEnd(true);
        } else if (panividaSorting.equals("Oldest")) {
            panividaLinearLayoutManager = new LinearLayoutManager(getActivity());
            panividaLinearLayoutManager.setReverseLayout(false);
            panividaLinearLayoutManager.setStackFromEnd(false);
        }


        panividaRecyclerView.setLayoutManager(panividaLinearLayoutManager);

        dRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
                imagesLinks = new ArrayList<String>();
                titles = new ArrayList<String>();
                complexPanividaMap = new HashMap<>();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String imagesLink = childSnapshot.child("image").getValue(String.class);
                    imagesLinks.add(imagesLink);
                    String title_one = childSnapshot.child("title").getValue(String.class);
                    titles.add(title_one);
                    if (childSnapshot.hasChild("images")) {
                        String titleComplex =childSnapshot.child("title").getValue().toString();
                        complexPanividaMap.put(titleComplex, new ArrayList<String>());
                        long noOfImages = childSnapshot.child("images").getChildrenCount();

                        for (int i = 1; i < noOfImages+1; i++) {
                            complexPanividaMap.get(titleComplex).add(childSnapshot.child("images").child(String.valueOf(i)).getValue().toString());
                        }
                    }
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
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(
                Model.class,
                R.layout.one_panivida,
                ViewHolder.class,
                dRef
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                viewHolder.setDetail(getActivity(), model.title, model.getImage(), complexPanividaMap);
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (complexPanividaMap.containsKey(titles.get(position))) {
                            Intent intent = new Intent(view.getContext(), extendedPanividaActivity.class);
                            intent.putExtra("complexPanividaImages", complexPanividaMap.get(titles.get(position)));
                            intent.putExtra("title", titles.get(position));
                            startActivity(intent);

                        } else {
                            Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                            intent.putExtra("title", "1");
                            intent.putExtra("search", false);
                            intent.putExtra("position", position);
                            intent.putExtra("imageLinks", imagesLinks);
                            HashMap<String, ArrayList<String>> complexPanividaImagesHashMap = new HashMap<>(complexPanividaMap);
                            intent.putExtra("complexPanividaImagesHashMap",complexPanividaImagesHashMap);
                            intent.putExtra("titles", titles);
                            startActivity(intent);
                        }


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolder;
            }
        };

        panividaRecyclerView.setAdapter(firebaseRecyclerAdapter);
       // panividaRecyclerView.scrollToPosition(50);
    }

    private void firebaseSearch(String searchText) {
        Query firebaseSearchQuery = dRef.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder>(
                Model.class,
                R.layout.one_panivida,
                ViewHolder.class,
                firebaseSearchQuery
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                viewHolder.setDetail(getActivity(), model.title, model.getImage(), complexPanividaMap);
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView title_TV = view.findViewById(R.id.textView_one_panivida);
//                        ImageView panivida_IV = view.findViewById(R.id.imageView_one_panivida);
//
                        String titileSTR = title_TV.getText().toString();
//                        Drawable panividaImageDR = panivida_IV.getDrawable();
//
//                        Bitmap bitmap = ((BitmapDrawable)panividaImageDR).getBitmap();
                        if (complexPanividaMap.containsKey(titileSTR)) {
                            Intent intent = new Intent(view.getContext(), extendedPanividaActivity.class);
                            intent.putExtra("complexPanividaImages", complexPanividaMap.get(titileSTR));
                            intent.putExtra("title", titileSTR);
                            startActivity(intent);

                        } else {
                            Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
//                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
//                        byte [] bytes = stream.toByteArray();


//                        intent.putExtra("image",bytes);
                            intent.putExtra("title", titileSTR);
                            intent.putExtra("imageLinks", imagesLinks);
                            intent.putExtra("search", true);
                            intent.putExtra("position", position);
                            intent.putExtra("titles", titles);
                            HashMap<String, ArrayList<String>> complexPanividaImagesHashMap = new HashMap<>(complexPanividaMap);
                            intent.putExtra("complexPanividaImagesHashMap",complexPanividaImagesHashMap);
                            startActivity(intent);
                        }

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

        getActivity().getMenuInflater().inflate(R.menu.menu_search, menu);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.sort) {

            showSortDialog();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        String sortOptions[] = {"Newest", "Oldest"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sort by")
                .setIcon(R.drawable.ic_action_sort)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {

                            SharedPreferences.Editor editor = panividaSharedPreferences.edit();
                            editor.putString("Sort", "Newest");
                            editor.apply();
                            getActivity().recreate();

                        } else if (i == 1) {
                            SharedPreferences.Editor editor = panividaSharedPreferences.edit();
                            editor.putString("Sort", "Oldest");
                            editor.apply();
                            getActivity().recreate();

                        }
                    }
                });
        builder.show();

    }

}
