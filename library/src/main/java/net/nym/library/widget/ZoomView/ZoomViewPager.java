/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.widget.ZoomView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author nym
 * @date 2015/4/28 0028.
 * @since 1.0
 */
public class ZoomViewPager extends ViewPager implements OnChildMovingListener{
    /**  当前子控件是否处理拖动状态  */
    private boolean mChildIsBeingDragged=false;

    public ZoomViewPager(Context context) {
        super(context);
    }

    public ZoomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(mChildIsBeingDragged)
            return false;
        return super.onInterceptTouchEvent(arg0);
    }
    @Override
    public void startDrag() {
        mChildIsBeingDragged=true;
    }


    @Override
    public void stopDrag() {
        mChildIsBeingDragged=false;
    }


}
