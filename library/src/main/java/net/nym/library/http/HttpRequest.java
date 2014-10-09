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


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import net.nym.library.task.AsyncTask;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;

/**
 * @author nym
 * @date 2014/10/9 0009.
 * @since 1.0
 */
public class HttpRequest<T> extends AsyncTask<Class<T>, Integer, T> {

    private RequestListener<T> mRequestListener;
    private ErrorHandler mErrorHandler = new ErrorHandler();
    private Method mMethod;
    private Dialog mDialog;


    public enum Method{
        GET,
        POST;
    }
    public HttpRequest(Method method,RequestListener listener)
    {
        mMethod = method;
        mRequestListener = listener;
    }
//    class MResponseHandler<T> implements ResponseHandler<T> {

//
//        @Override
//        public T handleResponse(
//                final HttpResponse response) throws IOException {
//            StatusLine statusLine = response.getStatusLine();
//            HttpEntity entity = response.getEntity();
//            if (statusLine.getStatusCode() >= 300) {
//                throw new HttpResponseException(
//                        statusLine.getStatusCode(),
//                        statusLine.getReasonPhrase());
//            }
//            if (entity == null) {
//                throw new ClientProtocolException("Response contains no content");
//            }
//            ContentType contentType = ContentType.getOrDefault(entity);
//            Charset charset = contentType.getCharset();
//            String result = EntityUtils.toString(entity,charset.name());
//            return null;
//        }
//    }

    /**
     * Runs on the UI thread before {@link #doInBackground}.
     *
     * @see #onPostExecute
     * @see #doInBackground
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
    protected T doInBackground(Class<T>... params) {
        if (params == null)
        {
            return null;
        }
        if (params.length < 1)
        {
            return null;
        }

        T instance = null;
        try {
            instance = params[0].newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (instance == null)
        {
            return null;
        }

        return instance;
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
        if (values == null)
        {
            return;
        }
        if (mRequestListener != null & values.length >= 2) {
            mRequestListener.onProgressUpdate(values[0],values[1]);
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
    protected void onPostExecute(T t) {
        if (t == null)
        {
            mErrorHandler.sendEmptyMessage(RequestListener.OTHER_ERROR);
            return;
        }
        if (mRequestListener != null) {
            mRequestListener.onResponse(t);
        }
    }

    public static void clear()
    {
        sPoolWorkQueue.clear();
    }

    public static class Builder<T>{
        private RequestListener<T> mRequestListener;
        private Context mContext;
        public Builder(Context context)
        {
            mContext = context;
        }

        public Builder setRequestListener(RequestListener<T> requestListener)
        {
            this.mRequestListener = requestListener;
            return this;
        }
    }

    private class ErrorHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
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
