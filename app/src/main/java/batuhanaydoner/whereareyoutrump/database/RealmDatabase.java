package com.moonturns.batuhanaydoner.whereareyoutrump.database;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmDatabase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration configuration=new RealmConfiguration.Builder().name("scores").build();
        Realm.setDefaultConfiguration(configuration);

    }
}
