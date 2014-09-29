package net.nym.library.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author nym
 * @date 2014/9/29 0029.
 */
public class Toaster {

    public static void toasterCustom(View view) {
        Toast toast = new Toast(view.getContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

//    public static void toasterWithDrawable(int stringId, int drawable) {
//        toasterWithDrawable(BaseApplication.getAppContext(), stringId,
//                drawable, Toast.LENGTH_SHORT);
//    }

    public static void toasterWithDrawable(Context context,int stringId, int drawable) {
        toasterWithDrawable(context, stringId,
                drawable, Toast.LENGTH_SHORT);
    }

    public static void toasterWithDrawable(Context context, int stringId,
                                           int drawable, int duration) {
        Toast toast = Toast.makeText(context, stringId, duration);
        ViewGroup view = (ViewGroup) toast.getView();
        ImageView imageview = new ImageView(context);
        imageview.setImageResource(drawable);
        view.addView(imageview, 0);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

//    public static void toasterWithDrawable(String string, int drawable) {
//        toasterWithDrawable(BaseApplication.getAppContext(), string, drawable,
//                Toast.LENGTH_SHORT);
//    }

    public static void toasterWithDrawable(Context context,String string, int drawable) {
        toasterWithDrawable(context, string, drawable,
                Toast.LENGTH_SHORT);
    }

    public static void toasterWithDrawable(Context context, String string,
                                           int drawable, int duration) {
        Toast toast = Toast.makeText(context, string, duration);
        ViewGroup view = (ViewGroup) toast.getView();
        ImageView imageview = new ImageView(context);
        imageview.setImageResource(drawable);
        view.addView(imageview, 0);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /** Print an on-screen message to alert the user */
    public static void toaster(Context context, int stringId, int duration) {
        Toast.makeText(context, stringId, duration).show();
    }

//    public static void toaster(int stringId) {
//        toaster(BaseApplication.getAppContext(), stringId);
//    }

    public static void toaster(Context context, int stringId) {
        toaster(context, stringId, Toast.LENGTH_SHORT);
    }

    /** Print an on-screen message to alert the user */
    public static void toaster(Context context, String string, int duration) {
        Toast.makeText(context, string, duration).show();
    }

//    public static void toaster(String string) {
//        toaster(BaseApplication.getAppContext(), string);
//    }

    public static void toaster(Context context, String string) {
        toaster(context, string, Toast.LENGTH_SHORT);
    }
}
