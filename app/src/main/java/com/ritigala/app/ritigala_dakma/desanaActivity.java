package com.ritigala.app.ritigala_dakma;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class desanaActivity extends AppCompatActivity {

    DownloadManager dm;
    long quid;
    Button downBT;
    private static final int WRITE_EXTERNAL_STO_CODE = 1;

    Button playBT;
    SeekBar posbar;
    MediaPlayer mp;
    TextView currentTime, totalTime;
    private boolean filePrepared = false;
    int totalTimeInt;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desana);


        posbar = (SeekBar) findViewById(R.id.skBar_desana);
        currentTime = (TextView) findViewById(R.id.current_time_desana);
        totalTime = (TextView) findViewById(R.id.total_time_desana);
        playBT = (Button) findViewById(R.id.button_desana_play);
        downBT = (Button) findViewById(R.id.button_test_down);

        mp = new MediaPlayer();

        loadingBar = new ProgressDialog(this);

        posbar.setMax(100000);
        posbar.setProgress(1);

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
                        mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/ritigala-dakma-29931.appspot.com/o/desana_audio%2F1%20GAHALAHA%2CBAWANA%20WEDASATHANA.mp3?alt=media&token=879781d8-3407-441f-9fe3-50795aaf748d");
                        loadingBar.setTitle("Loading");
                        loadingBar.setMessage("Please wait wile loading");
                        loadingBar.show();
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
                        mp.prepare();

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



    }

    private String makeTime(int millis){
        int sec = millis/1000;
        int min = sec/60;
        int secShow = sec % 60;
        return String.valueOf(min)+":"+String.valueOf(secShow);

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


}


/*

    /////////////////////

    DownloadManager dm;
    long quid;
    Button downBT, downPBT;
    private static final int WRITE_EXTERNAL_STO_CODE = 1;


    ////////////////////////

    Button playBT;
    SeekBar posbar;
    MediaPlayer mp;
    MediaPlayer mp2;
    TextView start, end;
    int totalTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_desana, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        posbar = (SeekBar) getActivity().findViewById(R.id.skBar);
        start = (TextView) getActivity().findViewById(R.id.start);
        end = (TextView) getActivity().findViewById(R.id.end);
        playBT = (Button) getActivity().findViewById(R.id.button_test_play);

        mp = new MediaPlayer();
        mp2 = new MediaPlayer();


        ///////////////////////////
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
                            Toast.makeText(context, "SUC", Toast.LENGTH_SHORT).show();
                            downBT.setText("DOWN COM");
                        }
                    }
                }
            }
        };

        getActivity().registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        downBT = (Button) getActivity().findViewById(R.id.button_test_down);
        downBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dm = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://firebasestorage.googleapis.com/v0/b/ritigala-dakma-29931.appspot.com/o/1%20GAHALAHA%2CBAWANA%20WEDASATHANA.mp3?alt=media&token=a153b533-a8a7-43d1-865f-a26c39318419"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, WRITE_EXTERNAL_STO_CODE);
                    } else {
                        File path = Environment.getExternalStorageDirectory();
                        File dir = new File(path + "/Download/Ritigala_dekma");
                        dir.mkdirs();
                    }
                } else {
                    File path = Environment.getExternalStorageDirectory();
                    File dir = new File(path + "/Download/Ritigala_dekma");
                    dir.mkdirs();

                }
                request.setTitle("desana.mp3")
                        .setDestinationInExternalPublicDir("/Download/Ritigala_dekma", "des.mp3");
                quid = dm.enqueue(request);


            }
        });

        downPBT = (Button) getActivity().findViewById(R.id.button_test_playFromDown);
        downPBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    mp2.setDataSource(Environment.getExternalStorageDirectory()+"/Download/Ritigala_dekma/des.mp3");
                    mp2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            Toast.makeText(getActivity(), "prepared", Toast.LENGTH_SHORT).show();
                        }
                    });
                    mp2.prepare();
                    mp2.setLooping(true);
                    mp2.seekTo(0);
                    mp2.start();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        ////////////////////////////

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/ritigala-dakma-29931.appspot.com/o/1%20GAHALAHA%2CBAWANA%20WEDASATHANA.mp3?alt=media&token=a153b533-a8a7-43d1-865f-a26c39318419");
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            Toast.makeText(getActivity(), "prepared", Toast.LENGTH_SHORT).show();
                        }
                    });
                    mp.prepare();
                    mp.setLooping(true);
                    mp.seekTo(0);
                    totalTime = mp.getDuration();
                    posbar.setMax(totalTime);

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //TODO your background code

            }
        });


        playBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mp.isPlaying()) {
                    mp.start();
                    playBT.setText("Pause");
                } else {
                    mp.pause();
                    playBT.setText("Play");
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

    }
/////////////////


    /////////////////

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPos = msg.what;
            posbar.setProgress(currentPos);
            //start.setText(currentPos/1000);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        if (mp.isPlaying()) {
            mp.pause();
            playBT.setText("Play");
        }

    }

//    public void play_song(){
//
//        final MediaPlayer mp = new MediaPlayer();
//        try{
//
//            mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/ritigala-dakma-29931.appspot.com/o/1%20GAHALAHA%2CBAWANA%20WEDASATHANA.mp3?alt=media&token=a153b533-a8a7-43d1-865f-a26c39318419");
//            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mediaPlayer) {
//                    mp.start();
//                }
//            });
//            mp.prepare();
//        }catch (Exception e){
//            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//
//    }

*/
