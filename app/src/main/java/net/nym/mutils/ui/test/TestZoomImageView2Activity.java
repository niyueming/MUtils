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

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.nym.library.widget.ZoomView.ZoomImageView2;
import net.nym.library.widget.ZoomView.ZoomViewPager;
import net.nym.mutils.R;

/**
 * @author nym
 * @date 2015/4/28 0028.
 * @since 1.0
 */
public class TestZoomImageView2Activity extends ActionBarActivity {

    private ZoomViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_zoom_imageview2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = (ZoomViewPager) findViewById(R.id.viewpager);
        int[] data = {R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
        mViewPager.setAdapter(new ImageViewPagerAdapter(this,data));

    }

    private class ImageViewPagerAdapter extends PagerAdapter{

        private Context mContext;
        private int[] mData;
        public ImageViewPagerAdapter(Context context, int[] data){
            mContext = context;
            mData = data;
        }
        @Override
        public int getCount() {
            return mData.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ZoomImageView2 image = (ZoomImageView2) LayoutInflater.from(mContext).inflate(R.layout.item_image,container,false);
            image.setImageResource(mData[position]);
            image.setOnMovingListener(mViewPager);
//            ImageLoader.getInstance().displayImage(data[position],image);
            container.addView(image);
            return image;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
