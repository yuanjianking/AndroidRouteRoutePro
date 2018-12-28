package jp.co.nri.route.util;

import android.widget.Toast;

import jp.co.nri.route.base.BaseApplication;

/**
 * Created by ZhuChengyi on 2016/12/16.
 * Email chengyi.zhu@pactera.com
 */

public class AppUtil {

    private static String oldMsg;
    private static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    public static BaseApplication getContext() {
        return BaseApplication.getApplication();
    }

    /**
     * 表示するToast
     * @param msg String
     */
    public static void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(AppUtil.getContext(), msg, Toast.LENGTH_LONG);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_LONG) {
                    toast.show();
                }
            } else {
                oldMsg = msg;
                toast.setText(msg);
                toast.show();
            }
        }
        oneTime = twoTime;
    }
}
