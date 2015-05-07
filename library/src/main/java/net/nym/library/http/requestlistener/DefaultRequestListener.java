/*
 * Copyright (c) 2014  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.http.requestlistener;


import net.nym.library.common.BaseApplication;
import net.nym.library.http.RequestListener;
import net.nym.library.util.Toaster;

/**
 * @author nym
 * @date 2014/10/9 0009.
 * @since 1.0
 */
abstract class DefaultRequestListener<T> implements RequestListener<T> {


    /**
     * @param errorCode 错误码
     * @param message   错误信息
     */
    @Override
    public void onError(int errorCode, String message) {
        Toaster.toaster(BaseApplication.getAppContext(), message == null ? "" : message);
    }

    @Override
    public abstract void onResponse(T object) ;


    @Override
    public void onProgressUpdate(int totals, int progress)
    {

    }
}
