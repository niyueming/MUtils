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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;


/**
 * 类 <code>NetBroadcastReceiver</code>
 * 监听网络状态变化
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 *
 * IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
 *
 * @author nym
 * @version 2013-6-4
 * @since 
 * @time 上午9:23:32
 * @see 
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

	private final String TAG = NetBroadcastReceiver.class.getSimpleName();
    private ArrayList<OnConnectivityChangeListener> mLiseners = ConnectivityUtils.getOnConnectivityChangeListeners();
	@Override
	public void onReceive(Context arg0, Intent arg1) {
//		Log.i(TAG + ":%s", "网络状态改变");

		boolean success = false;
		boolean wifiState = false;
		boolean mobileState = false;
		String mobileNetName = "";	//网络名称

		// 获得网络连接服务
		ConnectivityManager connManager = (ConnectivityManager) arg0
				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		connManager.setNetworkPreference(ConnectivityManager.TYPE_WIFI);	//优先使用wifi
		NetworkInfo  wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(wifiInfo != null)
		{
			if(wifiInfo.isConnected())
			{
				wifiState = true;
			}
		}
		
		NetworkInfo  mobileInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        int networkType = 0;
		if(mobileInfo != null)
		{

			if(mobileInfo.isConnected())
			{
				mobileState = true;
			}
            networkType = mobileInfo.getSubtype();
            switch (mobileInfo.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    mobileNetName = "联通3G";
                    break;
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    mobileNetName = "电信3G";
                    break;
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    mobileNetName = "移动或联通2G";
                    break;
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    mobileNetName = "电信2G";
                    break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    mobileNetName = "电信3G";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:
                    mobileNetName = "LTE";
                    break;
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    mobileNetName = "UNKNOWN";
                    break;
                default:
                    break;
			}

//            switch (networkType) {
//                case TelephonyManager.NETWORK_TYPE_GPRS:
//                case TelephonyManager.NETWORK_TYPE_EDGE:
//                case TelephonyManager.NETWORK_TYPE_CDMA:
//                case TelephonyManager.NETWORK_TYPE_1xRTT:
//                case TelephonyManager.NETWORK_TYPE_IDEN:
//                    mobileNetName = "2G";
//                    break;//NETWORK_CLASS_2_G;
//                case TelephonyManager.NETWORK_TYPE_UMTS:
//                case TelephonyManager.NETWORK_TYPE_EVDO_0:
//                case TelephonyManager.NETWORK_TYPE_EVDO_A:
//                case TelephonyManager.NETWORK_TYPE_HSDPA:
//                case TelephonyManager.NETWORK_TYPE_HSUPA:
//                case TelephonyManager.NETWORK_TYPE_HSPA:
//                case TelephonyManager.NETWORK_TYPE_EVDO_B:
//                case TelephonyManager.NETWORK_TYPE_EHRPD:
//                case TelephonyManager.NETWORK_TYPE_HSPAP:
//                    mobileNetName = "3G";
//                    break;//NETWORK_CLASS_3_G;
//                case TelephonyManager.NETWORK_TYPE_LTE:
//                    mobileNetName = "4G";
//                    break;//NETWORK_CLASS_4_G;
//                default:
//                    mobileNetName = "UNKNOWN";
//                    break;//NETWORK_CLASS_UNKNOWN;
//            }
		}
		
		if (wifiState | mobileState) { // 判断是否正在使用网络
			success = true;
		}
		Log.i(TAG ,String.format("%s:%b,%s状态:%s","wifi状态" , wifiState, mobileNetName, mobileState + ""));
//		OperateSharePreferences.getInstance().saveNetState(success);
        for (int i = 0 ;i < mLiseners.size();i++)
        {
            if (mLiseners.get(i) == null) {
                mLiseners.remove(i);
                i--;
            }
            else {
                mLiseners.get(i).onChange(success, wifiState, networkType, mobileNetName);
            }
        }
	}
    public static interface OnConnectivityChangeListener{
        void onChange(boolean hasNet, boolean isWifi, int networkType, String networkTypeName);
    }
}
