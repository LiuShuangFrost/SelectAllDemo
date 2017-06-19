package com.streamt.www.selectalldemo;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

/**
 * @author： LiuShuang
 * @time： 2017/6/16 9:22
 * @description：
 */

public class MyApp extends Application {
    /**
     * 用于控制RecyclerView中的heckBox的选中状态
     */
    public static boolean[] flag=new boolean[MainActivity.content.size()];
    /**
     * 用于存放选中的checkbox的位置
     */
    public static Map<Integer,Boolean> flags = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
