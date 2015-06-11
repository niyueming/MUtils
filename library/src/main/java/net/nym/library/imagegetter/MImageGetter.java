package net.nym.library.imagegetter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.Html;
import android.webkit.URLUtil;

import net.nym.library.R;
import net.nym.library.common.BaseApplication;
import net.nym.library.util.ContextUtils;
import net.nym.library.util.Crypto;

import java.io.File;

/**
 * @author nym
 * @date 2015/6/3 0003.
 * @since 1.0
 */
public class MImageGetter implements Html.ImageGetter {
    @Override
    public Drawable getDrawable(String source) {
        // 不存在文件时返回默认图片，并异步加载网络图片
        Resources res = BaseApplication.getAppResources();
        URLDrawable defaultDrawable = new URLDrawable(
                res.getDrawable(R.drawable.ic_launcher));

        if (!URLUtil.isNetworkUrl(source)){
            return defaultDrawable;
        }
        String imageName = Crypto.md5(source);
        String sdcardPath = Environment.getExternalStorageDirectory()
                .toString(); // 获取SDCARD的路径
//                                            String sdcardPath = getCacheDir()
//                                                    .toString(); // 获取SDCARD的路径
//                                            Log.e(sdcardPath);

        // 获取图片后缀名
        String[] ss = source.split("\\.");
        String ext = ss[ss.length - 1];

        // 最终图片保持的地址
        String savePath = sdcardPath + "/" + BaseApplication.getAppContext().getPackageName() + "/"
                + imageName + "." + ext;

        File file = new File(savePath);
        if (file.exists()) {
            // 如果文件已经存在，直接返回
            Drawable drawable = Drawable.createFromPath(savePath);
            if (drawable == null){
                return defaultDrawable;
            }
            float times = times(drawable.getIntrinsicWidth());
            drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth() * times),
                    (int)(drawable.getIntrinsicHeight() * times));
            return drawable;
        }


        new ImageAsync(defaultDrawable).execute(savePath, source);
        return defaultDrawable;
    }

    private float times(int w){
        float times = 1;
        int width = ContextUtils.getMetrics(BaseApplication.getAppContext()).widthPixels ;
        times = width*1f /w;
        return times;
    }
}
