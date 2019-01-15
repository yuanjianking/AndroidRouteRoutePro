package jp.co.nri.route.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

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

    /**
     * 表示ダイアログ
     */
    public static void showAlertDialog(Context context, String title, String message,
        String negative, String positive, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(negative, null)
                .setPositiveButton(positive, listener).show();
    }

    /**
     * 年月日ダイアログ
     */
    public static void showDatePickerDialog(Context context, EditText editText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(context, 0, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month++;
                editText.setText(year + "/" + (month < 10 ? "0" + month : month) + "/" + (day < 10 ? "0" + day : day));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * 時のダイアログ
     */
    public static void showTimePickerDialog(Context context, EditText editText) {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(context, 0, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editText.setText((hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (minute < 10 ? "0" + minute : minute));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }
}
