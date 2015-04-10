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

import java.util.Vector;

/**
 *
 */
public class HttpResponse {
 
	String urlString;
 
	int defaultPort;
 
	String file;
 
	String host;
 
	String path;
 
	int port;
 
	String protocol;
 
	String query;
 
	String ref;
 
	String userInfo;
 
	String contentEncoding;
 
	String content;
 
	String contentType;
 
	int code;
 
	String message;
 
	String method;
 
	int connectTimeout;
 
	int readTimeout;
 
	Vector<String> contentCollection;
	
	String sessionId;
 
	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setDefaultPort(int defaultPort) {
		this.defaultPort = defaultPort;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public String getContent() {
		return content;
	}
 
	public String getContentType() {
		return contentType;
	}
 
	public int getCode() {
		return code;
	}
 
	public String getMessage() {
		return message;
	}
 
	public Vector<String> getContentCollection() {
		return contentCollection;
	}
 
	public String getContentEncoding() {
		return contentEncoding;
	}
 
	public String getMethod() {
		return method;
	}
 
	public int getConnectTimeout() {
		return connectTimeout;
	}
 
	public int getReadTimeout() {
		return readTimeout;
	}
 
	public String getUrlString() {
		return urlString;
	}
 
	public int getDefaultPort() {
		return defaultPort;
	}
 
	public String getFile() {
		return file;
	}
 
	public String getHost() {
		return host;
	}
 
	public String getPath() {
		return path;
	}
 
	public int getPort() {
		return port;
	}
 
	public String getProtocol() {
		return protocol;
	}
 
	public String getQuery() {
		return query;
	}
 
	public String getRef() {
		return ref;
	}
 
	public String getUserInfo() {
		return userInfo;
	}

	public void setContentCollection(Vector<String> contentCollection) {
		this.contentCollection = contentCollection;
	}

	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
 
}
