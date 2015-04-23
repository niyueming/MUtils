/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.image_displayer;

import android.graphics.Bitmap;
import android.os.Handler;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

/**
 * @author nym
 * @date 2015/4/23 0023.
 * @since 1.0
 */
public class DisplayImageOptionsFactory {

    private static DisplayImageOptionsFactory my;
    private DisplayImageOptionsFactory()
    {

    }

    public static DisplayImageOptionsFactory getInstance()
    {
        if (my == null)
        {
            my = new DisplayImageOptionsFactory();
        }
        return my;
    }

    public DisplayImageOptions createDefaultOptions()
    {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        DisplayImageOptions options = createOptions(new SimpleBitmapDisplayer());
        return options;
    }

    public DisplayImageOptions createRoundedOptions(int cornerRadiusPixels)
    {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        DisplayImageOptions options = createOptions(new RoundedBitmapDisplayer(cornerRadiusPixels));
        return options;
    }

    public DisplayImageOptions createRoundedOptions(int cornerRadiusPixels,int margin)
    {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        DisplayImageOptions options = createOptions(new RoundedBitmapDisplayer(cornerRadiusPixels,margin));
        return options;
    }

    public DisplayImageOptions createCircleOptions(int cornerRadiusPixels)
    {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        DisplayImageOptions options = createOptions(new CircleBitmapDisplayer(cornerRadiusPixels));
        return options;
    }

    public DisplayImageOptions createCircleOptions(int cornerRadiusPixels,int margin)
    {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        DisplayImageOptions options = createOptions(new CircleBitmapDisplayer(cornerRadiusPixels,margin));
        return options;
    }

    private DisplayImageOptions createOptions( BitmapDisplayer displayer)
    {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.ic_launcher) // resource or
                // drawable
//				.showImageForEmptyUri(R.drawable.ic_launcher) // resource or
                // drawable
//				.showImageOnFail(R.drawable.ic_launcher) // resource or drawable
                .resetViewBeforeLoading(true) // default
                .delayBeforeLoading(0).cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(false) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(displayer) // default
                .handler(new Handler()) // default
                .build();
        return options;
    }
}
