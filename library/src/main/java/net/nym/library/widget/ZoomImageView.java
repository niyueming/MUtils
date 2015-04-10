/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;



/****
 * 这里你要明白几个方法执行的流程： 首先ImageView是继承自View的子类.
 * onLayout方法：是一个回调方法.该方法会在在View中的layout方法中执行，在执行layout方法前面会首先执行setFrame方法.
 * layout方法：
 * setFrame方法：判断我们的View是否发生变化，如果发生变化，那么将最新的l，t，r，b传递给View，然后刷新进行动态更新UI.
 * 并且返回ture.没有变化返回false.
 *
 * invalidate方法：用于刷新当前控件,
 *
 *
 * @author zhangjia
 *
 */

/**
 * 增加初始化赋值和可设置缩放的最大倍数和最小倍数
 *
 * @author nym
 * @date 2014-9-15
 */
public class ZoomImageView extends ImageView {
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;
    private int screen_W, screen_H;// 可见屏幕的宽高度

    private int MAX_W, MAX_H, MIN_W, MIN_H;// 极限值

    private int current_Top, current_Right, current_Bottom, current_Left;// 当前图片上下左右坐标

    private int start_Top = -1, start_Right = -1, start_Bottom = -1,
            start_Left = -1;// 初始化默认位置.

    private int start_x, start_y, current_x, current_y;// 触摸位置

    private float beforeLenght, afterLenght;// 两触点距离

    private float scale_temp;// 缩放比例

    private boolean isControl_V = false;// 垂直监控

    private boolean isControl_H = false;// 水平监控

    private MyAsyncTask myAsyncTask;// 异步动画
    private MyInAsyncTask myInAsyncTask;// 异步动画

    private float maxScale = 3f; // 最大缩放倍数
    private float minScale = 0.5f; // 最小缩放倍数
    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    public ZoomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Activity.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);

        screen_W = displayMetrics.widthPixels;
        screen_H = displayMetrics.heightPixels;

        MAX_W = (int) (screen_W * maxScale);
        MAX_H = (int) (screen_H * maxScale);

        MIN_W = (int) (screen_W * minScale);
        MIN_H = (int) (screen_H * minScale);
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

    float downX, downY;

    boolean isMoved = true;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        /** 处理单点、多点触摸 **/
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(event);
                downX = event.getX();
                downY = event.getY();
                if (isMoved)
                {
                    isMoved = false;
                    return super.onTouchEvent(event);
                }
                break;
            // 多点触摸
            case MotionEvent.ACTION_POINTER_DOWN:
                onPointerDown(event);
                break;

            case MotionEvent.ACTION_MOVE:
                onTouchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                mode = NONE;
                float stepX = event.getX() - downX;
                float stepY = event.getY() - downY;
                Log.i("ZoomImageView",String.format("x=%f,y=%f", stepX, stepY));
                if (Math.abs(stepX) < 1 & Math.abs(stepY) < 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                        callOnClick();
                    }
                }
                break;

            // 多点松开
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                /** 执行缩放还原 **/
//				if (isScaleAnim) {
//					doScaleAnim();
//				}
                if (this.getWidth() > MAX_W) {
                    doScaleAnimIn();
                } else if (this.getWidth() < screen_W) {
                    doScaleAnim();
                }
                break;
        }

        return true;
    }

    /**
     * 按下 *
     */
    void onTouchDown(MotionEvent event) {
        mode = DRAG;

        current_x = (int) event.getRawX();
        current_y = (int) event.getRawY();

        start_x = (int) event.getX();
        start_y = current_y - this.getTop();

    }

    /**
     * 两个手指 只能放大缩小 *
     */
    void onPointerDown(MotionEvent event) {
        if (event.getPointerCount() == 2) {
            mode = ZOOM;
            beforeLenght = spacing(event);// 获取两点的距离
        }
    }

    /**
     * 移动的处理 *
     */
    void onTouchMove(MotionEvent event) {
        int left = 0, top = 0, right = 0, bottom = 0;
        /** 处理拖动 **/
        if (mode == DRAG) {

            /** 在这里要进行判断处理，防止在drag时候越界 **/

            /** 获取相应的l，t,r ,b **/
            left = current_x - start_x;
            right = current_x + this.getWidth() - start_x;
            top = current_y - start_y;
            bottom = current_y - start_y + this.getHeight();

            /** 恢复上层活动 **/
            if (left >= 0 | right <= screen_W) {
                isMoved = true;
            }
            /** 水平进行判断 **/
            if (isControl_H) {
                if (left >= 0) {
                    left = 0;
                    right = this.getWidth();
                }
                if (right <= screen_W) {
                    left = screen_W - this.getWidth();
                    right = screen_W;
                }
            } else {
                left = this.getLeft();
                right = this.getRight();
            }
            /** 垂直判断 **/
            if (isControl_V) {
                if (top >= 0) {
                    top = 0;
                    bottom = this.getHeight();
                }

                if (bottom <= screen_H) {
                    top = screen_H - this.getHeight();
                    bottom = screen_H;
                }
            } else {
                top = this.getTop();
                bottom = this.getBottom();
            }
            if (isControl_H || isControl_V)
                this.setPosition(left, top, right, bottom);

            current_x = (int) event.getRawX();
            current_y = (int) event.getRawY();

        }
        /** 处理缩放 **/
        else if (mode == ZOOM) {

            afterLenght = spacing(event);// 获取两点的距离

            float gapLenght = afterLenght - beforeLenght;// 变化的长度

            if (Math.abs(gapLenght) > 5f) {
                scale_temp = afterLenght / beforeLenght;// 求的缩放的比例

                this.setScale(scale_temp);

                beforeLenght = afterLenght;
            }
        }

    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * 实现处理拖动 *
     */
    private void setPosition(int left, int top, int right, int bottom) {
        this.layout(left, top, right, bottom);
    }

    /**
     * 处理缩放 *
     */
    void setScale(float scale) {
        int disX = (int) (this.getWidth() * Math.abs(1 - scale)) / 4;// 获取缩放水平距离
        int disY = (int) (this.getHeight() * Math.abs(1 - scale)) / 4;// 获取缩放垂直距离

        // 放大
        if (scale > 1) {//&& this.getWidth() <= MAX_W) {
            current_Left = this.getLeft() - disX;
            current_Top = this.getTop() - disY;
            current_Right = this.getRight() + disX;
            current_Bottom = this.getBottom() + disY;

            this.setFrame(current_Left, current_Top, current_Right,
                    current_Bottom);
            /***
             * 此时因为考虑到对称，所以只做一遍判断就可以了。
             */
            if (current_Top <= 0 && current_Bottom >= screen_H) {
                //		Log.e("jj", "屏幕高度=" + this.getHeight());
                isControl_V = true;// 开启垂直监控
            } else {
                isControl_V = false;
            }
            if (current_Left <= 0 && current_Right >= screen_W) {
                isControl_H = true;// 开启水平监控
            } else {
                isControl_H = false;
            }

        }
        // 缩小
        else if (scale < 1 && this.getWidth() >= MIN_W) {
            current_Left = this.getLeft() + disX;
            current_Top = this.getTop() + disY;
            current_Right = this.getRight() - disX;
            current_Bottom = this.getBottom() - disY;
            /***
             * 在这里要进行缩放处理
             */
            // 上边越界
            if (isControl_V && current_Top > 0) {
                current_Top = 0;
                current_Bottom = this.getBottom() - 2 * disY;
                if (current_Bottom < screen_H) {
                    current_Bottom = screen_H;
                    isControl_V = false;// 关闭垂直监听
                }
            }
            // 下边越界
            if (isControl_V && current_Bottom < screen_H) {
                current_Bottom = screen_H;
                current_Top = this.getTop() + 2 * disY;
                if (current_Top > 0) {
                    current_Top = 0;
                    isControl_V = false;// 关闭垂直监听
                }
            }

            // 左边越界
            if (isControl_H && current_Left >= 0) {
                current_Left = 0;
                current_Right = this.getRight() - 2 * disX;
                if (current_Right <= screen_W) {
                    current_Right = screen_W;
                    isControl_H = false;// 关闭
                }
            }
            // 右边越界
            if (isControl_H && current_Right <= screen_W) {
                current_Right = screen_W;
                current_Left = this.getLeft() + 2 * disX;
                if (current_Left >= 0) {
                    current_Left = 0;
                    isControl_H = false;// 关闭
                }
            }

            if (isControl_H || isControl_V) {
                this.setFrame(current_Left, current_Top, current_Right,
                        current_Bottom);
            } else {
                this.setFrame(current_Left, current_Top, current_Right,
                        current_Bottom);
            }

        }

    }

    /**
     * 放大到最小
     * 缩放动画处理
     */
    public void doScaleAnim() {
        myAsyncTask = new MyAsyncTask(screen_W, this.getWidth(),
                this.getHeight());
        myAsyncTask.setLTRB(this.getLeft(), this.getTop(), this.getRight(),
                this.getBottom());
        myAsyncTask.execute();
    }

    /**
     * 缩小到最大
     * 缩放动画处理
     */
    public void doScaleAnimIn() {
        myInAsyncTask = new MyInAsyncTask(MAX_W, this.getWidth(),
                this.getHeight());
        myInAsyncTask.setLTRB(this.getLeft(), this.getTop(), this.getRight(),
                this.getBottom());
        myInAsyncTask.execute();
    }


    public float getMinScale() {
        return minScale;
    }

    public void setMinScale(float minScale) {
        if (maxScale < minScale) {
//			minScale = maxScale;
            throw new IllegalStateException("minScale must bigger than maxScale");
        }
        this.minScale = minScale;

        MIN_W = (int) (screen_W * minScale);
        MIN_H = (int) (screen_H * minScale);
    }

    public float getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(float maxScale) {
        if (maxScale < minScale) {
//			maxScale = minScale;
            throw new IllegalStateException("maxScale must bigger than minScale");
        }
        this.maxScale = maxScale;

        MAX_W = (int) (screen_W * maxScale);
        MAX_H = (int) (screen_H * maxScale);
    }


    /**
     * 回缩动画執行
     */
    class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
        private int screen_W, current_Width, current_Height;

        private int left, top, right, bottom;

        private float scale_WH;// 宽高的比例

        /**
         * 当前的位置属性 *
         */
        public void setLTRB(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        private float STEP = 8f;// 步伐

        private float step_H, step_V;// 水平步伐，垂直步伐

        public MyAsyncTask(int screen_W, int current_Width, int current_Height) {
            super();
            this.screen_W = screen_W;
            this.current_Width = current_Width;
            this.current_Height = current_Height;
            scale_WH = (float) current_Height / current_Width;
            step_H = STEP;
            step_V = scale_WH * STEP;
        }

        @Override
        protected Void doInBackground(Void... params) {

            while (current_Width <= screen_W) {

                left -= step_H;
                top -= step_V;
                right += step_H;
                bottom += step_V;

                current_Width += 2 * step_H;

                left = Math.max(left, start_Left);
                top = Math.max(top, start_Top);
                right = Math.min(right, start_Right);
                bottom = Math.min(bottom, start_Bottom);
//                Log.e("jj", "top="+top+",bottom="+bottom+",left="+left+",right="+right);
                onProgressUpdate(new Integer[]{left, top, right, bottom});
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

    /**
     * 回缩动画執行
     */
    class MyInAsyncTask extends AsyncTask<Void, Integer, Void> {
        private int screen_W, current_Width, current_Height;

        private int left, top, right, bottom;

        private float scale_WH;// 宽高的比例

        /**
         * 当前的位置属性 *
         */
        public void setLTRB(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        private float STEP = 15f;// 步伐

        private float step_H, step_V;// 水平步伐，垂直步伐

        public MyInAsyncTask(int target_screen, int current_Width, int current_Height) {
            super();
            this.screen_W = target_screen;
            this.current_Width = current_Width;
            this.current_Height = current_Height;
            scale_WH = (float) current_Height / current_Width;
            step_H = STEP;
            step_V = scale_WH * STEP;
        }

        @Override
        protected Void doInBackground(Void... params) {

            while (current_Width >= screen_W) {

                left += step_H;
                top += step_V;
                right -= step_H;
                bottom -= step_V;

                current_Width -= 2 * step_H;

                left = Math.min(left, start_Left);
                top = Math.min(top, start_Top);
                right = Math.max(right, start_Right);
                bottom = Math.max(bottom, start_Bottom);
//	                Log.e("jj", "top="+top+",bottom="+bottom+",left="+left+",right="+right);
                onProgressUpdate(new Integer[]{left, top, right, bottom});
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
