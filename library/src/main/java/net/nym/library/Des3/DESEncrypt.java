/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.Des3;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class DESEncrypt {

    /**
     * 加解密算法
     *
     * @param data
     *            加解密数据
     * @param key
     *            秘钥
     * @param mode
     *            模式
     * @return 加解密结果
     */
    public static String desCryt(String data, String key, int mode) {
        byte[] result = null;
        try {
            SecureRandom sr = new SecureRandom();
            SecretKeyFactory keyFactory;
            DESKeySpec dks = new DESKeySpec(hexString2Bytes(key));
            keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretkey = keyFactory.generateSecret(dks);
            // 创建Cipher对象
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            // 初始化Cipher对象
            cipher.init(mode, secretkey, sr);
            // 加解密
            result = cipher.doFinal(hexString2Bytes(data));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes2HexString(result);
    }

    /**
     * byte数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    /**
     * 16进制字符串转成byte数组
     *
     * @param src
     * @return
     */
    public static byte[] hexString2Bytes(String src) {
        byte[] ret = new byte[16];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < 16; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static void main(String[] args) {
        // 加解密模式
        int mode = Cipher.DECRYPT_MODE;
        // 被加解密byte数组16进制字符串
        String data = "94DC61B1DA9C90CEF78975F2588F177309DF2FCF";
        // 秘钥byte数组16进制字符串
        String key = "C773E326BAF48F3B267A68AD1A76894C";
        String result = desCryt(data, key, mode);
        // 打印结果
        System.out.println("结果：" + result);
    }
}