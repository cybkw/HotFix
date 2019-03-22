package com.bkw.hotfix;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * @author bkw
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(context);
    }
}
