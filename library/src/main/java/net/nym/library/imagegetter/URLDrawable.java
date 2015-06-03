package net.nym.library.imagegetter;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import net.nym.library.common.BaseApplication;
import net.nym.library.util.ContextUtils;

/**
 * @author nym
 * @date 2015/6/3 0003.
 * @since 1.0
 */
public class URLDrawable extends BitmapDrawable {

    private Drawable drawable;

    public URLDrawable(Drawable defaultDraw) {
        setDefaultDrawable(defaultDraw);
    }

    public void setDrawable(Drawable nDrawable) {
        drawable = nDrawable;

        float times = times(drawable.getIntrinsicWidth());
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth() * times),
                (int)(drawable.getIntrinsicHeight() * times));
        setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * times),
                (int) (drawable.getIntrinsicHeight() * times));
    }

    private void setDefaultDrawable(Drawable nDrawable) {
        drawable = nDrawable;

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        setBounds(0, 0, drawable.getIntrinsicWidth() ,
                drawable.getIntrinsicHeight() );
    }

    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        drawable.draw(canvas);
    }

    private float times(int w){
        float times = 1;
        int width = ContextUtils.getMetrics(BaseApplication.getAppContext()).widthPixels ;
        times = width*1f /w;
        return times;
    }

}