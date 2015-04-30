package net.nym.library.widget.ZoomView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import net.nym.library.util.Log;

/**
 *
 */
public class ZoomImageView2 extends ImageView {
    private final static String TAG="ZoomImageView2";
    private GestureDetector mGestureDetector;
    /**  */
    private static final int MODE_NONE = 0;
    /** 拖拉照片模式 */
    private static final int MODE_DRAG = 1;
    /** 放大缩小照片模式 */
    private static final int MODE_ZOOM = 2;

    private int screen_W, screen_H;// 可见屏幕的宽高度

    private int MAX_W, MAX_H, MIN_W, MIN_H;// 极限值
    private int start_Top = -1, start_Right = -1, start_Bottom = -1,
    start_Left = -1;// 初始化默认位置.

    /**   最大缩放级别*/
    float mMaxScale=2f;
    /**   最小缩放级别*/
    float mMinScale=0.5f;
    /**   双击时的缩放级别*/
    float mDobleClickScale=2;
    private int mMode = MODE_NONE;//
    /**  缩放开始时的手指间距 */
    private float mStartDis;
    /** 用于记录开始时候的坐标位置 */
    private PointF startPoint = new PointF();
    /** 是否上下出界了（左右不会出界）*/
    private boolean isOut;

    private OnChildMovingListener mOnChildMovingListener;

    public ZoomImageView2(Context context)
    {
        super(context);
        initZoomImageView2();
    }

    public ZoomImageView2(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        initZoomImageView2();
    }

    public ZoomImageView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public void initZoomImageView2() {
        mGestureDetector=new GestureDetector(getContext(), new GestureListener());
        //背景设置为balck
//        setBackgroundColor(Color.BLACK);
        //将缩放类型设置为FIT_CENTER，表示把图片按比例扩大/缩小到View的宽度，居中显示
        setScaleType(ScaleType.FIT_CENTER);

        initScale();
    }

    public void initScale() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Activity.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);


        screen_W = displayMetrics.widthPixels;
        screen_H = displayMetrics.heightPixels;

        MAX_W = (int) (screen_W * mMaxScale);
        MAX_H = (int) (screen_H * mMaxScale);

        MIN_W = (int) (screen_W * mMinScale);
        MIN_H = (int) (screen_H * mMinScale);
    }

    public float getMaxScale() {
        return mMaxScale;
    }

    public void setMaxScale(float mMaxScale) {
        this.mMaxScale = mMaxScale;
        initScale();
    }

    public float getMinScale() {
        return mMinScale;
    }

    public void setMinScale(float mMinScale) {
        this.mMinScale = mMinScale;
        initScale();
    }

    public float getDobleClickScale() {
        return mDobleClickScale;
    }

    public void setDobleClickScale(float mDobleClickScale) {
        this.mDobleClickScale = mDobleClickScale;
    }

    public void setOnMovingListener(OnChildMovingListener listener)
    {
        mOnChildMovingListener = listener;
    }

    private void startMove()
    {
        if (mOnChildMovingListener != null){
            mOnChildMovingListener.startDrag();
        }
    }

    private void stopMove()
    {
        if (mOnChildMovingListener != null){
            mOnChildMovingListener.stopDrag();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //设置拖动模式
                mMode=MODE_DRAG;
                startPoint.set(event.getRawX(), event.getRawY());
                startMove();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                reSetMatrix();
                stopMove();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMode == MODE_ZOOM) {
                    setZoomMatrix(event);
                }else if (mMode==MODE_DRAG) {
                    setDragMatrix(event);
                }else {
                    stopMove();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mMode=MODE_ZOOM;
                Log.e("MODE_ZOOM");
                mStartDis = distance(event);
                break;
            default:
                break;
        }

        return mGestureDetector.onTouchEvent(event);
    }

    public void setDragMatrix(MotionEvent event) {
        float dx = event.getRawX() - startPoint.x; // 得到x轴的移动距离
        float dy = event.getRawY() - startPoint.y; // 得到x轴的移动距离
        //避免和双击冲突,大于10f才算是拖动
        if(Math.sqrt(dx*dx+dy*dy)>10f){
            startPoint.set(event.getRawX(), event.getRawY());
            //在当前基础上移动
            int current_Left = this.getLeft() + (int)dx;
            int current_Top = this.getTop() + (int)dy;
            int current_Right = this.getRight() + (int)dx;
            int current_Bottom = this.getBottom() + (int)dy;
            if (current_Left > start_Left | current_Right < start_Right)
            {
//                setFrame(current_Left, current_Top, current_Right, current_Bottom);
                if ( Math.sqrt(dx*dx+dy*dy)>30f ){
                    stopMove();
                }
            }
            else {
                setFrame(current_Left, current_Top, current_Right, current_Bottom);
            }



            if (isOutBound())
            {
                isOut = true;
            }
        }
    }

    private boolean isOutBound()
    {
        return (getLeft() > start_Left | getRight() < start_Right | getTop() > start_Top | getBottom() < start_Bottom);
    }

    /**
     *  判断缩放级别是否是改变过
     *  @return   true表示非初始值,false表示初始值
     */
    private boolean isZoomChanged() {

        return (getLeft() != start_Left | getRight() != start_Right | getTop() != start_Top | getBottom() != start_Bottom);
    }

    /**
     *  和当前矩阵对比，检验dy，使图像移动后不会超出ImageView边界
     *  @param values
     *  @param dy
     *  @return
     */
//    private float checkDyBound(float[] values, float dy) {
//        float height=getHeight();
//        if(mImageHeight*values[Matrix.MSCALE_Y]<height)
//            return 0;
//        if(values[Matrix.MTRANS_Y]+dy>0)
//            dy=-values[Matrix.MTRANS_Y];
//        else if(values[Matrix.MTRANS_Y]+dy<-(mImageHeight*values[Matrix.MSCALE_Y]-height))
//            dy=-(mImageHeight*values[Matrix.MSCALE_Y]-height)-values[Matrix.MTRANS_Y];
//        return dy;
//    }

    /**
     *和当前矩阵对比，检验dx，使图像移动后不会超出ImageView边界
     *  @param values
     *  @param dx
     *  @return
     */
//    private float checkDxBound(float[] values,float dx) {
//        float width=getWidth();
//        if(mImageWidth*values[Matrix.MSCALE_X]<width)
//            return 0;
//        if(values[Matrix.MTRANS_X]+dx>0)
//            dx=-values[Matrix.MTRANS_X];
//        else if(values[Matrix.MTRANS_X]+dx<-(mImageWidth*values[Matrix.MSCALE_X]-width))
//            dx=-(mImageWidth*values[Matrix.MSCALE_X]-width)-values[Matrix.MTRANS_X];
//        return dx;
//    }

    /**
     *  设置缩放Matrix
     *  @param event
     */
    private void setZoomMatrix(MotionEvent event) {
        //只有同时触屏两个点的时候才执行
        if(event.getPointerCount()<2) return;
        float endDis = distance(event);// 结束距离
        if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
            float scale = endDis / mStartDis;// 得到缩放倍数
            mStartDis=endDis;//重置距离
            int disX = (int) (this.getWidth() * (1 - scale)) / 4;// 获取缩放水平距离
            int disY = (int) (this.getHeight() * (1 - scale)) / 4;// 获取缩放垂直距离
            int current_Left = this.getLeft() + disX;
            int current_Top = this.getTop() + disY;
            int current_Right = this.getRight() - disX;
            int current_Bottom = this.getBottom() - disY;

            setFrame(current_Left, current_Top, current_Right,
                    current_Bottom);

        }
    }

    /**
     *  检验scale，使图像缩放后不会超出最大倍数
     *  @param scale
     *  @param values
     *  @return
     */
//    private float checkMaxScale(float scale, float[] values) {
//        if(scale*values[Matrix.MSCALE_X]>mMaxScale)
//            scale=mMaxScale/values[Matrix.MSCALE_X];
//        mCurrentMatrix.postScale(scale, scale,getWidth()/2,getHeight()/2);
//        return scale;
//    }

    /**
     *   重置Matrix
     */
    private void reSetMatrix() {
        if (this.getWidth() > MAX_W) {
            //TODO
            new MyZoomAsyncTask(MAX_W,getWidth(),getHeight()).execute();
        } else if (this.getWidth() < (start_Right - start_Left)) {
            //TODO
            new MyZoomAsyncTask((start_Right - start_Left),getWidth(),getHeight()).execute();
        }
        if(isOut)  //拖越界了，回弹
        {
            isOut = false;
            //TODO 回弹
            int xDelta = 0;
            int yDelta = 0;
            if (getLeft() > start_Left)
            {
                xDelta = start_Left - getLeft() ;
            }
            else if (getRight() < start_Right)
            {
                xDelta = start_Right - getRight();
            }
            else if (getTop() > start_Top)
            {
                yDelta = start_Top -getTop();
            }
            else if (getBottom() < start_Bottom)
            {
                yDelta = start_Bottom - getBottom();
            }

//            new MyTranslateAnimationAsyncTask(xDelta,yDelta,300).execute();
        }
    }


    /**
     *  计算两个手指间的距离
     *  @param event
     *  @return
     */
    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        /** 使用勾股定理返回两点之间的距离 */
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     *   双击时触发
     */
    public void onDoubleClick(){
        int scale=(int)(isZoomChanged()?1:mDobleClickScale);
        int current_Left = start_Left - (scale - 1) * (start_Right - start_Left)/2;
        int current_Top = start_Top - (scale - 1) * (start_Bottom - start_Top)/2;
        int current_Right = start_Right + (scale - 1) * (start_Right - start_Left)/2;
        int current_Bottom = start_Bottom + (scale - 1) * (start_Bottom - start_Top)/2;;

        setFrame(current_Left, current_Top, current_Right,
                current_Bottom);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (start_Top == -1) {
            start_Top = top;
            start_Left = left;
            start_Bottom = bottom;
            start_Right = right;
        }

    }


    private class  GestureListener extends GestureDetector.SimpleOnGestureListener {
        public GestureListener() {
        }
        @Override
        public boolean onDown(MotionEvent e) {
            //捕获Down事件
            return true;
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //触发双击事件
            Log.e("onDoubleTap");
            onDoubleClick();
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            Log.e("onLongPress");
            ZoomImageView2.this.performLongClick();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
            super.onShowPress(e);
        }





        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            // TODO Auto-generated method stub
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.e("onSingleTapConfirmed");
            return ZoomImageView2.this.performClick();
        }

    }


    /**
     * 回缩动画執行
     */
    class MyZoomAsyncTask extends AsyncTask<Void, Integer, Void> {
        private int screen_W, current_Width, current_Height;

        private int left, top, right, bottom;

        private float scale_WH;// 宽高的比例


        private float STEP = 15f;// 步伐

        private float step_H, step_V;// 水平步伐，垂直步伐

        public MyZoomAsyncTask(int target_screen, int current_Width, int current_Height) {
            super();
            this.screen_W = target_screen;
            this.current_Width = current_Width;
            this.current_Height = current_Height;
            scale_WH = (float) current_Height / current_Width;
            if (target_screen < current_Width)
            {
                step_H = STEP;
            }
            else {
                step_H = -STEP;
            }
            step_V = scale_WH * step_H;
            left = getLeft();
            right = getRight();
            top = getTop();
            bottom = getBottom();
        }

        @Override
        protected Void doInBackground(Void... params) {

            while (true) {

                left += step_H;
                top += step_V;
                right -= step_H;
                bottom -= step_V;


                if (screen_W < current_Width)
                {
                    left = Math.min(left, start_Left);
                    top = Math.min(top, start_Top);
                    right = Math.max(right, start_Right);
                    bottom = Math.max(bottom, start_Bottom);
                    current_Width -= 2 * step_H;
//	                Log.e("jj", "top="+top+",bottom="+bottom+",left="+left+",right="+right);
                    onProgressUpdate(new Integer[]{left, top, right, bottom});
                    if (screen_W > current_Width)
                    {
                        break;
                    }
                }
                else {
                    left = Math.max(left, start_Left);
                    top = Math.max(top, start_Top);
                    right = Math.min(right, start_Right);
                    bottom = Math.min(bottom, start_Bottom);
                    current_Width -= 2 * step_H;
//	                Log.e("jj", "top="+top+",bottom="+bottom+",left="+left+",right="+right);
                    onProgressUpdate(new Integer[]{left, top, right, bottom});
                    if (screen_W < current_Width)
                    {
                        break;
                    }
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            super.onProgressUpdate(values);
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setFrame(values[0], values[1], values[2], values[3]);
                }
            });

        }

    }

}