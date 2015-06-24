package net.nym.library.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author nym
 * @date 2014/9/25 0025.
 */
public class ContextUtils {

    /**
     * api8,2.2及以上
     *
     * @return
     */
    public static boolean isFroyoOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * api9,2.3及以上
     *
     * @return
     */
    public static boolean isGingerbreadOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * api11,3.0及以上
     *
     * @return
     */
    public static boolean isHoneycombOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * api14,4.0及以上
     *
     * @return
     */
    public static boolean isICSOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * api16,4.1及以上
     *
     * @return
     */
    public static boolean isJellyBeanOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * api19,4.4及以上
     *
     * @return
     */
    public static boolean isKitkatOrLater() {
        return Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.KITKAT;
    }

    /**
     * api21,5.0及以上
     *
     * @return
     */
    public static boolean isLollipopOrLater() {
        return Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * 判断设备是否能打电话
     * @param context
     * @return true is phone
     */
    public static boolean isPhone(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {
            return false;
        } else {
            return true;
        }
    }


    public static boolean hasExternalStorage() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static int getVersionCode(Context ctx) {
        int version = 0;
        try {
            version = ctx.getPackageManager().getPackageInfo(ctx.getApplicationInfo().packageName, 0).versionCode;
        } catch (Exception e) {
            Log.e("getVersionInt", e);
        }
        return version;
    }

    public static String getVersionName(Context ctx) {
        String version = "";
        try {
            version = ctx.getPackageManager().getPackageInfo(ctx.getApplicationInfo().packageName, 0).versionName;
        } catch (Exception e) {
            Log.e("getVersionInt", e);
        }
        return version;
    }

    /**
     * 获取设备ID
     *
     * @param context
     * @return
     */
    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Returns the unique device ID, for example, the IMEI for GSM and the MEID
     * or ESN for CDMA phones. Return null if device ID is not available.
     *
     * <p>Requires Permission:
     *   {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId() + "";
    }

    public static String getUUID(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + telephonyManager.getDeviceId();
        tmSerial = "" + telephonyManager.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId ;
    }

    /**
     * Role:Telecom service providers获取手机服务商信息 <BR>
     *
     * @param context
     * @return
     */
    public static String getPhoneProvidersName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String ProvidersName = null;
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String IMSI = telephonyManager.getSubscriberId();
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        // System.out.println(IMSI);
        if (IMSI != null) {
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = "中国移动";
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = "中国联通";
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = "中国电信";
            }

        }
        return ProvidersName;
    }

    /**
     * 获取屏幕大小
     *
     * @param context
     * @return
     * @see android.util.DisplayMetrics
     * @see android.view.WindowManager
     */
    public static DisplayMetrics getMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((WindowManager) context.getSystemService(Activity.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     *
     * 获取状态栏高度
     * */
    public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     *
     * 广播相册扫描文件
     *
     * */
    public static void mediaScanFile(Activity activity,Uri uri)
    {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri);
        activity.sendBroadcast(intent);
    }

    public static void insertMedia(Activity activity,File file)
    {
        try {
            //插入图库
            MediaStore.Images.Media.insertImage(activity.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
            mediaScanFile(activity,Uri.fromFile(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hides the input method.
     *
     * @param context context
     * @param view The currently focused view
     * @return success or not.
     */
    public static boolean hideInputMethod(Context context, View view) {
        if (context == null || view == null) {
            return false;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        return imm != null ? imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY) : false;
    }

    /**
     * Show the input method.
     *
     * @param context context
     * @param view The currently focused view, which would like to receive soft keyboard input
     * @return success or not.
     */
    public static boolean showInputMethod(Context context, View view) {
        if (context == null || view == null) {
            return false;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        return imm != null ? imm.showSoftInput(view,InputMethodManager.HIDE_IMPLICIT_ONLY) : false;
    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     *
     * @param context The application's environment.
     * @param intent The Intent action to check for availability.
     *
     * @return True if an Intent with the specified action can be sent and
     *         responded to, false otherwise.
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();

        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }

    /**
     * @param context
     * @param metaKey   meta-data里的name
     * @return meta-data的value
     *
     * */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * @param context
     * @param px
     * @return dp
     *
     * */
    public static int convertPxToDp(Context context ,int px) {
        return Math.round(px / getMetrics(context).density);
    }

    /**
     * @param context
     * @param dp
     * @return px
     *
     * */
    public static int convertDpToPx(Context context ,int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getMetrics(context)));
    }

    /**
     * 下载文件
     *
     * @param context
     * @param url   下载文件的网络路径
     * @param dir_name 下载到该文件夹
     * @param file_name 保存的文件名
     * @param title_name 通知栏的标题名
     */
    public static void download(Context context, String url,String dir_name,String file_name,String title_name) {
        if (isHoneycombOrLater()) {
            downloadInHoneycombOrLater(context, url,dir_name,file_name,title_name);
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        }
    }

    /**用系统下载管理器下载
     * @param context
     * @param url   下载文件的网络路径
     * @param dir_name 下载到该文件夹
     * @param file_name 保存的文件名
     * @param title_name 通知栏的标题名
     * @return 下载队列id
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static long downloadInHoneycombOrLater(Context context, String url,String dir_name,String file_name,String title_name) {
        DownloadManager manager = (DownloadManager) context
                .getSystemService(Activity.DOWNLOAD_SERVICE);
        Uri resource = Uri.parse(url);
        // 开始下载
        DownloadManager.Request request = new DownloadManager.Request(resource);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(false); // 是否允许漫游下载
        // 设置文件类型
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap
                .getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);
        // 在通知栏中显示
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(true);
        //如果存放地址已存在该文件，删除
        File file = new File(Environment.getExternalStoragePublicDirectory(dir_name),file_name);
        if (file.exists())
        {
            file.delete();
        }
        // sdcard的目录下的download文件夹
        request.setDestinationInExternalPublicDir(dir_name,file_name);
        request.setTitle(title_name);
        return manager.enqueue(request);
    }


    /**
     * 剪切板
     * */
    public static void clipboardManagerCopy(Context context,String message)
    {
        if (isHoneycombOrLater())
        {
            clipboardManagerCopyHoneycomb(context,message);
        }
        else
        {
            android.text.ClipboardManager c = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            c.setText(message);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void clipboardManagerCopy(Context context,ClipData clipData)
    {
        if (isHoneycombOrLater())
        {
            android.content.ClipboardManager c = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            c.setPrimaryClip(clipData);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void clipboardManagerCopyHoneycomb(Context context,String message)
    {
        android.content.ClipboardManager c = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        c.setPrimaryClip(ClipData.newPlainText(null, message));
    }


    /**
     * 获取全部图片地址
     * @return
     */
    public static ArrayList<String> getAllImages(Context context){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Uri uri = intent.getData();
        ArrayList<String> list = new ArrayList<String>();
        String[] proj ={MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);//managedQuery(uri, proj, null, null, null);
        while(cursor.moveToNext()){
            String path =cursor.getString(0);
            list.add(new File(path).getAbsolutePath());
        }
        return list;
    }

    /**
     * 创建桌面快捷键
     * 需要权限<uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
     * @param name 桌面快捷方式显示名称
     * @param icon 桌面快捷方式显示图片
     * @param clazz 桌面快捷方式打开页面
     * @param isDuplicate 是否允许重复创建
     * @return
     */
    public static void addShortcut(Context context,String name , int icon,Class clazz,boolean isDuplicate) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");

        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        shortcut.putExtra("duplicate", isDuplicate); //是否允许重复创建

        /****************************此方法已失效*************************/
        //ComponentName comp = new ComponentName(this.getPackageName(), "."+this.getLocalClassName());
        //shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));  　　
        /******************************end*******************************/
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClassName(context, clazz.getName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

        //快捷方式的图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, icon);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        context.sendBroadcast(shortcut);
    }

    /**
     * 删除桌面快捷键
     * 需要权限<uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
     * @param name 桌面快捷方式显示名称
     * @param clazz 桌面快捷方式打开页面
     * @return
     */
    public static void deleteShortcut(Context context,String name ,Class clazz) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");

        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
        String appClass = clazz.getName();
        ComponentName comp = new ComponentName(context.getPackageName(), appClass);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));

       context.sendBroadcast(shortcut);
    }
}
