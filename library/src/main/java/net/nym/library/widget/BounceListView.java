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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

/**
 * @author nym
 * @date 2014/9/29 0029.
 */
public class BounceListView extends ListView{
    /**
     *允许滚出屏幕距离 Y
     */
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;

    private int mMaxYOverscrollDistance;
    /**
     *允许滚出屏幕距离 X
     */
    private static final int MAX_X_OVERSCROLL_DISTANCE = 200;

    private int mMaxXOverscrollDistance;

    public BounceListView(Context context) {
        this(context, null);
    }

    public BounceListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initBounceListView();
    }

    public BounceListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initBounceListView();
    }

    private void initBounceListView(){
        //get the density of the screen and do some maths with it on the max overscroll distance
        //variable so that you get similar behaviors no matter what the screen size

        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        final float density = metrics.density;

        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
        mMaxXOverscrollDistance = (int) (density * MAX_X_OVERSCROLL_DISTANCE);
    }

    /**
     * Scroll the view with standard behavior for scrolling beyond the normal
     * content boundaries. Views that call this method should override
     * {@link #onOverScrolled(int, int, boolean, boolean)} to respond to the
     * results of an over-scroll operation.
     * <p/>
     * Views can use this method to handle any touch or fling-based scrolling.
     *
     * @param deltaX         Change in X in pixels
     * @param deltaY         Change in Y in pixels
     * @param scrollX        Current X scroll value in pixels before applying deltaX
     * @param scrollY        Current Y scroll value in pixels before applying deltaY
     * @param scrollRangeX   Maximum content scroll range along the X axis
     * @param scrollRangeY   Maximum content scroll range along the Y axis
     * @param maxOverScrollX Number of pixels to overscroll by in either direction
     *                       along the X axis.
     * @param maxOverScrollY Number of pixels to overscroll by in either direction
     *                       along the Y axis.
     * @param isTouchEvent   true if this scroll operation is the result of a touch event.
     * @return true if scrolling was clamped to an over-scroll boundary along either
     * axis, false otherwise.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, mMaxXOverscrollDistance, mMaxYOverscrollDistance, isTouchEvent);
    }
}
