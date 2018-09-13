package com.ritigala.app.ritigala_dakma;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ViewHolder extends RecyclerView.ViewHolder{

    View mView;
    SharedPreferences imageSharedPreferences;

    public ViewHolder(View itemView) {
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
    public void setDetail(final Context ctx, final String title, String image,Map<String,ArrayList<String>> complexPanividaMap){

        TextView titleTV = (TextView) mView.findViewById(R.id.textView_one_panivida);
        TextView extraDetailsTV = (TextView) mView.findViewById(R.id.textView_one_panivida_moreImages);
        final ImageView imageIV = (ImageView) mView.findViewById(R.id.imageView_one_panivida);
        titleTV.setText(title);
        extraDetailsTV.setVisibility(View.GONE);
        if(complexPanividaMap.containsKey(title)){
            extraDetailsTV.setText("මෙම පණිවිඩයේ  සියලු කොටස්  කියවිඉමට පණිවිඩය මත touch කරන්න.");
            extraDetailsTV.setVisibility(View.VISIBLE);
        }


        imageSharedPreferences = ctx.getSharedPreferences("ImagePaths", Context.MODE_PRIVATE);
        String imagePath = imageSharedPreferences.getString(title,"error");



        if(!imagePath.equals("error")){
            boolean error = loadImageFromStorage(imagePath,imageIV,title,ctx);
            if(error){
                LoadImageFromPicasso(image,imageIV,title,ctx);
            }
        }else{
            LoadImageFromPicasso(image,imageIV,title,ctx);

        }




    }

    private void LoadImageFromPicasso(String image, final ImageView imageIV, final String title, final Context ctx) {
        Picasso.get().load(image).into(imageIV, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap panividaBMP = ((BitmapDrawable) imageIV.getDrawable()).getBitmap();
                String path = saveToInternalStorage(panividaBMP,ctx,title);
                SharedPreferences.Editor editor = imageSharedPreferences.edit();
                editor.putString(title,path);
                editor.apply();

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    private String saveToInternalStorage(Bitmap bitmapImage ,Context ctx,String title){
        ContextWrapper cw = new ContextWrapper(ctx);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("panividaDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,title+".jpeg");
        boolean status=false;

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            //Toast.makeText(ctx, "done "+title, Toast.LENGTH_SHORT).show();
            status=false;
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(ctx, "error", Toast.LENGTH_SHORT).show();
            status=true;
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(status){
            return "error";
        }else {
            return directory.getAbsolutePath();
        }
    }

    private boolean loadImageFromStorage(String path,ImageView img,String title,Context ctx)
    {

        try {
            File f=new File(path,title+".jpeg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            img.setImageBitmap(b);
            //Toast.makeText(ctx, "Loaded from saved", Toast.LENGTH_SHORT).show();
            return false;
        }
        catch (FileNotFoundException e)
        {
            //Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
            return true;
        }


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
