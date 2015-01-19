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

import net.nym.library.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.EOFException;
import java.net.SocketTimeoutException;
import java.util.Map;


public class WebServiceVisitor  {
    private static final int TIME_OUT = 30000;
    private static final String NAME_SPACE = "http://webservice.momoService.mbsService.com";
    //公网
    private static final String END_POINT = "http://118.144.133.90/momo/services/momoMobileService?wsdl";
    private static final String SOAP_ACTION_HOST = "http://118.144.133.90/";
    //欧弟的机子
//    private static final String END_POINT = "http://192.168.18.10:8686/momo/services/momoMobileService?wsdl";
//    private static final String SOAP_ACTION_HOST = "http://192.168.18.10:8686/";


	public static String callWebService(String methodName,Map<String,Object> propertyMap) {

		return callWebService(methodName,propertyMap,TIME_OUT);
	}

    /**
     * @param methodName
     * @param propertyMap
     * @param timeout
     *
    * */
	public static String callWebService(String methodName,Map<String,Object> propertyMap,int timeout) {
		// 命名空间
		String nameSpace = NAME_SPACE;
		// 调用的方法名称

		// EndPoint
		String endPoint = END_POINT;
//		String endPoint = "http://192.168.107.200:8080/ams/services/aphMobileService?wsdl";
		// SOAP Action
		String soapAction = SOAP_ACTION_HOST + methodName;
//		String soapAction = "http://192.168.107.200:8080/" + methodName;
		// ָ指定WebService的命名空间和调用的方法名
		SoapObject rpc = new SoapObject(nameSpace, methodName);
		// 设置需调用WebService接口需要传入的两个参数mobileCode、userId
		for(String key : propertyMap.keySet()){
			rpc.addProperty(key, propertyMap.get(key));
		}
		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);

		envelope.bodyOut = rpc;
		// 设置是否调用的是dotNet开发的WebService
		envelope.dotNet = false;
		// 等价于envelope.bodyOut = rpc;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE transport = new HttpTransportSE(endPoint,timeout);
		try {
            // 调用WebService
            transport.call(soapAction, envelope);

        }catch (SocketTimeoutException e){
            e.printStackTrace();
            return null;
        }catch (EOFException e){
            e.printStackTrace();
            try {
                transport.call(soapAction, envelope);
            } catch (Exception e1) {
                e1.printStackTrace();
                return null;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        SoapObject object = null;
        try{
            // 获取返回的数据
            Log.i("out:%s", envelope.bodyOut.toString());
            Log.i("in:%s", envelope.bodyIn.toString());
            object = (SoapObject) envelope.bodyIn;
        }catch(Exception ex){
            return null;
        }
        // 获取返回的结果
        String result = object==null? null : object.getProperty(0).toString();
		return result;
	}
}
