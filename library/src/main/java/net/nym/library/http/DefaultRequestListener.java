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

import net.nym.library.util.Toaster;

/**
 * @author nym
 * @date 2014/10/9 0009.
 * @since 1.0
 */
public abstract class DefaultRequestListener<T> implements RequestListener<T> {
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
//        switch(errorCode)
//        {
//            case RequestListener.IO_ERROR:
//                Toaster.toaster(mContext, RequestListener.ERROR_OTHER);
//                break;
//            case RequestListener.WITHOUT_NETWORK_ERROR:
//                Toaster.toaster(mContext, RequestListener.ERROR_WITHOUT_NETWORK);
//                break;
//            case RequestListener.TIMEOUT_ERROR:
//                Toaster.toaster(mContext, RequestListener.ERROR_TIMEOUT);
//                break;
//            case RequestListener.JSON_ERROR:
//                Toaster.toaster(mContext, RequestListener.ERROR_JSON_PARSE);
//                break;
//        }
    }

    @Override
    public void onProgressUpdate(int totals, int progress)
    {

    }
}
