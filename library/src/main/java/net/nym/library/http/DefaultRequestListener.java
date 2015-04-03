/*
 * Copyright (c) 2014  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.http;

import android.content.Context;

import net.nym.library.util.Log;
import net.nym.library.util.Toaster;

import java.util.ArrayList;

/**
 * @author nym
 * @date 2014/10/9 0009.
 * @since 1.0
 */
public class DefaultRequestListener implements RequestListener {
    Context mContext;

    public DefaultRequestListener(Context context) {
        mContext = context;
    }

    /**
     * @param errorCode 错误码
     * @param message   错误信息
     */
    @Override
    public void onError(int errorCode, String message) {
        Toaster.toaster(mContext, message == null ? "" : message);
    }

    @Override
    public void onResponse(String object) {
        Log.i("onResponse=%s",object+ "");
    }


    @Override
    public void onProgressUpdate(int totals, int progress)
    {

    }
}
