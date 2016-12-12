package projects.jgy.com.videolist.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * Created by Wxcily on 16/1/5.
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onBefore();
        super.onCreate(savedInstanceState);
        initialize();
/*
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);

        // set a custom tint color for all system bars
        tintManager.setTintColor(getResources().getColor(R.color.title_bg));*/
/*// set a custom navigation bar resource
        tintManager.setNavigationBarTintResource(R.drawable.my_tint);
// set a custom status bar drawable
        tintManager.setStatusBarTintDrawable(MyDrawable);*/

        initView();
        initData();
        onAfter();
    }

    /**
     * onCreate 执行之前的操作
     */
    protected void onBefore() {
    }

    /**
     * 用于初始化对象,获取Intent数据等操作
     */
    protected abstract void initialize();

    /**
     * 用于初始化视图,获取控件实例
     */
    protected abstract void initView();

    /**
     * 用于初始化数据,填充视图
     */
    protected void initData() {
    }

    /**
     * 用于执行数据填充完后的操作
     */
    protected void onAfter() {
    }


    /**
     * 监听返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onPressBack()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 复写返回键操作,返回true则不继续下发
     *
     * @return
     */
    protected boolean onPressBack() {
        return false;
    }
}
