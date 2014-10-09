/*
 * Copyright (c) 2014  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.javabean;

import net.nym.library.util.StringUtils;

/**
 * @author nym
 * @date 2014/10/9 0009.
 */
public class JSONTypeUtil {

    public enum JSON_TYPE{
        /**
         * JSONObject
         */
        JSON_TYPE_OBJECT,
        /**
         * JSONArray
         */
        JSON_TYPE_ARRAY,
        /**
         * 非JSON
         */
        JSON_TYPE_ERROR
    }

    /**
     * @param str String
     * @return {@link JSON_TYPE}
     */
    public static JSON_TYPE getJSONType(String str)
    {
        if (StringUtils.isNullOrEmpty(str))
        {
            return JSON_TYPE.JSON_TYPE_ERROR;
        }
        if (str.startsWith("{"))
        {
            return JSON_TYPE.JSON_TYPE_OBJECT;
        }
        if (str.startsWith("["))
        {
            return JSON_TYPE.JSON_TYPE_ARRAY;
        }

        return JSON_TYPE.JSON_TYPE_ERROR;
    }
}
