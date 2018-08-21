package com.ritigala.app.ritigala_dakma;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder{

    View mView;

    public ViewHolder(View itemView) {
        super(itemView);
        mView=itemView;
    }
    public void setDetail(Context ctx,String title,String image){

        TextView titleTV = (TextView) mView.findViewById(R.id.textView_one_panivida);
        ImageView imageIV = (ImageView) mView.findViewById(R.id.imageView_one_panivida);
        titleTV.setText(title);

        Picasso.get().load(image).into(imageIV);

    }
}
