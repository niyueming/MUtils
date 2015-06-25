/*
 * Copyright (c) 2014  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author nym
 * @date 2014/11/24 0024.
 * @since 1.0
 */
public class ImageUtils {

    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     * @param bitmap 原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }

    public static Bitmap compressImageSize(String srcPath,int targetW,int targetH) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = targetW;//这里设置高度为800f
        float ww = targetH;//这里设置宽度为480f
        int be = 1;//be=1表示不缩放
        if (w > ww || h > hh) {
            final int heightRatio = Math.round((float) h / hh);
            final int widthRatio = Math.round((float) w / ww);
            be = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inJustDecodeBounds = false;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }


    public static Uri compressImage(Uri imageUri,String targetPath) {
        return compressImage(imageUri,targetPath,400 * 1024);
    }

    /**
     * @param targetSize 目标大小，单位b
     * */
    public static Uri compressImage(Uri imageUri,String targetPath,long targetSize) {
        Bitmap image = BitmapFactory.decodeFile(imageUri.getPath());
        int options = 100;
//        System.out.println("原来----------"+baos.size()/1024);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        while (baos.toByteArray().length   > targetSize) {  //判断如果图片是否大于400kb,大于继续压缩
            options -= 4;
            Log.i("options=%d",options);
            if(options<=0){
                break;
            }
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }


        File file = new File(targetPath);
        try {

            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(baos.toByteArray());
//            System.out.println("现在----------"+baos.size()/1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("newlen=%d",file.length());
        return Uri.fromFile(file);
    }
}
