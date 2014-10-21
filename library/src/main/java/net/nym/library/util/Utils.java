package net.nym.library.util;

import android.annotation.TargetApi;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nym
 * @date 2014/9/25 0025.
 */
public class Utils {

    /**
     * 获取权限
     *
     * @param permission
     *            权限 ，比如"777"
     * @param path
     *            路径
     */
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 检查字符串是否为手机号码的方法,并返回true or false的判断值 */
    public static boolean isPhoneNumberValid(String tel) {
        boolean isValid = false;
		/*
		 * 可接受的电话格式有：
		 */
        // String expression = "^[1][34578]\\d{9}$";
        String expression = "^[1]\\d{10}$";
        CharSequence inputStr = tel;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断是否是有效的邮箱
     *
     * */
    public static boolean isEmailValid(String email) {
        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * Constructs a new {@code DecimalFormat} using the specified non-localized
     * pattern and the {@code DecimalFormatSymbols} for the user's default Locale.
     * See "<a href="../util/Locale.html#default_locale">Be wary of the default locale</a>".
     * @param pattern
     *            the non-localized pattern.
     * @throws IllegalArgumentException
     *            if the pattern cannot be parsed.
     * @return DecimalFormat
     */
    public static DecimalFormat getDecimalFormat(String pattern) {

        return new DecimalFormat(pattern);
    }

    /**
     *
     * @param time long型时间，毫秒
     * @param format 如："yyyy-MM-dd HH:mm:ss"
     * @return 返回format格式的时间
     *
     * 时间戳转换成字符窜
     * */
    public static String getDateToString(String format,long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(d);
    }

    /**
     * 返回默认格式"yyyy-MM-dd HH:mm:ss"的时间
     * @param time long型时间，毫秒
     *
     *
     * */
    public static String getDateToString(long time) {
        return getDateToString("yyyy-MM-dd HH:mm:ss",time);
    }

    /**
     * Check if external storage is built-in or removable.
     *  检测sdcard是否内置
     * @return True if external storage is removable (like an SD card), false
     *         otherwise.
     */
    @TargetApi(9)
    public static boolean isExternalStorageRemovable() {
        if (ContextUtils.isGingerbreadOrLater()) {
            return Environment.isExternalStorageRemovable();
        }
        return true;
    }

    /**
     * Role:Telecom service providers获取手机服务商信息 <BR>
     *
     * @param telephonyManager
     * @return
     */
    public static String getProvidersName(TelephonyManager telephonyManager) {
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

//    /**
//     * 生成二维码
//     *   源码库地址： https://github.com/zxing/zxing
//     * @param str
//     * @param twidth
//     * @param theight
//     * @return
//     * @throws WriterException
//     */
//    public static Bitmap Create2DCode(String str, int twidth, int theight)
//            throws WriterException {
//        BitMatrix matrix = new MultiFormatWriter().encode(str,
//                BarcodeFormat.QR_CODE, twidth, theight);
//        int width = matrix.getWidth();
//        int height = matrix.getHeight();
//        int[] pixels = new int[width * height];
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                if (matrix.get(x, y)) {
//                    pixels[y * width + x] = 0xff000000;
//                }
//            }
//        }
//        Bitmap bitmap = Bitmap.createBitmap(width, height,
//                Bitmap.Config.ARGB_8888);
//        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//        return bitmap;
//    }
}


