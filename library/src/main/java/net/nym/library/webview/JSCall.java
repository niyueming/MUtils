/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.webview;

import android.app.Activity;
import android.webkit.JavascriptInterface;

/**
 * JavascriptInterface,不要被混淆
 * @author nym
 * @date 2014/9/30 0030.
 */
public class JSCall {
    Activity mActivity;

    public JSCall(Activity activity)
    {
        mActivity = activity;
    }

    @JavascriptInterface
    public void loginTimeOutCall()
    {
        mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {



            }
        });
    }
}
