package net.nym.library.imagegetter;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author nym
 * @date 2015/6/3 0003.
 * @since 1.0
 */
public class ImageAsync extends AsyncTask<String, Integer, Drawable> {

    private URLDrawable drawable;

    public ImageAsync(URLDrawable drawable) {
        this.drawable = drawable;
    }

    @Override
    protected Drawable doInBackground(String... params) {
        // TODO Auto-generated method stub
        String savePath = params[0];
        String url = params[1];

        InputStream in = null;
        try {
            // 获取网络图片
            HttpGet http = new HttpGet(url);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = (HttpResponse) client.execute(http);
            BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(
                    response.getEntity());
            in = bufferedHttpEntity.getContent();

        } catch (Exception e) {
            try {
                if (in != null)
                    in.close();
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }

        if (in == null)
            return drawable;

        try {
            File file = new File(savePath);
            String basePath = file.getParent();
            File basePathFile = new File(basePath);
            if (!basePathFile.exists()) {
                basePathFile.mkdirs();
            }
            file.createNewFile();
            FileOutputStream fileout = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            while (in.read(buffer) != -1) {
                fileout.write(buffer);
            }
            fileout.flush();

            Drawable mDrawable = Drawable.createFromPath(savePath);
            return mDrawable;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return drawable;
    }

    @Override
    protected void onPostExecute(Drawable result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if (result != null) {
            drawable.setDrawable(result);
//            tv_my_web.setText(tv_my_web.getText()); // 通过这里的重新设置 TextView 的文字来更新UI
        }
    }

}