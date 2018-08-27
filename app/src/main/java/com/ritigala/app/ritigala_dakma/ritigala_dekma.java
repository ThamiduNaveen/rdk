package com.ritigala.app.ritigala_dakma;


import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class ritigala_dekma extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
