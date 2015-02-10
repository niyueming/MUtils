/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.cookie;

import android.content.Context;

import net.nym.library.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * @author nym
 * @date 2015/2/10 0010.
 * @since 1.0
 */
public class CookieTest {


    private String getRequest(Context context,String urlString, Map<String, Object> params) throws IOException {
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(params.get(key));
            i++;
        }

        Log.i(urlString + param.toString());
        //将URL与参数拼接
        HttpGet getMethod = new HttpGet(urlString  + param.toString());

        DefaultHttpClient client = new DefaultHttpClient();

        client.setCookieStore(new PersistentCookieStore(context));
        HttpResponse response = client.execute(getMethod);
        if(response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK)
        {
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            return result;
        }
        return null;
    }
}
