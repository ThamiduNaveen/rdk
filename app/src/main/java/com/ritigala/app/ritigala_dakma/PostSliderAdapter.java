package com.ritigala.app.ritigala_dakma;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PostSliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    private ArrayList<String> imagesLinks;
    private ArrayList<String> titles;

    SharedPreferences imageSharedPreferences;


    public PostSliderAdapter(Context context,ArrayList<String> imagesLinks,ArrayList<String> titles) {
        this.context = context;
        this.imagesLinks=imagesLinks;
        this.titles=titles;

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

        imageSharedPreferences = context.getSharedPreferences("ImagePaths", Context.MODE_PRIVATE);
        String imagePath = imageSharedPreferences.getString(titles.get(position),"error");


        if(!imagePath.equals("error")){
            boolean error = loadImageFromStorage(imagePath,imgslide,titles.get(position),context);
            if(error){
                LoadImageFromPicasso(imagesLinks.get(position),imgslide,titles.get(position),context);
            }
        }else{
            LoadImageFromPicasso(imagesLinks.get(position),imgslide,titles.get(position),context);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
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
        File mypath=new File(directory,title+".png");
        boolean status=false;

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
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
            File f=new File(path,title+".png");
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





}
