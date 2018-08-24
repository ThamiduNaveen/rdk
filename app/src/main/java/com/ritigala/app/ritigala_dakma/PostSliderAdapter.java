package com.ritigala.app.ritigala_dakma;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostSliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    private ArrayList<String> imagesLinks;


    public PostSliderAdapter(Context context,ArrayList<String> imagesLinks) {
        this.context = context;
        this.imagesLinks=imagesLinks;

    }


    @Override
    public int getCount() {
        return imagesLinks.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_post_detail,container,false);
        LinearLayout layoutslide = (LinearLayout) view.findViewById(R.id.slide_layout);
        ImageView imgslide = (ImageView)  view.findViewById(R.id.imageView_postDetails);
        Picasso.get().load(imagesLinks.get(position)).into(imgslide);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }





}
