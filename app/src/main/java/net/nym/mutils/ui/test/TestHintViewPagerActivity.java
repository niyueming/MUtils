package net.nym.mutils.ui.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.nym.library.widget.HintViewPager.HintPagerAdapter;
import net.nym.library.widget.HintViewPager.HintViewPager;
import net.nym.library.widget.ZoomView.ZoomImageView2;
import net.nym.mutils.R;

/**
 * @author nym
 * @date 2015/5/9 0009.
 * @since 1.0
 */
public class TestHintViewPagerActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_hint_view_pager);

        HintViewPager pager = (HintViewPager) findViewById(R.id.hintViewPager);
        HintPagerAdapter adapter = new HintPagerAdapter() {

            int[] data = {R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
            @Override
            public int getRealCount() {
                return data.length;
            }

            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
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
                ZoomImageView2 image = (ZoomImageView2) LayoutInflater.from(TestHintViewPagerActivity.this).inflate(R.layout.item_image,container,false);
                image.setImageResource(data[position%data.length]);

                container.addView(image);
                return image;
            }
        };
        pager.setAdapter(adapter);
        pager.setHintBackgroundColor(Color.parseColor("#88000000"));
        pager.setHintPointBackgroundColor(Color.parseColor("#88ff0000"));
        pager.setPointGravity(Gravity.RIGHT);
        pager.auto();
    }
}
