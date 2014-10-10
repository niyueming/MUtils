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
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import net.nym.library.javabean.JSONTypeUtil;
import net.nym.library.javabean.JavaBeanParser;
import net.nym.library.task.AsyncTask;
import net.nym.library.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
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
    private Dialog mDialog;
    private Method mMethod;
    private HttpClient mClient;
    private HttpGet mHttpGet;
    private HttpPost mHttpPost;
    private boolean isShowDialog;
    private Context mContext;

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

    public void setClient(HttpClient mClient) {
        this.mClient = mClient;
    }
    public void setMethod(Method method) {
        this.mMethod = method;
    }

    public void setShowDialog(boolean isShowDialog) {
        this.isShowDialog = isShowDialog;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setHttpGet(HttpGet mHttpGet) {
        this.mHttpGet = mHttpGet;
    }

    public void setHttpPost(HttpPost mHttpPost) {
        this.mHttpPost = mHttpPost;
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
        if (isShowDialog)
        {
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
            HttpResponse response = null;
            switch (mMethod)
            {
                case GET:
                    response = mClient.execute(mHttpGet);
                    break;
                case POST:
                    response = mClient.execute(mHttpPost);
                    break;
                default:
                    Log.i("%s", "only temporarily support get and post method now");
                    break;
            }
            if (response == null)
            {
                mErrorHandler.sendEmptyMessage(RequestListener.OTHER_ERROR);
            }
            else
            {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                {
                    HttpEntity httpEntity = response.getEntity();
                    if(httpEntity == null)
                    {
                        mErrorHandler.sendEmptyMessage(RequestListener.OTHER_ERROR);
                    }
                    else
                    {
                        ContentType contentType = ContentType.getOrDefault(httpEntity);
                        Charset charset = contentType.getCharset();
                        String result = EntityUtils.toString(httpEntity,charset.name());
                        if (String.class.isInstance(instance))
                        {
                            instance = (T)result;
                        }
                        else if (JSONObject.class.isInstance(instance))
                        {
                            if (JSONTypeUtil.getJSONType(result) == JSONTypeUtil.JSON_TYPE.JSON_TYPE_OBJECT) {
                                instance = (T)new JSONObject(result);
                            }
                            else {
                                mErrorHandler.sendEmptyMessage(RequestListener.JSON_ERROR);
                            }
                        }
                        else if (JSONArray.class.isInstance(instance))
                        {
                            if (JSONTypeUtil.getJSONType(result) == JSONTypeUtil.JSON_TYPE.JSON_TYPE_ARRAY) {
                                instance = (T)new JSONArray(result);
                            }
                            else {
                                mErrorHandler.sendEmptyMessage(RequestListener.JSON_ERROR);
                            }
                        }
                        else
                        {
                            switch (JSONTypeUtil.getJSONType(result))
                            {
                                case JSON_TYPE_OBJECT:
                                    instance = JavaBeanParser.parserJSONObject(params[0],new JSONObject(result));
                                    break;
                                case JSON_TYPE_ARRAY:
                                    ArrayList<T> list = JavaBeanParser.parserJSONArray(params[0],new JSONArray(result));
                                    Message message = mErrorHandler.obtainMessage();
                                    message.what = RequestListener.RESULT_LIST;
                                    message.obj = list;
                                    mErrorHandler.sendMessage(message);
                                    break;
                                case JSON_TYPE_ERROR:
                                    mErrorHandler.sendEmptyMessage(RequestListener.JSON_ERROR);
                                    break;

                            }
                        }

                    }
                }
                else
                {
                    Log.i("reasonPhrase=%s",response.getStatusLine().getReasonPhrase() + "");
                    mErrorHandler.sendEmptyMessage(RequestListener.OTHER_ERROR);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mErrorHandler.sendEmptyMessage(RequestListener.JSON_ERROR);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            mErrorHandler.sendEmptyMessage(RequestListener.OTHER_ERROR);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            mErrorHandler.sendEmptyMessage(RequestListener.OTHER_ERROR);
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            mErrorHandler.sendEmptyMessage(RequestListener.TIMEOUT_ERROR);
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            mErrorHandler.sendEmptyMessage(RequestListener.TIMEOUT_ERROR);
        }catch (IOException e) {
            e.printStackTrace();
            mErrorHandler.sendEmptyMessage(RequestListener.IO_ERROR);
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
        if (isShowDialog & mDialog!= null )
        {
            if (mDialog.isShowing())
            {
                mDialog.dismiss();
            }
        }
        if (t == null)
        {
            return;
        }
        if (mRequestListener != null) {
            mRequestListener.onResponse(t);
        }
    }

    @Override
    public boolean cancel() {
        if (isShowDialog & mDialog!= null )
        {
            if (mDialog.isShowing())
            {
                mDialog.dismiss();
            }
        }
        return super.cancel();
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
        private boolean isShowDialog;

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

        public Builder<T> setShowDialog(boolean isShowDialog)
        {
            this.isShowDialog = isShowDialog;
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
            request.setMethod(mMethod);
            switch (mMethod)
            {
                case GET:
                    final StringBuffer sb = new StringBuffer();
                    for (NameValuePair pair : mParams)
                    {
                        sb.append(pair.getName()).append('=').append(pair.getValue()).append('&');
                        Log.println("%s:%s", pair.getName() + "",pair.getValue() + "");
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
                    final HttpGet get = new HttpGet(mUrl);
                    request.setHttpGet(get);
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
                    request.setHttpPost(post);
                    break;
                default:
                    Log.i("%s", "only temporarily support get and post method now");
                    break;
            }
            Log.println("url=%s", mUrl);

            if (mRequestListener == null)
            {
                mRequestListener = new DefaultRequestListener<T>(mContext);
            }
            request.setRequestListener(mRequestListener);
            request.setContext(mContext);
            request.setShowDialog(isShowDialog);

            HttpClient client = new DefaultHttpClient();
            request.setClient(client);

            return request;
        }

    }

    private class ErrorHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (isShowDialog & mDialog!= null )
            {
                if (mDialog.isShowing())
                {
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
                case RequestListener.RESULT_LIST:
                    if (mRequestListener != null){
                        mRequestListener.onResponse((ArrayList<T>)msg.obj);
                    }
                    break;
            }
        }

    }
}
