package net.nym.library.widget.HintViewPager;

import android.support.v4.view.PagerAdapter;

/**
 * @author nym
 * @date 2015/5/9 0009.
 * @since 1.0
 */
public abstract class HintPagerAdapter extends PagerAdapter{
    /**
     * Return the real number of views available.
     */
    public abstract int getRealCount();
}
