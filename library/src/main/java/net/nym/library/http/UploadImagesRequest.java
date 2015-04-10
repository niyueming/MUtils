/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.http;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import net.nym.library.util.Log;
import net.nym.library.util.NetUtil;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;


public class UploadImagesRequest extends AsyncTask<String, Integer, String> {
//  private String host = new String("http://192.168.18.10:8686/momo/mg/");      //欧弟的机子
    private String host = new String("http://118.144.133.90/momo/mg/");      //公网
	Map<String, Object> mMap;
	Map<String, File> mFiles;
	RequestListener mPostListener;
	ErrorHandler mHandler;
	Context mContext;
	String mUrl;
    private Dialog mDialog;
    private boolean isShowDialog;

	public UploadImagesRequest(Context context, String url,
                               Map<String, Object> map, Map<String, File> files,
                               RequestListener postListener) {
		mContext = context;
		mUrl = url;
		mMap = map;
		mFiles = files;
		mPostListener = postListener;
		mHandler = new ErrorHandler();
	}


	public void setHost(String host) {
		this.host = host;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if (mContext == null) {
            return;
        }
        if (result == null) {
            if (mPostListener != null) {
                mPostListener.onError(
                        RequestListener.WITHOUT_NETWORK_ERROR,
                        RequestListener.ERROR_WITHOUT_NETWORK);
            }
        } else if (mPostListener != null) {
            try {
                JSONObject object = new JSONObject(result);
                Log.println("%s", object.toString());
                mPostListener.onResponse(object.toString());
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (mPostListener != null) {
                    mPostListener.onError(RequestListener.JSON_ERROR,
                            RequestListener.ERROR_JSON_PARSE);
                }
            }
        }


       /* if (mDialog!= null )
        {
            if (mDialog.isShowing())
            {
                mDialog.dismiss();
            }
        }
        if (result == null)
        {
            if (mPostListener != null) {
                mPostListener.onError(RequestListener.OTHER_ERROR,
                        RequestListener.ERROR_OTHER);
            }
            return;
        }
        if (mPostListener != null) {
            mPostListener.onResponse(result);
        }*/
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
      /*  mDialog = new ProgressDialog(mContext);
        mDialog = new Dialog(mContext, R.style.dialog);
        mDialog.setContentView(R.layout.progress_dialog);
        mDialog.show();
        mDialog.dismiss();*/
	}

	@Override
	protected String doInBackground(String... arg0) {
		// TODO Auto-generated method stub
//		if (!OperateSharePreferences.getInstance().hasNet()) {
//			Message msg2 = mHandler.obtainMessage();
//			msg2.obj = RequestListener.ERROR_WITHOUT_NETWORK;
//			msg2.what = RequestListener.IO_ERROR;
//			mHandler.sendMessage(msg2);
//			return null;
//		}

//		BasicHttpParams httpParameters = new BasicHttpParams();// Set the
//																// timeout in
//																// milliseconds
//																// until a
//																// connection is
//																// established.
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 50000);// Set
//																			// the
//																			// default
//																			// socket
//																			// timeout
//																			// (SO_TIMEOUT)
//																			// //
//																			// in
//																			// milliseconds
//																			// which
//																			// is
//																			// the
//																			// timeout
//																			// for
//																			// waiting
//																			// for
//																			// data.
//		HttpConnectionParams.setSoTimeout(httpParameters, 5000);
//		HttpClient httpclient = new DefaultHttpClient();
//
//		String json = null;
//		// 设置通信协议版本
//		httpclient.getParams().setParameter(
//				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
//		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//		if (mMap != null & mMap.size() > 0) {
//			for (Entry<String, Object> entry : mMap.entrySet()) {
//				builder.addTextBody(entry.getKey(), entry.getValue() + "");
////                mUrl += "?" + entry.getKey() + "=" + entry.getValue().toString().replaceAll(Matcher.quoteReplacement("\""), Matcher.quoteReplacement("\\\"")) ;
////				Log.println("%s:%s", entry.getKey(), entry.getValue().toString().replaceAll(Matcher.quoteReplacement("\""), Matcher.quoteReplacement("\\\"")) + "");
//			}
//		}
//		if (mFiles != null) {
//			if (mFiles.size() > 0) {
//				for (Entry<String, File> entry : mFiles.entrySet()) {
//					File file = (File) entry.getValue();
//					Log.println("%s:%s", entry.getKey(), file.getAbsolutePath()
//							+ "");
//					if (file.exists()) {
//						builder.addBinaryBody(entry.getKey(),
//								(File) entry.getValue());
////						builder.addBinaryBody(entry.getKey(), file, ContentType.create("image/png"), file.getName());
//					} else {
//						Log.println("%s is not exists", file.getAbsolutePath()
//								+ "");
//					}
//				}
//			}
//		}
//
//		HttpEntity mpEntity = builder.build();
//		HttpPost httppost = new HttpPost(host + mUrl);
////		httppost.addHeader("enctype", "multipart/form-data");
//		httppost.setEntity(mpEntity);
//		Log.println("url=%s", host + mUrl);
////		HttpParams params = httppost.getParams();
//
//		try {
//			HttpResponse response = httpclient.execute(httppost);
//
//			HttpEntity resEntity = response.getEntity();
//			// Log.println(response.getStatusLine().toString() + "");// 通信Ok
//			if (resEntity != null) {
//				json = EntityUtils.toString(resEntity, HTTP.UTF_8);
//
//				Log.println("result=%s", json);
//
//			}
//
//			if (resEntity != null) {
//
//				resEntity.consumeContent();
//
//			}
//			httpclient.getConnectionManager().shutdown();
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ClientProtocolException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ConnectTimeoutException e) {
//			e.printStackTrace();
//			Message msg1 = mHandler.obtainMessage();
//			msg1.obj = e.getMessage();
//			msg1.what = RequestListener.TIMEOUT_ERROR;
//			mHandler.sendMessage(msg1);
//		} catch (SocketTimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Message msg1 = mHandler.obtainMessage();
//			msg1.obj = e.getMessage();
//			msg1.what = RequestListener.TIMEOUT_ERROR;
//			mHandler.sendMessage(msg1);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//			Message msg2 = mHandler.obtainMessage();
//			msg2.obj = e1.getMessage();
//			msg2.what = RequestListener.IO_ERROR;
//			mHandler.sendMessage(msg2);
//		}

        String json = null;
		try {
			json = postFile(host + mUrl,mMap,mFiles);
            Log.i(json + "");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			Message msg1 = mHandler.obtainMessage();
			msg1.obj = e.getMessage();
			msg1.what = RequestListener.TIMEOUT_ERROR;
			mHandler.sendMessage(msg1);
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message msg1 = mHandler.obtainMessage();
			msg1.obj = e.getMessage();
			msg1.what = RequestListener.TIMEOUT_ERROR;
			mHandler.sendMessage(msg1);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Message msg2 = mHandler.obtainMessage();
			msg2.obj = e1.getMessage();
			msg2.what = RequestListener.IO_ERROR;
			mHandler.sendMessage(msg2);
		}

		return json;
	}

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param url
     *            Service net address
     * @param params
     *            text content
     * @param files
     *            pictures
     * @return String result of Service response
     * @throws java.io.IOException
     */
    public static String postFile(String url, Map<String, Object> params,
                                  Map<String, File> files) throws IOException {
        String result = "";
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

        StringBuilder sb = new StringBuilder("?");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
//                sb.append(PREFIX);
//                sb.append(BOUNDARY);
//                sb.append(LINEND);
//                sb.append("Content-Disposition: form-data; name=\""
//                        + entry.getKey() + "\"" + LINEND);
//                sb.append("Content-Type: text/plain; charset=" + CHARSET
//                        + LINEND);
//                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
//                sb.append(LINEND);
//                sb.append(entry.getValue());
//                sb.append(LINEND);
//                String key = entry.getKey();
//                sb.append("&");
//                sb.append(key).append("=").append(params.get(key));
//                Log.i("%s=%s",key,params.get(key).toString());
            sb.append(  entry.getKey() )
                    .append( "=" )
                    .append(NetUtil.URLEncode(entry.getValue().toString()))
                    .append("&")
            ;
        }
        sb.deleteCharAt(sb.length() - 1);
            URL uri = new URL(url + sb.toString());
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
            conn.setConnectTimeout(50000);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                    + ";boundary=" + BOUNDARY);
            // 首先组拼文本类型的参数
//            StringBuilder sb = new StringBuilder();
//            for (Map.Entry<String, Object> entry : params.entrySet()) {
////                sb.append(PREFIX);
////                sb.append(BOUNDARY);
////                sb.append(LINEND);
////                sb.append("Content-Disposition: form-data; name=\""
////                        + entry.getKey() + "\"" + LINEND);
////                sb.append("Content-Type: text/plain; charset=" + CHARSET
////                        + LINEND);
////                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
////                sb.append(LINEND);
////                sb.append(entry.getValue());
////                sb.append(LINEND);
//                String key = entry.getKey();
//                sb.append("&");
//                sb.append(key).append("=").append(params.get(key));
//                Log.i("%s=%s",key,params.get(key).toString());
//            }

            DataOutputStream outStream = new DataOutputStream(
                    conn.getOutputStream());
//            outStream.write(sb.toString().getBytes());
            // 拼接文件数据
            if (files != null) {
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sbFile = new StringBuilder();
                    sbFile.append(PREFIX);
                    sbFile.append(BOUNDARY);
                    sbFile.append(LINEND);
                    /**
                     * 这里重点注意： name里面的值为服务端需要key 只有这个key 才可以得到对应的文件
                     * filename是文件的名字，包含后缀名的 比如:abc.png
                     */
                    sbFile.append("Content-Disposition: form-data; name=\""+ file.getKey() +"\"; filename=\""
                            + file.getValue().getName() + "\"" + LINEND);
                    sbFile.append("Content-Type: application/octet-stream; charset="
                            + CHARSET + LINEND);
                    sbFile.append(LINEND);
                    Log.i(sbFile.toString());
                    outStream.write(sbFile.toString().getBytes());

                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    is.close();
                    outStream.write(LINEND.getBytes());
                }
                // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            }

            outStream.flush();
            // 得到响应码
            int res = conn.getResponseCode();
            InputStream in = conn.getInputStream();
            StringBuilder sbResult = new StringBuilder();
            if (res == 200) {
                int ch;
                while ((ch = in.read()) != -1) {
                    sbResult.append((char) ch);
                }
            }
            result = sbResult.toString();
            outStream.close();
            conn.disconnect();
        return result;
    }


	private class ErrorHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case RequestListener.IO_ERROR:
				if (mPostListener != null) {
                    mPostListener.onError(RequestListener.IO_ERROR,
                            msg.obj + "");
				}
				break;
			case RequestListener.TIMEOUT_ERROR:
				if (mPostListener != null) {
                    mPostListener.onError(RequestListener.TIMEOUT_ERROR,
                            msg.obj + "");
				}
				break;
			default:
				break;
			}
		}

	}

}