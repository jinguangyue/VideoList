package projects.jgy.com.videolist.utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Wxcily on 15/10/29.
 * Activity管理
 */
public class ActivityManager {
    /**
     * 转载Activity的容器
     */
    private List<Activity> activities = new LinkedList<>();
    private static ActivityManager instance = new ActivityManager();

    /**
     * 将构造函数私有化
     */
    private ActivityManager() {
    }

    /**
     * 获取ExitAppUtils的实例，保证只有一个ExitAppUtils实例存在
     *
     * @return
     */
    public static ActivityManager getInstance() {
        return instance;
    }

    /**
     * 添加Activity实例到mActivityList中，在onCreate()中调用
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 从容器中删除多余的Activity实例，在onDestroy()中调用
     *
     * @param activity
     */
    public void delActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 退出程序的方法
     */
    public void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
        System.exit(0);
    }

    /**
     * 清理堆栈
     */
    public void clean() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }
}
