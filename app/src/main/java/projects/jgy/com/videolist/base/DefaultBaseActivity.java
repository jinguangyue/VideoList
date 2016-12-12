package projects.jgy.com.videolist.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import projects.jgy.com.videolist.MyApplication;
import projects.jgy.com.videolist.utils.ActivityManager;

/**
 * Created by Wxcily on 16/1/5.
 */
public abstract class DefaultBaseActivity extends BaseActivity {

    protected Context context;
    protected Activity activity;
    protected int screenWidth;
    protected int screenHeight;

    protected boolean addTask = true;

    protected void thisHome() {
        this.addTask = false;
    }

    @Override
    protected void onBefore() {
        super.onBefore();
        this.context = this;
        this.activity = this;
        screenWidth = MyApplication.getInstance().getScreenWidth();
        screenHeight = MyApplication.getInstance().getScreenHeight();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (addTask)
            ActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (addTask)
            ActivityManager.getInstance().delActivity(this);
    }

}
