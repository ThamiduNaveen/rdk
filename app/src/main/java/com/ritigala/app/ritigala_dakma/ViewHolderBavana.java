package com.ritigala.app.ritigala_dakma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

public class ViewHolderBavana extends RecyclerView.ViewHolder {

    View mView;
    private ProgressDialog loadingBar;
    private Bitmap bavanaBMP;
    private ImageView downImg;

    public ViewHolderBavana(View itemView) {
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

    public void setDetail(final Context ctx, String date, final String image, String address, final String contact , final String location){

        final TextView contactTV = (TextView) mView.findViewById(R.id.textView_Phone_Number);
        final TextView addressTV = (TextView) mView.findViewById(R.id.textView_address);
        TextView dateTV = (TextView) mView.findViewById(R.id.textView_one_bavana);
        ImageView imageIV = (ImageView) mView.findViewById(R.id.imageView_one_bavana);
        dateTV.setText(date);
        addressTV.setText(address);
        contactTV.setText(contact);
        contactTV.setVisibility(View.GONE);
        addressTV.setVisibility(View.GONE);
        Picasso.get().load(image).into(imageIV);

        ImageButton callIB = mView.findViewById(R.id.Button_call);
        callIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactTV.setVisibility(View.VISIBLE);
                dialContactPhone(contact,ctx);
            }
        });
        ImageButton locationIB = mView.findViewById(R.id.Button_location);
        locationIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressTV.setVisibility(View.VISIBLE);
                goToLoction(ctx,location);
            }
        });
        ImageButton shareBT = mView.findViewById(R.id.Button_share);
        shareBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareImage(ctx,image);
            }
        });


    }

    private void ShareImage(final Context context,String image) {
        loadingBar = new ProgressDialog(context);
        loadingBar.setTitle("Loading");
        loadingBar.setMessage("Please wait wile loading");
        loadingBar.show();

        downImg = new ImageView(context);

        Picasso.get().load(image).into(downImg, new Callback() {
            @Override
            public void onSuccess() {
                loadingBar.dismiss();
                try {
                    bavanaBMP = ((BitmapDrawable) downImg.getDrawable()).getBitmap();

                    try {

                        File file = new File(context.getExternalCacheDir(), "sample.jpeg");
                        FileOutputStream fOut = new FileOutputStream(file);
                        bavanaBMP.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                        fOut.flush();
                        fOut.close();
                        file.setReadable(true, false);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Intent.EXTRA_TEXT, "Bavana wadasatahana");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        intent.setType("image/jpeg");
                        context.startActivity(Intent.createChooser(intent, "Share via "));


                    } catch (Exception e) {

                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(Exception e) {
                loadingBar.dismiss();
                Toast.makeText(context, "Error while loading images.. Try again", Toast.LENGTH_LONG).show();

            }
        });



    }

    private void goToLoction(Context ctx,String location) {

        try {
            Uri gmmIntentUri = Uri.parse(location);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            ctx.startActivity(mapIntent);
        }catch (Exception e){
            Toast.makeText(ctx, "Location hasn't fixed yet", Toast.LENGTH_SHORT).show();
        }


    }

    private void dialContactPhone(final String phoneNumber, Context ctx) {
        ctx.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
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
