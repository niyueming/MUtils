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

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import net.nym.library.R;


/**
 * @author nym
 *
 */
public class SharingLayout extends ViewGroup {
	private int mPaddingLeft = 0;
	private int mPaddingRight = 0;
	private int mPaddingTop = 0;
	private int mPaddingBottom = 0;
	private int mWidth, mHeight;

	private final int column;

	public SharingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.Scale);
		column = typedArray.getInt(0, 4);
		typedArray.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		mPaddingLeft = getPaddingLeft();
		mPaddingRight = getPaddingRight();
//		Log.i("left=%d,top=%d,right=%d,bottom=%d", mPaddingLeft,mPaddingTop,mPaddingRight,mPaddingBottom);
		int count = getChildCount();
		
		int maxHeight = 0;
		int maxWidth = 0;
		int cellWidth = (MeasureSpec.getSize(widthMeasureSpec) - mPaddingLeft - mPaddingRight)/ column;
		measureChildren(MeasureSpec.makeMeasureSpec(cellWidth,
                MeasureSpec.EXACTLY), heightMeasureSpec);

		int height = 0;
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			if (child.getVisibility() != GONE) {
				int childRight;
				int childBottom;

				LayoutParams lp = (LayoutParams) child
						.getLayoutParams();

				childRight = lp.x + child.getMeasuredWidth();
				childBottom = lp.y + child.getMeasuredHeight();

//				Log.i("childtop=%d,childbottom=%d", child.getTop(), child.getBottom());
				maxWidth = Math.max(maxWidth, childRight);
				height = Math.max(height, childBottom);
				if(i%column == column - 1)
				{
					maxHeight += height;
					height = 0;
				}else if(i == count - 1)
				{
					maxHeight += height;
				}

			}
		}

		maxWidth += mPaddingLeft + mPaddingRight;
		maxHeight += mPaddingTop + mPaddingBottom;

		maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
		maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

		setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec),
				resolveSize(maxHeight, heightMeasureSpec));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		int count = getChildCount();
	    mWidth = getMeasuredWidth() - mPaddingLeft - mPaddingRight;
	    mHeight = getMeasuredHeight();
	    int j = 0;
	    for (int i = 0; i < count; i++) {
	      View child = getChildAt(i);
	      if (child.getVisibility() != GONE) {
	    	  LayoutParams lp = (LayoutParams) child.getLayoutParams();
	        int childLeft = lp.x;
	        if(j%column == 0)
	        {
	        	childLeft += mPaddingLeft;
	        }
	        if (lp.width > 0)
	          childLeft += (int) ((mWidth/column - lp.width) / 2.0);
	        else
	          childLeft += (int) ((mWidth/column - child.getMeasuredWidth()) / 2.0);
	        childLeft += (mWidth/column) * (j%column);
	        int childTop = mPaddingTop + lp.y ;
//	        if (lp.height > 0)
//	          childTop += (int) ((mHeight/(count/column + 1) - lp.height) / 2.0);
//	        else
//	          childTop += (int) ((mHeight/(count/column + 1) - child.getMeasuredHeight()) / 2.0);
	        
	        childTop += (childTop + child.getMeasuredHeight()) * (j/column);
	        child.layout(childLeft , childTop , childLeft + child.getMeasuredWidth(), childTop + child.getMeasuredHeight());
	        j++;
	      }
	    }
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(
			ViewGroup.LayoutParams p) {
		return new LayoutParams(p);
	}

	public static class LayoutParams extends ViewGroup.LayoutParams {
		public int x;
		public int y;

		public LayoutParams(int width, int height, int x, int y) {
			super(width, height);
			this.x = x;
			this.y = y;
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}
	}

}
