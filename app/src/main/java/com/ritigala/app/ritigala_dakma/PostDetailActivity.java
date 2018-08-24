package com.ritigala.app.ritigala_dakma;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;


public class PostDetailActivity extends AppCompatActivity {

    TextView titleTW ;
    PhotoView panividaIV;
    private ViewPager viewPager;
    private PostSliderAdapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
//        panividaIV = (PhotoView) findViewById(R.id.imageView_postDetails);
//
//
//        titleTW = (TextView) findViewById(R.id.textView_postDetails);
//
//        byte [] bytes = getIntent().getByteArrayExtra("image");
//        String titleSTR = getIntent().getStringExtra("title");
//
//        Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//
//        panividaIV.setImageBitmap(bmp);
//        titleTW.setText(titleSTR);

        ArrayList<String> imagesLinks=getIntent().getStringArrayListExtra("imageLinks");
        int position = getIntent().getIntExtra("position",0);
        viewPager = (ViewPager) findViewById(R.id.viewpager_postDetails);
        myadapter = new PostSliderAdapter(this,imagesLinks);

        viewPager.setAdapter(myadapter);
        viewPager.setCurrentItem(position);




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override   protected void onResume() {
        super.onResume();
        //FullScreencall();
    }

    public void FullScreencall() {
        if (Build.VERSION.SDK_INT < 19) {
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
