/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

//package net.nym.library.service;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.location.LocationClientOption.LocationMode;
//
//import net.nym.library.util.Log;
//
//import cn.com.firsecare.aph.common.BaseApplication;
//import cn.com.firsecare.aph.common.OperateSharePreferences;
//
//public class LocationService extends Service {
//	public static final int SPACE_TIME = 1000 * 60;
//
//	// 定位
//	static LocationClient mLocationClient;
//	static MyLocationListener mLocationListener;
//
//	boolean flag;
//
//	static {
//		location();
//	}
//
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		Log.i("service run again");
//		if (mLocationClient != null) {
//			if (mLocationClient.isStarted()) {
//				mLocationClient.requestLocation();
//			} else {
//				mLocationClient.start();
//				mLocationClient.requestLocation();
//			}
//		} else {
//			location();
//		}
////		flags = START_STICKY;
//		return flags;
//		// return super.onStartCommand(intent, flags, startId);
//	}
//
//	private static void location() {
//		// mLocationData = new LocationData();
//		// MyApplication.getAppContext().initEngineManager(getApplicationContext());
//		mLocationClient = new LocationClient(BaseApplication.getAppContext());
//		mLocationListener = new MyLocationListener();
//		mLocationClient.registerLocationListener(mLocationListener);
//		LocationClientOption option = new LocationClientOption();
//		option.setOpenGps(true);
//		option.setScanSpan(LocationService.SPACE_TIME);// 设置发起定位请求的间隔时间为ms
//		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
//		option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
//		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
//		option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的方向
//		mLocationClient.setLocOption(option);
//		mLocationClient.start();
//		mLocationClient.requestLocation();
//	}
//
//	private static class MyLocationListener implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation arg0) {
//			if (arg0 == null) {
//				// Util.toaster(MyApplication.getAppContext(),
//				// "BDLocation = null");
//				return;
//			}
//
//            String province = arg0.getProvince();
//			String city = arg0.getCity();
//			String district = arg0.getDistrict();
//			Log.d("mLatitude = %s,mLongitude=%s", "" + arg0.getLatitude(), ""
//					+ arg0.getLongitude());
//			OperateSharePreferences.getInstance().setUserLocationLatitude(
//					arg0.getLatitude() + "");
//			OperateSharePreferences.getInstance().setUserLocationLongitude(
//					arg0.getLongitude() + "");
//			if (province != null) {
//				OperateSharePreferences.getInstance().setUserLocationProvince(province);
//			}
//			if (city != null) {
//				OperateSharePreferences.getInstance().setUserLocationCity(city);
//			}
//			if (district != null) {
//				OperateSharePreferences.getInstance().setUserLocationDistrict(
//						district);
//			}
//			OperateSharePreferences.getInstance().setUserLocationAddress(arg0.getAddrStr() + "");
//            if (mLocationClient != null) {
//                mLocationClient.stop();
//                mLocationClient.unRegisterLocationListener(mLocationListener);
//                mLocationClient = null;
//            }
//
//		}
//
//
//	}
//
//	@Override
//	public void onDestroy() {
//		flag = true;
//		if (mLocationClient != null) {
//			mLocationClient.stop();
//			mLocationClient.unRegisterLocationListener(mLocationListener);
//			mLocationClient = null;
//		}
//		super.onDestroy();
//	}
//
//	@Override
//	public IBinder onBind(Intent intent) {
//		return null;
//	}
//}
