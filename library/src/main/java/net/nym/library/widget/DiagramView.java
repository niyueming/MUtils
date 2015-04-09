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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;

public class DiagramView extends SurfaceView implements Callback {

	SurfaceHolder surfaceHolder;
	boolean flag;
	int mWidth, mHeight;

	private ArrayList<Float> lines = new ArrayList<Float>();
	private int lineColor = Color.WHITE; // 线的颜色，默认白色
	private int hLineCount = 10;
	private int vLineCount = 40;

	private int[] curveColors = { Color.parseColor("#9950fca8"),
			Color.parseColor("#99f53c61") }; // 曲线图的颜色
	private ArrayList<Integer> colors = new ArrayList<Integer>();
	private ArrayList<ArrayList<Float>> datas = new ArrayList<ArrayList<Float>>();

	private boolean isRun;

	public DiagramView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DiagramView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DiagramView(Context context) {
		super(context);
		init();
	}

	private void init() {
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		mWidth = getWidth();
		mHeight = getHeight();

		// 透明
		setZOrderOnTop(true);
		getHolder().setFormat(PixelFormat.TRANSLUCENT);

		lines.add(Float.valueOf(0));
		lines.add(Float.valueOf(0));
		lines.add(Float.valueOf(0));
		lines.add(Float.valueOf(0));

		colors.add(curveColors[0]);
		colors.add(curveColors[1]);
		
		setGridData();


    }

    public void test() {
        //test
        for(int i = 0 ; i < 100;i ++)
        {
        addCurve(0, Float.valueOf(i));
        }
        for(int i = 0 ; i < 100;i ++)
        {
        addCurve(1, Float.valueOf(100 - i));
        }
    }

    private void setGridData() {
		lines.clear();
		for (int i = 0; i < vLineCount; i++) {
			addLine(new Float[] { Float.valueOf((mWidth / vLineCount) * i),
					Float.valueOf(0),
					Float.valueOf((mWidth / vLineCount) * i),
					Float.valueOf(mHeight) });
		}
		addLine(new Float[] { Float.valueOf(mWidth - 1), Float.valueOf(0),
				Float.valueOf(mWidth - 1), Float.valueOf(mHeight) });

		for (int i = 0; i < hLineCount; i++) {
			addLine(new Float[] { Float.valueOf(0),
					Float.valueOf((mHeight / hLineCount) * i),
					Float.valueOf(mWidth),
					Float.valueOf((mHeight / hLineCount) * i) });
		}
		addLine(new Float[] { Float.valueOf(0), Float.valueOf(mHeight - 1),
				Float.valueOf(mWidth), Float.valueOf(mHeight - 1) });
	}

	/**
	 * 更改某个曲线图的颜色
	 * 
	 * @param index
	 *            大于等于0
	 * @param color
	 */
	public void setCurveColor(int index, int color) {
		if (index < colors.size()) {
			colors.remove(index);
			colors.add(index, color);
		} else if (index == colors.size()) {
			colors.add(color);
		}
	}

	/**
	 * @param index
	 *            大于等于0
	 * @param value
	 */
	public void addCurve(int index, float value) {
		if (index > datas.size())
		{
			return;
		}
		
		if (index == datas.size()) {
			datas.add(new ArrayList<Float>());
		}

		ArrayList<Float> data = datas.get(index);
		Float[] values = { Float.valueOf(data.size() / 4),
				Float.valueOf(mHeight - value), Float.valueOf(data.size() / 4),
				Float.valueOf(mHeight) };
		data.addAll(Arrays.asList(values));
		isRun = true;
	}

	public void clearCurves() {
		datas.clear();
		isRun = true;
	}

	private float[] getCurveLines(int index) {
		float[] ls = new float[datas.get(index).size()];
		for (int i = 0; i < datas.get(index).size(); i++) {
			ls[i] = datas.get(index).get(i).floatValue();
		}

		return ls;
	}

	private void addLine(Float[] line) {
		if (line == null) {
			throw new NullPointerException("line can not be null");
		}
		if (line.length != 4) {
			throw new ArrayIndexOutOfBoundsException("array's count must be 4");
		}

		lines.addAll(Arrays.asList(line));
	}

	private float[] getLines() {
		float[] ls = new float[lines.size()];
		for (int i = 0; i < lines.size(); i++) {
			ls[i] = lines.get(i).floatValue();
		}

		return ls;
	}

	public void setLineColor(int color) {
		lineColor = getResources().getColor(color);
	}

	/**
	 * @param color
	 *            "#ffffff"
	 */
	public void setLineColor(String color) {
		lineColor = Color.parseColor(color);
	}

	/**
	 * 设置竖线数量，默认40
	 * 
	 * @param count
	 */
	public void setVerticalLineCount(int count) {
		vLineCount = count;
	}

	/**
	 * 设置横线数量，默认10
	 * 
	 * @param count
	 */
	public void setHorizontalLineCount(int count) {
		hLineCount = count;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		new DrawThread().start();
		flag = true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		for (int i = 0; i < datas.size(); i++) {
			ArrayList<Float> data = datas.get(i);
			for (int j = 3; j < data.size(); j += 4) {
				Float f = data.remove(j - 2);
				data.add(j - 2, Float.valueOf(f - mHeight + height));
				data.remove(j);
				data.add(j, Float.valueOf(height));
			}
		}

		mWidth = width;
		mHeight = height;
		setGridData();
		isRun = true;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}

	class DrawThread extends Thread {

		@Override
		public void run() {

			Canvas canvas = null;
			Paint paint = new Paint();
			while (flag) {
				if (!isRun) {
					continue;
				}

				isRun = false;

				try {
					canvas = surfaceHolder.lockCanvas();

					// 透明
					canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);

					drawGrid(canvas, paint);

					drawCurve(canvas, paint);

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (canvas != null)
						surfaceHolder.unlockCanvasAndPost(canvas);
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		/**
		 * 画曲线图
		 * 
		 * @param canvas
		 * @param paint
		 */
		private void drawCurve(Canvas canvas, Paint paint) {
			for (int i = 0; i < datas.size(); i++) {
				if (i < colors.size()) {
					paint.setColor(colors.get(i));
				}

				canvas.drawLines(getCurveLines(i), paint);
			}
		}

		/**
		 * 画格子
		 * 
		 * @param canvas
		 * @param paint
		 */
		private void drawGrid(Canvas canvas, Paint paint) {
			paint.setColor(lineColor);

			canvas.drawLines(getLines(), paint);
		}

	}

}
