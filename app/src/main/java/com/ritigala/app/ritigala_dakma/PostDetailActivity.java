package com.ritigala.app.ritigala_dakma;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class PostDetailActivity extends AppCompatActivity {

    TextView titleTW ;
    ImageView panividaIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Panivida detail");
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

        titleTW = (TextView) findViewById(R.id.textView_postDetails);
        panividaIV = (ImageView) findViewById(R.id.imageView_postDetails);

        byte [] bytes = getIntent().getByteArrayExtra("image");
        String titleSTR = getIntent().getStringExtra("title");

        Bitmap bmp = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

        titleTW.setText(titleSTR);
        panividaIV.setImageBitmap(bmp);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
