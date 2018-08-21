package com.ritigala.app.ritigala_dakma;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class startActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);

    Thread thread =new Thread(){
      @Override
      public void run() {
        try {
          sleep(5000);
        }
        catch (Exception e){
          e.printStackTrace();
        }
        finally {
          Intent welcome_main = new Intent(startActivity.this,MainActivity.class);
          startActivity(welcome_main);
        }
      }
    };
    thread.start();
  }

  @Override
  protected void onPause() {
    super.onPause();
    finish();
  }
}
