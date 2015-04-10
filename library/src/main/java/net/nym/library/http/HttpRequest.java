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


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import net.nym.library.cookie.PersistentCookieStore;
import net.nym.library.javabean.JSONTypeUtil;
import net.nym.library.javabean.JavaBeanParser;
import net.nym.library.task.AsyncTask;
import net.nym.library.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author nym
 * @date 2014/10/9 0009.
 * @since 1.0
 */
public class HttpRequest extends AsyncTask<LinkedHashMap<String, Object>, Integer, String> {

    /**
     * 允许请求失败次数
     * */
    private int maxRetry = 1;
    private String mUrl;
    private RequestListener mRequestListener;
    private ErrorHandler mErrorHandler = new ErrorHandler();
    private Dialog mDialog;
    private Method mMethod = Method.GET;
    private boolean isShowDialog;
    private Context mContext;

    public enum Method {
        GET,
        POST,
        PUT,
        TRACE,
        Options,
        Delete
    }

    public HttpRequest(Context context, String url, Method method, RequestListener listener, int retry,boolean isShowDialog) {
        setContext(context);
        setUrl(url);
        setMethod(method);
        setRequestListener(listener);
        setRetry(retry);
        setShowDialog(isShowDialog);
    }
    public HttpRequest(Context context, String url, Method method, RequestListener listener, boolean isShowDialog) {
        this(context,url,method,listener,1,isShowDialog);
    }

    public HttpRequest(Context context, String url, Method method, RequestListener listener) {
        this(context, url, method, listener, true);
    }

    /**
     * @param retry 重试次数
     * */
    public void setRetry(int retry)
    {
        this.maxRetry = retry;
    }
    public void setMethod(Method method) {
        this.mMethod = method;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setShowDialog(boolean isShowDialog) {
        this.isShowDialog = isShowDialog;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }


    public void setRequestListener(RequestListener mRequestListener) {
        this.mRequestListener = mRequestListener;
    }


    /**
     * Runs on the UI thread before {@link #doInBackground}.
     *
     * @see #onPostExecute
     * @see #doInBackground
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isShowDialog) {
            mDialog = new ProgressDialog(mContext);
            mDialog.show();
        }
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected String doInBackground(LinkedHashMap<String, Object>... params) {
        String result = null;
        if (params == null) {
        } else if (params.length >= 1 && params[0] != null) {

            try {
                switch (mMethod) {
                    case GET:
                        result = getRequest(mUrl, params[0]);
                        break;
                    case POST:
                        if (params.length >= 2)
                        {
                            result = postRequest(mUrl, params[0],params[1]);
                        }
                        else
                        {
                            result = postRequest(mUrl, params[0],null);
                        }
                        break;
                    case PUT:

                        break;
                    case TRACE:

                        break;
                    case Options:

                        break;
                    case Delete:

                        break;
                }

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                mErrorHandler.sendEmptyMessage(RequestListener.TIMEOUT_ERROR);
            } catch (ConnectTimeoutException e) {
                e.printStackTrace();
                mErrorHandler.sendEmptyMessage(RequestListener.TIMEOUT_ERROR);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                mErrorHandler.sendEmptyMessage(RequestListener.TIMEOUT_ERROR);
            } catch (IOException e) {
                e.printStackTrace();
                mErrorHandler.sendEmptyMessage(RequestListener.IO_ERROR);
            }
        }

        Log.i("result=%s", result + "");
        android.util.Log.i("result", String.format("result=%s", result + ""));
        return result;
    }

    private String getRequest(String urlString, LinkedHashMap<String, Object> params) throws IOException {
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
        HttpGet getMethod = new HttpGet(urlString + param.toString());

        DefaultHttpClient client = new DefaultHttpClient();

        //支持cookie
        client.setCookieStore(new PersistentCookieStore(mContext));

        //允许重复
        client.setHttpRequestRetryHandler(new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                Log.i("retry=%d",i);
                if (i < maxRetry)
                {
                    return  true;
                }
                return false;
            }
        });

        HttpResponse response = client.execute(getMethod);
        if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            return result;
        }
        return null;
    }

    private String postRequest(String urlString, LinkedHashMap<String, Object> params, LinkedHashMap<String, Object> files) throws IOException {

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        for (String key : params.keySet()) {
            builder.addTextBody(key,params.get(key) + "");
            Log.i("%s:%s", key, params.get(key) + "");
        }

        if (files != null)
        {
            for (String key : files.keySet()) {
                File file = new File(files.get(key) + "");
                if (!file.exists())
                {
                    continue;
                }
                builder.addBinaryBody(key,file );
                Log.i("%s:%s", key, file.toString() + "");
            }
        }


        //将URL与参数拼接
        HttpPost getMethod = new HttpPost(urlString);
        getMethod.setEntity(builder.build());

        DefaultHttpClient client = new DefaultHttpClient();
        client.setCookieStore(new PersistentCookieStore(mContext));
        client.setHttpRequestRetryHandler(new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                Log.i("retry=%d", i);
                if (i < maxRetry) {
                    return true;
                }
                return false;
            }
        });

        HttpResponse response = client.execute(getMethod);
        if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            return result;
        }
        return null;
    }

    /**
     * Runs on the UI thread after {@link #publishProgress} is invoked.
     * The specified values are the values passed to {@link #publishProgress}.
     *
     * @param values The values indicating progress.
     * @see #publishProgress
     * @see #doInBackground
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        if (values == null) {
            return;
        }
        if (mRequestListener != null & values.length >= 2) {
            mRequestListener.onProgressUpdate(values[0], values[1]);
        }
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p/>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param t The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(String t) {
        if (isShowDialog & mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
        if (t == null) {
            return;
        }
        if (mRequestListener != null) {
            mRequestListener.onResponse(t);
        }
    }

    @Override
    public boolean cancel() {
        if (isShowDialog & mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
        return super.cancel();
    }

    public static void clear() {
        sPoolWorkQueue.clear();
    }


    private class ErrorHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (isShowDialog & mDialog != null) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
            switch (msg.what) {
                case RequestListener.IO_ERROR:
                    if (mRequestListener != null) {
                        mRequestListener.onError(msg.what,
                                RequestListener.ERROR_IO);
                    }
                    break;
                case RequestListener.WITHOUT_NETWORK_ERROR:
                    if (mRequestListener != null) {
                        mRequestListener.onError(msg.what,
                                RequestListener.ERROR_WITHOUT_NETWORK);
                    }
                    break;
                case RequestListener.TIMEOUT_ERROR:
                    if (mRequestListener != null) {
                        mRequestListener.onError(msg.what,
                                RequestListener.ERROR_TIMEOUT);
                    }
                    break;
                case RequestListener.JSON_ERROR:
                    if (mRequestListener != null) {
                        mRequestListener.onError(msg.what,
                                RequestListener.ERROR_JSON_PARSE);
                    }
                    break;
                case RequestListener.OTHER_ERROR:
                    if (mRequestListener != null) {
                        mRequestListener.onError(msg.what,
                                RequestListener.ERROR_OTHER);
                    }
                    break;
            }
        }

    }
}
