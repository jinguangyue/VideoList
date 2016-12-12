package projects.jgy.com.videolist;

import android.app.Application;

import projects.jgy.com.videolist.utils.DeviceInfoUtils;

/**
 * Created by yue on 2016/12/12.
 */

public class MyApplication extends Application {
    private int screenWidth;
    private int screenHeight;
    //Application单例
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        screenWidth = DeviceInfoUtils.getScreenWidth(this);//获取屏幕宽度
        screenHeight = DeviceInfoUtils.getScreenHeight(this);//获取屏幕高度
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
