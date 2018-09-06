package com.ritigala.app.ritigala_dakma;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class desanaActivity extends AppCompatActivity {

    DownloadManager dm;
    DownloadManager.Request request;
    long quid;
    Button downBT;
    private static final int WRITE_EXTERNAL_STO_CODE = 1;

    Button playBT;
    SeekBar posbar;
    MediaPlayer mp;
    TextView currentTime, totalTime;
    private boolean filePrepared = false;
    int totalTimeInt;

    private String desanaSTR;

    private ProgressDialog loadingBar;

    String titleSTR;

    private int positionOfdesana;

    SharedPreferences desanaSharedPreferences;

    boolean isDownloaded;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desana);

        desanaSharedPreferences = getSharedPreferences("desanaData", Context.MODE_PRIVATE);
        final Boolean firstTime = desanaSharedPreferences.getBoolean("FirstTime",true);

        titleSTR = getIntent().getStringExtra("title");
        String descriptionSTR = getIntent().getStringExtra("description");

        positionOfdesana = getIntent().getIntExtra("position",0);

        TextView titleTV = (TextView)findViewById(R.id.textView_desana_title);
        titleTV.setText(titleSTR);
        TextView descriptionTV = (TextView)findViewById(R.id.textView_desana_description);
        descriptionTV.setText(descriptionSTR);

        final TextView downStatusTV = (TextView) findViewById(R.id.textView_desana_downloatStatus);

        desanaSTR = getIntent().getStringExtra("desanaLink");


        posbar = (SeekBar) findViewById(R.id.skBar_desana);
        currentTime = (TextView) findViewById(R.id.current_time_desana);
        totalTime = (TextView) findViewById(R.id.total_time_desana);
        playBT = (Button) findViewById(R.id.button_desana_play);
        downBT = (Button) findViewById(R.id.button_down);

        mp = new MediaPlayer();

        loadingBar = new ProgressDialog(this);

        posbar.setMax(100000);
        posbar.setProgress(1);

        if(!firstTime) {
            File path = Environment.getExternalStorageDirectory();
            File dir = new File(path + "/Ritigala_dekma/" + titleSTR + "_" + String.valueOf(positionOfdesana) + ".mp3");
            if (dir.exists()) {
                isDownloaded = true;
                downBT.setBackgroundResource(R.drawable.ic_check_circle_black_24dp);
                downStatusTV.setText("Downloaded");

            }
        }

        playBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(filePrepared){
                    if (!mp.isPlaying()) {
                        mp.start();
                        playBT.setBackgroundResource(R.drawable.ic_action_pause);
                    } else {
                        mp.pause();
                        playBT.setBackgroundResource(R.drawable.ic_action_play);
                    }
                }else {
                    try {
                        if(!isDownloaded){
                            loadingBar.setTitle("Loading");
                            loadingBar.setMessage("Please wait wile loading");
                            loadingBar.show();
                            if(checkConnection()) {
                                mp.setDataSource(desanaSTR);
                                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mediaPlayer) {
                                        filePrepared = true;
                                        mp.setLooping(true);
                                        mp.seekTo(0);
                                        totalTimeInt = mp.getDuration();
                                        posbar.setMax(totalTimeInt);
                                        mp.start();
                                        playBT.setBackgroundResource(R.drawable.ic_action_pause);
                                        totalTime.setText(makeTime(totalTimeInt));
                                        loadingBar.dismiss();
                                    }
                                });
                                mp.prepareAsync();
                            }else{
                                loadingBar.dismiss();
                            }
                        }else{
                            mp.setDataSource(Environment.getExternalStorageDirectory()+"/Ritigala_dekma/" + titleSTR + "_" + String.valueOf(positionOfdesana) + ".mp3");
                            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                @Override
                                public void onPrepared(MediaPlayer mediaPlayer) {
                                    filePrepared = true;
                                    mp.setLooping(true);
                                    mp.seekTo(0);
                                    totalTimeInt = mp.getDuration();
                                    posbar.setMax(totalTimeInt);
                                    mp.start();
                                    playBT.setBackgroundResource(R.drawable.ic_action_pause);
                                    totalTime.setText(makeTime(totalTimeInt));
                                }
                            });
                            mp.prepareAsync();
                        }


                    } catch (Exception e) {
                        Toast.makeText(desanaActivity.this,"Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }

            }
        });

        posbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mp.seekTo(i);
                    posbar.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mp != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mp.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    DownloadManager.Query req_qur = new DownloadManager.Query();
                    req_qur.setFilterById(quid);
                    Cursor c = dm.query(req_qur);
                    if (c.moveToFirst()) {
                        int columIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);

                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columIndex)) {
                            Toast.makeText(context, "File successfully downloaded", Toast.LENGTH_SHORT).show();
                            downBT.setBackgroundResource(R.drawable.ic_check_circle_black_24dp);
                            downStatusTV.setText("Downloaded");
                            isDownloaded = true;

                        }else{
                            Toast.makeText(context, "Error while downloading try again", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        downBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDownloaded && checkConnection()) {
                    if (firstTime) {
                        SharedPreferences.Editor editor = desanaSharedPreferences.edit();
                        editor.putBoolean("FirstTime", false);
                        editor.apply();
                    }

                    dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    request = new DownloadManager.Request(Uri.parse(desanaSTR));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, WRITE_EXTERNAL_STO_CODE);
                        } else {
                            File path = Environment.getExternalStorageDirectory();
                            File dir = new File(path + "/Ritigala_dekma");
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            request.setTitle(titleSTR + "_" + String.valueOf(positionOfdesana) + ".mp3")
                                    .setDestinationInExternalPublicDir("/Ritigala_dekma", titleSTR + "_" + String.valueOf(positionOfdesana) + ".mp3");
                            quid = dm.enqueue(request);

                        }
                    } else {
                        File path = Environment.getExternalStorageDirectory();
                        File dir = new File(path + "/Ritigala_dekma");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        request.setTitle(titleSTR + "_" + String.valueOf(positionOfdesana) + ".mp3")
                                .setDestinationInExternalPublicDir("/Ritigala_dekma", titleSTR + "_" + String.valueOf(positionOfdesana) + ".mp3");
                        quid = dm.enqueue(request);

                    }


                }
            }
        });




    }

    @SuppressLint("DefaultLocale")
    private String makeTime(long millis){
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));


    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPos = msg.what;
            posbar.setProgress(currentPos);
            currentTime.setText(makeTime(currentPos));
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (mp.isPlaying()) {
            mp.pause();
            playBT.setBackgroundResource(R.drawable.ic_action_play);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STO_CODE:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    File path = Environment.getExternalStorageDirectory();
                    File dir = new File(path + "/Ritigala_dekma");
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    request.setTitle(titleSTR+"_"+String.valueOf(positionOfdesana)+".mp3")
                            .setDestinationInExternalPublicDir("/Ritigala_dekma", titleSTR+"_"+String.valueOf(positionOfdesana)+".mp3");
                    quid = dm.enqueue(request);

                }else {
                    Toast.makeText(this, "Enable permission to save image", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private boolean checkConnection(){
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected() )
        {
            return true;

        }
        else
        {

            Toast.makeText(desanaActivity.this, "Check your Internet connection", Toast.LENGTH_LONG).show();
            return false;

        }
    }


}

