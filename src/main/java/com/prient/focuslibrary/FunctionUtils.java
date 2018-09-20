package com.prient.focuslibrary;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FunctionUtils {

    public static String getStringById(Context context, int id) {
        return context.getResources().getString(id);
    }

    /**
     * 获取屏幕密度dp
     */
    public static int getSceenDensityDpi(Context context) {
        Activity activity = (Activity) context;
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }

    /**
     * 将屏幕分辨率的单位由dp转化为px
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将屏幕分辨率的单位由px转化为dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public void getDisplayElement(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕像素宽度
        int height = dm.heightPixels; //屏幕像素高度
        float density = dm.density; //屏幕密度0.75/1.0/1.5
        int densityDpi = dm.densityDpi; //屏幕密度dpi 120/160/240
        double diagonalPixels = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));//对角线
        double screenSize = diagonalPixels / (160 * density);
    }

    /*
     * getting screen width pixel
     */
    public static int getScreenWidth(Context context) {
        int columnWidth;
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    /**
     * 根据屏幕尺寸设置字体大小
     */
    public static void setFontSize(Context context, View view, int fontSize) {
        int screenWidth = 480;
        int screenHeight = 800;
        int densityDpi = FunctionUtils.getSceenDensityDpi(context);

        if (densityDpi == 240) {
            if (screenWidth == 320 && screenHeight == 480) {
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    tv.setTextSize(fontSize - 3);
                } else if (view instanceof Button) {
                    Button btn = (Button) view;
                    btn.setTextSize(fontSize - 3);
                }
            }
        }
    }

    /**
     * 把long时间转化为String时间
     */
    public static String formatDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sdf.format(time);
    }

    /**
     * 把String时间转化为 Calendar 对象
     */
    public static Calendar convertStringCalendar(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);//Fri Jan 13 17:26:33 CST 2012

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
    /**
     * 把String时间转化为 Calendar 对象
     */
    public static Date convertStringDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(time);//Fri Jan 13 17:26:33 CST 2012

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String showDiffFormatTime(String time) {
        Calendar now = Calendar.getInstance();
        Calendar timeGet = Calendar.getInstance();
        Date date = convertStringDate(time);
        timeGet.setTime(date);
        if (now.get(Calendar.YEAR) == timeGet.get(Calendar.YEAR)){
            if(now.get(Calendar.WEEK_OF_YEAR) == timeGet.get(Calendar.WEEK_OF_YEAR)) {
                if(now.get(Calendar.DAY_OF_YEAR) == timeGet.get(Calendar.DAY_OF_YEAR)){
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    return sdf.format(date);
                }else {
                    switch (timeGet.get(Calendar.DAY_OF_WEEK)) {

                        case Calendar.FRIDAY:
                            return "星期五";
                        case Calendar.MONDAY:
                            return "星期一";
                        case Calendar.SATURDAY:
                            return "星期六";
                        case Calendar.SUNDAY:
                            return "星期日";
                        case Calendar.THURSDAY:
                            return "星期四";
                        case Calendar.TUESDAY:
                            return "星期二";
                        case Calendar.WEDNESDAY:
                            return "星期三";
                    }
                }
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
                return sdf.format(date);
            }
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        }
        return null;
    }

//    Date d = new Date();//Fri Jan 13 17:28:19 CST 2012
//    Calendar now = Calendar.getInstance();
//    System.out.println("年: " + now.get(Calendar.YEAR));
//    System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");
//    System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
//    System.out.println("时: " + now.get(Calendar.HOUR_OF_DAY));
//    System.out.println("分: " + now.get(Calendar.MINUTE));
//    System.out.println("秒: " + now.get(Calendar.SECOND));
//    System.out.println("当前时间毫秒数：" + now.getTimeInMillis());
//    System.out.println(now.getTime());//Fri Jan 13 17:28:19 CST 2012

//    SimpleDateFormat myFmt=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
//    SimpleDateFormat myFmt1=new SimpleDateFormat("yy/MM/dd HH:mm");
//    SimpleDateFormat myFmt2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//等价于now.toLocaleString()
//    SimpleDateFormat myFmt3=new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 E ");
//    SimpleDateFormat myFmt4=new SimpleDateFormat(
//            "一年中的第 D 天 一年中第w个星期 一月中第W个星期 在一天中k时 z时区");
//    Date now=new Date();

    /**
     * 获取当前网络
     */
    public static String netWorkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = null;
        if (cm != null) {
            info = cm.getActiveNetworkInfo();
        }
        if (info != null && info.isAvailable()) {
            return info.getTypeName();
        } else {
            return "请检查网络";
        }
    }

    /**
     * 判断应用是否在前台，该方法不完善
     */
    public static boolean isForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

        if (manager != null) {
            List<ActivityManager.RunningAppProcessInfo> appProcessInfos = manager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
                if (TextUtils.equals(appProcessInfo.processName, context.getPackageName())) {
                    return appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
                }
            }
        }
        return false;
    }
}
