package net.nym.library.http;


import net.nym.library.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.LinkedHashMap;

/**
 * @author nym
 * @date 2015/5/9 0009.
 * @since 1.0
 */
public class Request {

    public static String get(String urlString, LinkedHashMap<String, Object> params,int maxRetry) throws IOException {
        return request(Method.GET,urlString,params,null,maxRetry);
    }

    public static String post(String urlString, LinkedHashMap<String, Object> params, LinkedHashMap<String, Object> files,int maxRetry) throws IOException {
        return request(Method.POST,urlString,params,files,maxRetry);
    }

    private static String request(Method method ,String urlString, LinkedHashMap<String, Object> params, LinkedHashMap<String, Object> files,int maxRetry) throws IOException {
        switch (method){
            case GET:
                return getRequest(urlString, params, maxRetry);
            case POST:
                return postRequest(urlString, params, files, maxRetry);
        }
        return null;
    }

    private static String postRequest(String urlString, LinkedHashMap<String, Object> params, LinkedHashMap<String, Object> files, final int maxRetry) throws IOException {
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        if (params != null){
            for (String key : params.keySet()) {
                builder.addTextBody(key,params.get(key) + "");
                Log.i("%s:%s", key, params.get(key) + "");
            }
        }

        if (files != null)
        {
            for (String key : files.keySet()) {
                File file = null;
                if (File.class.isInstance(files.get(key)))
                {
                    file = (File) files.get(key);
                }
                else {
                    file = new File(files.get(key) + "");
                }
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
//        client.setCookieStore(new PersistentCookieStore(mContext));
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
    private static String getRequest(String urlString, LinkedHashMap<String, Object> params, final int maxRetry) throws IOException {
        StringBuffer param = new StringBuffer();
        if (param != null){

            int i = 0;
            for (String key : params.keySet()) {
                if (i == 0)
                    param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(params.get(key));
                i++;
            }

        }

        Log.i(urlString + param.toString());
        //将URL与参数拼接
        HttpGet getMethod = new HttpGet(urlString + param.toString());

        DefaultHttpClient client = new DefaultHttpClient();

        //支持cookie
//        client.setCookieStore(new PersistentCookieStore(mContext));

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
}
