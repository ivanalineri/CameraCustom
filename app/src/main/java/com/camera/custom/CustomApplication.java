package com.camera.custom;

import android.app.Application;

import com.camera.custom.dao.DaoMaster;
import com.camera.custom.dao.DaoSession;
import com.facebook.stetho.Stetho;

import org.greenrobot.greendao.database.Database;

public class CustomApplication extends Application {
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "qatracker-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        Stetho.initializeWithDefaults(this);

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
