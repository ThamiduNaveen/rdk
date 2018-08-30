package com.ritigala.app.ritigala_dakma;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class ViewHolderDesana extends RecyclerView.ViewHolder{

    private View mView;

    public ViewHolderDesana(View itemView) {
        super(itemView);
        mView=itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }

        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view,getAdapterPosition());
                return true;
            }
        });

    }
    public void setDetail(Context ctx, String title,String description){

        TextView titleTV = (TextView) mView.findViewById(R.id.textView_one_desana);
        titleTV.setText(title);
        TextView descriptionTV = (TextView) mView.findViewById(R.id.textView_one_desana_description);
        descriptionTV.setText(description);

    }

    private ViewHolder.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }
    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener =clickListener;
    }
}
