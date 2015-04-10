/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.broadcastreceiver;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;


/**
 * Created by sunliang on 2014/12/8 0008.
 * new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
 */
public class DownloadCompletedReceiver extends BroadcastReceiver {
    private final String TAG = DownloadCompletedReceiver.class.getSimpleName();
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("----------------");
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//            if (BaseApplication.getAppContext().getDownloadId() != downId) {
//                return;
//            }

            DownloadManager manager = (DownloadManager) context.getSystemService(Activity.DOWNLOAD_SERVICE);

            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downId);
            Cursor cursor = manager.query(query);
            if(cursor.moveToFirst()) {
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                switch(status) {
                    case DownloadManager.STATUS_PAUSED:
                        Log.v(TAG,"STATUS_PAUSED");
                    case DownloadManager.STATUS_PENDING:
                        Log.v( TAG,"STATUS_PENDING");
                    case DownloadManager.STATUS_RUNNING:
                        //正在下载，不做任何事情
                        Log.v(TAG,"STATUS_RUNNING");
                        break;
                    case DownloadManager.STATUS_SUCCESSFUL:
                        //完成
                        Log.v( TAG,"下载完成");
                        Uri downloadFileUri = manager.getUriForDownloadedFile(downId);
                        install(context, downloadFileUri);
                        break;
                    case DownloadManager.STATUS_FAILED:
                        //清除已下载的内容，重新下载
                        Log.v(TAG, "STATUS_FAILED");
//                        downloadManager.remove(prefs.getLong(DL_ID, 0));
                        break;
                }
            }


        }
    }

    private void install(Context context, Uri uri) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(context!=null){
            context.startActivity(install);
        }
    }
}
