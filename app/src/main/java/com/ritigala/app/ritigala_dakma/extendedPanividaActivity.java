package com.ritigala.app.ritigala_dakma;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class extendedPanividaActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ExtendedPostSliderAdapter myadapter;
    ImageView downImg;

    private static final int WRITE_EXTERNAL_STO_CODE = 1;

    Bitmap panividaBMP;
    ArrayList<String> imagesLinks;
    private String title;
    private boolean hadError;
    private ProgressDialog loadingBar;

    ArrayList<ImageView> shareImagesArray;
    ArrayList<Bitmap> panividaArrayBMP;


    private int shareImageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_panivida);
        downImg = new ImageView(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.extendedPanividaBar);

        imagesLinks = getIntent().getStringArrayListExtra("complexPanividaImages");
        title = getIntent().getStringExtra("title");


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        viewPager = (ViewPager) findViewById(R.id.viewpager_extendedPanivida);
        myadapter = new ExtendedPostSliderAdapter(this, imagesLinks, title);
        viewPager.setAdapter(myadapter);

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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, WRITE_EXTERNAL_STO_CODE);
                    } else {
                        saveImage(this);
                    }
                } else {
                    saveImage(this);
                }

                return true;
            case R.id.postDetail_share:
                ShareImage(this);

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void LoopLoad(final ArrayList<String> paths, final Context context, final ProgressDialog bar) {

        final ImageView shareImage = new ImageView(this);


        Picasso.get().load(paths.get(shareImageIndex)).into(shareImage, new Callback() {
            @Override
            public void onSuccess() {
                shareImageIndex++;
                try {
                    panividaArrayBMP.add(((BitmapDrawable) shareImage.getDrawable()).getBitmap());

                    if (paths.size() != shareImageIndex) {
                        LoopLoad(paths, context, bar);
                    }else{
                        bar.dismiss();

                        try {

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                            intent.putExtra(Intent.EXTRA_SUBJECT, title);
                            intent.setType("image/jpeg");

                            ArrayList<Uri> files = new ArrayList<Uri>();
                            byte i = 0;
                            for (Bitmap imageBitmaps : panividaArrayBMP) {
                               // Toast.makeText(context, String.valueOf(i), Toast.LENGTH_SHORT).show();
                                File file = new File(getExternalCacheDir(), "sample" + String.valueOf(i) + ".jpeg");
                                FileOutputStream fOut = new FileOutputStream(file);
                                imageBitmaps.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                                fOut.flush();
                                fOut.close();
                                file.setReadable(true, false);
                                Uri uri = Uri.fromFile(file);
                                files.add(uri);
                                i++;
                            }

                            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                            startActivity(intent);


                        } catch (Exception e) {

                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }

                } catch (Exception e) {
                    bar.dismiss();
                    Toast.makeText(context, " Check your internet connection " , Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Exception e) {
                bar.dismiss();
                Toast.makeText(extendedPanividaActivity.this, "Error while loading images.. Try again", Toast.LENGTH_LONG).show();
            }
        });

    }



    private void ShareImage(final Context context) {
        hadError = false;
        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Loading");
        loadingBar.setMessage("Please wait wile loading");
        loadingBar.show();

        shareImagesArray = new ArrayList<>();
        panividaArrayBMP = new ArrayList<>();
        shareImageIndex = 0;

        LoopLoad(imagesLinks, context, loadingBar);


    }


    private void saveImage(final Context context) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        File path = Environment.getExternalStorageDirectory();
        final File dir = new File(path + "/Ritigala_dekma");

        if (!dir.exists()) {
            dir.mkdir();
        }
        final String imgName = timeStamp + ".jpeg";
        final File file = new File(dir, imgName);
        final OutputStream[] output = new OutputStream[1];
        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Loading");
        loadingBar.setMessage("Please wait wile loading");
        loadingBar.show();


        Picasso.get().load(imagesLinks.get(viewPager.getCurrentItem())).into(downImg, new Callback() {
            @Override
            public void onSuccess() {
                try {
                    loadingBar.dismiss();
                    panividaBMP = ((BitmapDrawable) downImg.getDrawable()).getBitmap();
                    try {
                        output[0] = new FileOutputStream(file);
                        panividaBMP.compress(Bitmap.CompressFormat.JPEG, 80, output[0]);
                        output[0].flush();
                        output[0].close();
                        Toast.makeText(context, imgName + " saved to" + dir, Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STO_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage(this);
                } else {
                    Toast.makeText(this, "Enable permission to save image", Toast.LENGTH_SHORT).show();
                }
        }
    }

}
