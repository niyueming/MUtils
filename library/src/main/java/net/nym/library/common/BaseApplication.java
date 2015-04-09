/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.common;

import android.app.Application;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.ConnectivityManager;

import net.nym.library.broadcastreceiver.NetBroadcastReceiver;


/**
 * 要在manifest文件的<application></application>里声明
 *
 * */
public class BaseApplication extends Application {
	public final static String TAG = "BaseApplication";
	private NetBroadcastReceiver mNetReceiver;
	private static BaseApplication instance;


	@Override
	public void onCreate() {
		super.onCreate();

		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mNetReceiver = new NetBroadcastReceiver(), filter);

		instance = this;

	}


	/**
	 * Called when the overall system is running low on memory
	 */
	@Override
	public void onLowMemory() {
		super.onLowMemory();

	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		unregisterReceiver(mNetReceiver);
	}

	/**
	 * @return the main context of the Application
	 */
	public static BaseApplication getAppContext() {
		return instance;
	}

	/**
	 * @return the main resources from the Application
	 */
	public static Resources getAppResources() {
		if (instance == null)
			return null;
		return instance.getResources();
	}

}
