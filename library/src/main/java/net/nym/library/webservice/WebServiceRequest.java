/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.webservice;


import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import net.nym.library.http.RequestListener;
import net.nym.library.task.AsyncTask;
import net.nym.library.util.Log;

import java.util.HashMap;
import java.util.Map;


/**
 * @author nym
 * @date 2014/10/9 0009.
 * @since 1.0
 */
public class WebServiceRequest extends AsyncTask<Map<String,Object>, Integer, String> {

    private RequestListener<String> mRequestListener;
    private ErrorHandler mErrorHandler = new ErrorHandler();
    private Dialog mDialog;
    private boolean isShowDialog;
    private Context mContext;
    private String mMethodName;

    public WebServiceRequest(Context context,String methodName,RequestListener listener,boolean isShowDialog)
    {
        mContext = context;
        mMethodName = methodName;
        mRequestListener = listener;
        this.isShowDialog = isShowDialog;
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
        if (isShowDialog)
        {
//            mDialog = new ProgressDialog(mContext);
//            mDialog = new Dialog(mContext, R.style.dialog);
//            mDialog.setContentView(R.layout.progress_dialog);
//            mDialog.show();
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
    protected String doInBackground(Map<String,Object>... params) {
        Map<String,Object> map = new HashMap<String,Object>();
        if (params == null)
        {
        }
        else if (params.length >= 1 && params[0] != null)
        {
            map = params[0];
        }

        String result = WebServiceVisitor.callWebService(mMethodName,map);

        Log.i("result=%s", result + "");
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
     * @param result The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(String result) {
        if (isShowDialog & mDialog!= null )
        {
            if (mDialog.isShowing())
            {
                mDialog.dismiss();
            }
        }
        if (result == null)
        {
            if (mRequestListener != null) {
                mRequestListener.onError(RequestListener.OTHER_ERROR,
                        RequestListener.ERROR_OTHER);
            }
            return;
        }
        if (mRequestListener != null) {
            mRequestListener.onResponse(result);
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
            }
        }

    }
}
