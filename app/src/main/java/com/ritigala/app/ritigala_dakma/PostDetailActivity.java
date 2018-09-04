package com.ritigala.app.ritigala_dakma;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;


import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class PostDetailActivity extends AppCompatActivity {

    TextView titleTW;
    PhotoView panividaIV;
    private ViewPager viewPager;
    private PostSliderAdapter myadapter;
    private int positionInLink;
    ImageView downImg;

    private static final int WRITE_EXTERNAL_STO_CODE = 1;
    Bitmap panividaBMP;
    ArrayList<String> imagesLinks;
    ArrayList<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        downImg = new ImageView(this);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        FullScreencall();
        setContentView(R.layout.post_detail_container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.postDetailBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Panivida detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//       panividaIV = (PhotoView) findViewById(R.id.imageView_postDetails);
//
//
//        titleTW = (TextView) findViewById(R.id.textView_postDetails);
//
//        byte [] bytes = getIntent().getByteArrayExtra("image");
//
//        Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//
//        panividaIV.setImageBitmap(bmp);
//        titleTW.setText(titleSTR);



        imagesLinks = getIntent().getStringArrayListExtra("imageLinks");
        titles = getIntent().getStringArrayListExtra("titles");
        positionInLink = getIntent().getIntExtra("position", 0);
        boolean searchStatus = getIntent().getBooleanExtra("search", false);
        viewPager = (ViewPager) findViewById(R.id.viewpager_postDetails);
        myadapter = new PostSliderAdapter(this, imagesLinks,titles);
        viewPager.setAdapter(myadapter);
        if (searchStatus) {
            String titleSTR = getIntent().getStringExtra("title");
            int pos = titleSTR.indexOf(" ");
            if (pos == 1) {
                titleSTR = titleSTR.substring(0, 1);
            } else if (pos == 2) {
                titleSTR = titleSTR.substring(0, 2);
            } else if (pos == 3) {
                titleSTR = titleSTR.substring(0, 3);
            } else {
                titleSTR = "1";
            }
            positionInLink=Integer.parseInt(titleSTR) - 1;
            viewPager.setCurrentItem(positionInLink);
        } else {
            viewPager.setCurrentItem(positionInLink);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //FullScreencall();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_post_detail_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.postDetail_save:

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                        String [] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,WRITE_EXTERNAL_STO_CODE);
                    }else{
                        saveImage();
                    }
                }
                else {
                    saveImage();
                }

                return true;
            case R.id.postDetail_share:
                ShareImage();

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void ShareImage() {

        Picasso.get().load(imagesLinks.get(viewPager.getCurrentItem())).into(downImg);

        try {
            panividaBMP = ((BitmapDrawable) downImg.getDrawable()).getBitmap();

            try{

                File file = new File(getExternalCacheDir(),"sample.png");
                FileOutputStream fOut = new FileOutputStream(file);
                panividaBMP.compress(Bitmap.CompressFormat.PNG,100,fOut);
                fOut.flush();
                fOut.close();
                file.setReadable(true,false);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TEXT,"ritigala dakma "+(viewPager.getCurrentItem()+1));
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.setType("image/png");
                startActivity(Intent.createChooser(intent,"Share via "));



            }catch (Exception e){

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        catch (Exception e){
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }


    private void saveImage() {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        File path = Environment.getExternalStorageDirectory();
        File dir = new File(path+"/Ritigala_dekma");

        if (!dir.exists()){
            dir.mkdir();
        }
        String imgName = timeStamp+".PNG";
        File file = new File(dir,imgName);
        OutputStream output;


        Picasso.get().load(imagesLinks.get(viewPager.getCurrentItem())).into(downImg);
        try {
            panividaBMP = ((BitmapDrawable) downImg.getDrawable()).getBitmap();
            try{
                output = new FileOutputStream(file);
                panividaBMP.compress(Bitmap.CompressFormat.PNG,100,output);
                output.flush();
                output.close();
                Toast.makeText(this, imgName+" saved to"+dir, Toast.LENGTH_SHORT).show();


            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
        catch (Exception e){
            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STO_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    saveImage();
                }else {
                    Toast.makeText(this, "Enable permission to save image", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //    public void FullScreencall() {
//        if (Build.VERSION.SDK_INT < 19) {
//            View v = this.getWindow().getDecorView();
//            v.setSystemUiVisibility(View.GONE);
//        } else {
//            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//            decorView.setSystemUiVisibility(uiOptions);
//        }
//    }


}
