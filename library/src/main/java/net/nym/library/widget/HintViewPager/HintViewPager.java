package net.nym.library.widget.HintViewPager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.nym.library.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author nym
 * @date 2015/5/5 0005.
 * @since 1.0
 */
public class HintViewPager extends FrameLayout {

    private ViewPager mViewPager;
    private HintPagerAdapter mPagerAdapter;
    private LinearLayout mHintLayer;
    private LinearLayout mHintPointLayer;
    private ViewPager.OnPageChangeListener mListener;
    private int[] pointsRes = {R.drawable.point_unselected,R.drawable.point_selected};

    public static final int CHANGE_BANNER = 1;
    public static final int SPACE_TIME = 3000;
    private boolean isAuto;

    private Timer mTimer ;
    private class MTimerTask extends TimerTask {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            mHandler.sendEmptyMessage(CHANGE_BANNER);

        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case CHANGE_BANNER:
                    if(mPagerAdapter != null)
                    {
                        mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1)%mPagerAdapter.getCount(),true);
                    }
                    break;

                default:
                    break;
            }
        }
    };

    public void auto()
    {
        if(!isAuto)
        {
            if (mTimer == null){
                mTimer = new Timer(true);
                mTimer.scheduleAtFixedRate(new MTimerTask(), SPACE_TIME, SPACE_TIME);
            }
            isAuto = true;
        }

    }

    public void stop()
    {
        if (isAuto){
            cancelAuto();
            isAuto = false;
        }
    }

    private void cancelAuto()
    {
        if(mTimer != null)
        {
            mTimer.cancel();
            mTimer = null;
        }
    }

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

        mHintLayer = new LinearLayout(getContext());
        params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        mHintLayer.setLayoutParams(params);
        mHintLayer.setPadding(5, 5, 5, 5);
        mHintLayer.setGravity(Gravity.CENTER);
        addView(mHintLayer);

        mHintPointLayer = new LinearLayout(getContext());
        params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        mHintPointLayer.setLayoutParams(params);
        mHintPointLayer.setOrientation(LinearLayout.HORIZONTAL);
        mHintLayer.addView(mHintPointLayer);


        mViewPager.setOnPageChangeListener(_onPageChangeListener);

    }

    private void initPoints()
    {
        if (mPagerAdapter != null){
            mHintPointLayer.removeAllViews();
            int count = mPagerAdapter.getRealCount();
            for (int i = 0; i < count ;i ++){
                ImageView image = new ImageView(getContext());
                image.setPadding(2, 2, 2, 2);

                if(mViewPager.getCurrentItem()%count == i)
                {
                    image.setBackgroundResource(pointsRes[1]);
                }
                else {
                    image.setBackgroundResource(pointsRes[0]);
                }
                mHintPointLayer.addView(image);
            }
        }
    }

    private void changeSelected(int position)
    {
        int count = mHintPointLayer.getChildCount();
        for (int i = 0 ; i < count ;i ++){
            if(position%count == i)
            {
                mHintPointLayer.getChildAt(i).setBackgroundResource(pointsRes[1]);
            }
            else {
                mHintPointLayer.getChildAt(i).setBackgroundResource(pointsRes[0]);
            }
        }
    }

    public void setHintBackgroundColor(int color)
    {
        mHintLayer.setBackgroundColor(color);
    }

    public void setHintBackgroundResource(int res)
    {
        mHintLayer.setBackgroundResource(res);
    }

    public void setHintPadding(int padding)
    {
        mHintLayer.setPadding(padding, padding, padding, padding);
    }

    public void setHintPadding(int left, int top, int right, int bottom)
    {
        mHintLayer.setPadding(left,top,right,bottom);
    }

    public void setHintPointBackgroundColor(int color)
    {
        mHintPointLayer.setBackgroundColor(color);
    }

    public void setHintPointBackgroundResource(int res)
    {
        mHintPointLayer.setBackgroundResource(res);
    }

    public void setPointsRes(int unselected,int selected)
    {
        pointsRes = new int[]{unselected,selected};
        initPoints();
    }

    /**
     * Describes how the child views are positioned. Defaults to GRAVITY_TOP. If
     * this layout has a VERTICAL orientation, this controls where all the child
     * views are placed if there is extra vertical space. If this layout has a
     * HORIZONTAL orientation, this controls the alignment of the children.
     *
     * @param gravity See {@link android.view.Gravity}
     *
     * @attr ref android.R.styleable#LinearLayout_gravity
     */
    public void setPointGravity(int gravity)
    {
        mHintLayer.setGravity(gravity);
    }

    public void setAdapter(HintPagerAdapter adapter)
    {
        mViewPager.setAdapter(adapter);
        mPagerAdapter = adapter;
        initPoints();
    }

    public void setCurrentItem(int item)
    {
        mViewPager.setCurrentItem(item);
    }

    public void setCurrentItem(int item, boolean smoothScroll){
        mViewPager.setCurrentItem(item,smoothScroll);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener)
    {
        mListener = listener;
    }

    private ViewPager.OnPageChangeListener _onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mListener != null){
                mListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mListener != null){
                mListener.onPageSelected(position);
            }
            changeSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mListener != null){
                mListener.onPageScrollStateChanged(state);
            }
            switch (state){
                case ViewPager.SCROLL_STATE_IDLE:   //什么都没做

                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:   //正在滑动
                    if (isAuto){
                        cancelAuto();
                    }
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:   //滑动完毕
                    if (isAuto){
                        isAuto = false;
                        auto();
                    }
                    break;
            }
        }
    };
}
