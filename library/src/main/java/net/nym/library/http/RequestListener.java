/*
 * Copyright (c) 2014  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.http;

/**
 * @author nym
 * @date 2014/10/9 0009.
 * @since 1.0
 */
public interface RequestListener<T> {

    public static final String ERROR_WITHOUT_NETWORK = "无网络";
    public static final String ERROR_JSON_PARSE = "返回错误（不是json）";
    public static final String ERROR_TIMEOUT = "连接超时";
    public static final String ERROR_IO = "当前网络不可用，请稍后重试";
    public static final String ERROR_OTHER = "其他错误";
    public static final int OTHER_ERROR = 0;
    public static final int IO_ERROR = 1;
    public static final int TIMEOUT_ERROR = 2;
    public static final int WITHOUT_NETWORK_ERROR = 3;
    public static final int JSON_ERROR = 4;


	void onResponse(T object);

    /**
     * @param errorCode 错误码
     * @param message   错误信息
     */
    void onError(int errorCode, String message);

    void onProgressUpdate(int totals, int progress);
}
