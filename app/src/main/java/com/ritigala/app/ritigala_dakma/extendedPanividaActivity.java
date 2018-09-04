package com.ritigala.app.ritigala_dakma;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class extendedPanividaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_panivida);

        Toolbar toolbar = (Toolbar) findViewById(R.id.extendedPanividaBar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Panivida detail");//todo edit
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}
