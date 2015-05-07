package net.nym.library.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * @author nym
 * @date 2015/5/5 0005.
 * @since 1.0
 */
public class HintViewPager extends FrameLayout {
    ViewPager mViewPager;
    LinearLayout mHintLayer;
    public HintViewPager(Context context) {
        super(context);
        initView();
    }

    public HintViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HintViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView()
    {
        mViewPager = new ViewPager(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(params);
        addView(mViewPager);

    }
}
