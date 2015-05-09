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
import net.nym.library.task.AsyncTask;
import net.nym.library.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.LinkedHashMap;

/**
 * @author nym
 * @date 2014/10/9 0009.
 * @since 1.0
 */
public class HttpRequestJsonArray extends AsyncTask<Object, Integer, String> {

    /**
     * 允许请求失败次数
     * */
    private static int maxRetry = 1;
    private String mUrl;
    private RequestListener<JSONArray> mRequestListener;
    private ErrorHandler mErrorHandler = new ErrorHandler();
    private Dialog mDialog;
    private Method mMethod = Method.GET;
    private boolean isShowDialog;
    private Context mContext;
    private boolean isBaseActivity;
    private LinkedHashMap<String, Object> mParams;
    private LinkedHashMap<String, Object> mFiles;


    public HttpRequestJsonArray(Context context, String url, Method method, RequestListener<JSONArray> listener
            ,LinkedHashMap<String, Object> mParams,LinkedHashMap<String, Object> mFiles, int retry,boolean isShowDialog) {
        setContext(context);
        setUrl(url);
        setMethod(method);
        setRequestListener(listener);
        setRetry(retry);
        setShowDialog(isShowDialog);
        setParams(mParams);
        setFiles(mFiles);
    }
    public HttpRequestJsonArray(Context context, String url, Method method, RequestListener<JSONArray> listener
            , LinkedHashMap<String, Object> mParams,LinkedHashMap<String, Object> mFiles,boolean isShowDialog) {
        this(context, url, method, listener,mParams,mFiles, maxRetry, isShowDialog);
    }

    public HttpRequestJsonArray(Context context, String url, Method method, RequestListener<JSONArray> listener
            ,LinkedHashMap<String, Object> mParams,LinkedHashMap<String, Object> mFiles) {
        this(context, url, method, listener,mParams,mFiles, true);
    }

    public HttpRequestJsonArray(Context context, String url, Method method, RequestListener<JSONArray> listener
            ,LinkedHashMap<String, Object> mParams) {
        this(context, url, method, listener,mParams,null, true);
    }

    public HttpRequestJsonArray(Context context, String url, Method method, RequestListener<JSONArray> listener
            ,LinkedHashMap<String, Object> mParams,boolean isShowDialog) {
        this(context, url, method, listener,mParams,null, isShowDialog);
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
//        if (BaseActivity.class.isInstance(context))
//        {
//            isBaseActivity = true;
//        }
    }


    public void setRequestListener(RequestListener mRequestListener) {
        this.mRequestListener = mRequestListener;
    }

    public void setParams(LinkedHashMap<String, Object> mParams) {
        this.mParams = mParams;
    }

    public void setFiles(LinkedHashMap<String, Object> mFiles) {
        this.mFiles = mFiles;
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
        if (isShowDialog & mContext != null) {
            if (isBaseActivity)
            {
//                ((BaseActivity)mContext).showDialog(BaseActivity.DIALOG_LOADING);
            }
            else {
                mDialog = new ProgressDialog(mContext);
                mDialog.show();
            }
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
    protected String doInBackground(Object... params) {
        String result = null;

        try {
            switch (mMethod) {
                case GET:
                    result = Request.get(mUrl, mParams, maxRetry);
                    break;
                case POST:
                    result = Request.post(mUrl, mParams, mFiles, maxRetry);
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

        Log.i("result=%s", result + "");
        android.util.Log.i("result", String.format("result=%s", result + ""));
        return result;
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
        if (isShowDialog & mContext != null) {

            if (isBaseActivity)
            {
//                ((BaseActivity)mContext).dismissDialog(BaseActivity.DIALOG_LOADING);
            }
            else if (mDialog != null){
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        }
        if (t == null) {
            return;
        }
        if (mRequestListener != null) {
            if (JSONTypeUtil.getJSONType(t) == JSONTypeUtil.JSON_TYPE.JSON_TYPE_ARRAY)
                try {
                    mRequestListener.onResponse(new JSONArray(t));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public boolean cancel() {
        if (isShowDialog & mContext != null) {
            if (isBaseActivity)
            {
//                ((BaseActivity)mContext).dismissDialog(BaseActivity.DIALOG_LOADING);
            }
            else if (mDialog != null){
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
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
            if (isShowDialog & mContext != null) {
                if (isBaseActivity)
                {
//                    ((BaseActivity)mContext).dismissDialog(BaseActivity.DIALOG_LOADING);
                }
                else if (mDialog != null){
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
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
