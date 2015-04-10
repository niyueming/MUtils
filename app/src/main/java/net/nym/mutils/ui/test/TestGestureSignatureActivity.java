/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.mutils.ui.test;

import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import net.nym.mutils.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * 手势签名
 * @author nym
 * @date 2015/3/17 0017.
 * @since 1.0
 */
public class TestGestureSignatureActivity extends ActionBarActivity implements View.OnClickListener {
    private static final long LONG_FADE_OFF = 1000 * 60 * 60;
    private static final long SHORT_FADE_OFF = 100;

    private GestureOverlayView mGestureOverlayView;
    private Gesture mGesture;
    private Button btn_flush,btn_comfirm;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gesture_signature);
        initTitle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGestureOverlayView = (GestureOverlayView) findViewById(R.id.gestureOverlayView);
//        mGestureOverlayView.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
        mGestureOverlayView.setFadeOffset(LONG_FADE_OFF);
        mGestureOverlayView.addOnGestureListener(new GestureOverlayView.OnGestureListener() {
            @Override
            public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
                btn_flush.setEnabled(false);
                btn_comfirm.setEnabled(false);
                mGesture = null;
            }

            @Override
            public void onGesture(GestureOverlayView overlay, MotionEvent event) {

            }

            @Override
            public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
                mGesture = overlay.getGesture();
                if (mGesture != null)
                {
                    btn_flush.setEnabled(true);
                    btn_comfirm.setEnabled(true);

                }
            }

            @Override
            public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {

            }
        });

    }

    private void initTitle() {
        ((TextView)findViewById(R.id.title)).setText("签名");
        findViewById(R.id.left).setOnClickListener(this);
        findViewById(R.id.right).setOnClickListener(this);
        findViewById(R.id.flush).setOnClickListener(this);
        btn_flush = (Button) findViewById(R.id.flush);
        btn_comfirm = (Button) findViewById(R.id.right);
        btn_flush.setEnabled(false);
        btn_comfirm.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.left:
                onBackPressed();
                break;
            case R.id.right:
//                if (!ContextUtils.hasExternalStorage())
//                {
//                    Toaster.toaster("没有检测到sd卡");
//                    return;
//                }
                saveBitmap();
                break;
            case R.id.flush:
                mGestureOverlayView.setFadeOffset(SHORT_FADE_OFF);
                mGestureOverlayView.clear(true);
                mGestureOverlayView.setFadeOffset(LONG_FADE_OFF);
                btn_flush.setEnabled(false);
                btn_comfirm.setEnabled(false);
                break;
        }
    }

    private void saveBitmap() {
        Bitmap bitmap = toBitmap(mGestureOverlayView.getWidth(), mGestureOverlayView.getHeight(), 10, Color.BLACK);
        File f = new File(Environment.getExternalStorageDirectory(),"qianzi.png");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        Intent intent = new Intent();
        intent.setData(Uri.fromFile(f));
        setResult(RESULT_OK);
        finish();
    }

    /**
     *从 Gesture 复制出来的 toBitmap方法，增加背景白色
    * */
    private Bitmap toBitmap(int width, int height, int inset, int color) {
        final Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(2);

        final Path path = mGesture.toPath();
        final RectF bounds = new RectF();
        path.computeBounds(bounds, true);

        final float sx = (width - 2 * inset) / bounds.width();
        final float sy = (height - 2 * inset) / bounds.height();
        final float scale = sx > sy ? sy : sx;
        paint.setStrokeWidth(20.0f / scale);        //笔迹加重

        path.offset(-bounds.left + (width - bounds.width() * scale) / 2.0f,
                -bounds.top + (height - bounds.height() * scale) / 2.0f);

        canvas.drawColor(Color.WHITE);  //画背景
        canvas.translate(inset, inset);
        canvas.scale(scale, scale);

        canvas.drawPath(path, paint);
        return bitmap;
    }
}
