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
import net.nym.library.util.Log;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.client.util.HttpAsyncClientUtils;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;

import java.io.File;
import java.io.IOException;
import java.net.HttpRetryException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nym
 * @date 2014/10/9 0009.
 * @since 1.0
 */
public class HttpRequest<T> extends AsyncTask<Class<T>, Integer, T> {

    private RequestListener<T> mRequestListener;
    private ErrorHandler mErrorHandler = new ErrorHandler();
    HttpAsyncRequestProducer mHttpAsyncRequestProducer;
    private Dialog mDialog;
    private CloseableHttpAsyncClient mClient;

    public enum Method{
        GET,
        POST,
        PUT,
        TRACE,
        Options,
        Delete
    }
//    public HttpRequest(Method method,RequestListener listener)
//    {
//        mMethod = method;
//        mRequestListener = listener;
//    }

    public void setHttpAsyncRequestProducer(HttpAsyncRequestProducer producer) {
        this.mHttpAsyncRequestProducer = producer;
    }

    public void setClient(CloseableHttpAsyncClient mClient) {
        this.mClient = mClient;
    }

    public void setRequestListener(RequestListener<T> mRequestListener) {
        this.mRequestListener = mRequestListener;
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

        try {
            mClient.execute(mHttpAsyncRequestProducer.getTarget(), mHttpAsyncRequestProducer.generateRequest(),new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse result) {

                }

                @Override
                public void failed(Exception ex) {
                    mErrorHandler.sendEmptyMessage(RequestListener.OTHER_ERROR);
                }

                @Override
                public void cancelled() {
                    Log.i("request is cancelled");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        }finally {
            try {
                mClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        private Method mMethod = Method.GET;
        private ArrayList<NameValuePair> mParams = new ArrayList<NameValuePair>();
        private HashMap<String,File> mFiles = new HashMap<String, File>();
        private String mUrl;

        public Builder(Context context,String url)
        {
            mContext = context;
            mUrl = url;
        }

        public Builder<T> setUrl(String url)
        {
            this.mUrl = url;
            return this;
        }

        public Builder<T> setRequestListener(RequestListener<T> requestListener)
        {
            this.mRequestListener = requestListener;
            return this;
        }

        public Builder<T> setMethod( Method method)
        {
            this.mMethod = method;
            return this;
        }

        public Builder<T> addParameter(String name,String value)
        {
            NameValuePair pair = new BasicNameValuePair(name,value);
            mParams.add(pair);
            return this;
        }

        public Builder<T> addFile(String name,File file)
        {
            if (mMethod == Method.POST)
            {
                mFiles.put(name,file);
            }
            else
            {
                throw new RuntimeException("it is not httppost");
            }

            return this;
        }


        public HttpRequest<T> build()
        {
            HttpRequest<T> request = new HttpRequest<T>();
            if (mMethod == null)
            {
                mMethod = Method.GET;
            }
            HttpAsyncRequestProducer producer = null;
            switch (mMethod)
            {
                case GET:
                    final StringBuffer sb = new StringBuffer();
                    for (NameValuePair pair : mParams)
                    {
                        sb.append(pair.getName()).append('=').append(pair.getValue()).append('&');
                    }
                    if (sb.length() > 0)
                    {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    if (mUrl.contains("?"))
                    {
                        mUrl += "&" + sb.toString();
                    }
                    else
                    {
                        mUrl += "?" + sb.toString();
                    }
                    producer = HttpAsyncMethods.createGet(mUrl);
                    break;
                case POST:
                    HttpPost post = new HttpPost(mUrl);
                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                    for (NameValuePair pair : mParams)
                    {
                        builder.addTextBody(pair.getName(),pair.getValue());
                        Log.println("%s:%s", pair.getName() + "",pair.getValue() + "");
                    }
                    if (mFiles.size() > 0) {
                        for (Map.Entry<String, File> entry : mFiles.entrySet()) {
                            File file = (File) entry.getValue();
                            Log.println("%s:%s", entry.getKey(), file.getAbsolutePath()
                                    + "");
                            if (file.exists()) {
                                builder.addBinaryBody(entry.getKey(), file, ContentType.create(MimeTypeUtils.getMimeType(file.getName())), file.getName());
                            } else {
                                Log.println("%s is not exists", file.getAbsolutePath()
                                        + "");
                                continue;
                            }
                        }
                    }
                    post.setEntity(builder.build());
                    producer = HttpAsyncMethods.create(post);
                    break;
                default:
                    Log.i("%s", "only temporarily support get and post method now");
                    break;
            }
            Log.println("url=%s", mUrl);
            request.setHttpAsyncRequestProducer(producer);

            request.setRequestListener(mRequestListener);

            CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
            request.setClient(client);

            return request;
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
