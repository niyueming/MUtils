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

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * key要16或24字节
 * @author nym
 * @date 2015/2/11 0011.
 * @since 1.0
 */
public class Desede {

    private static final String DESede_ALG = "DESede/ECB/NOPADDING"; // 定义
    // 加密算法,可用
    // DES,DESede,Blowfish


    /**
     * 3DES加密
     *
     * @param src
     * @param key
     * @return
     */
    public static byte[] encryptDESede(byte[] src, byte[] key) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //需要bcprov-ext-jdk15on-151.jar包
        try {
            Cipher c1 = Cipher.getInstance(DESede_ALG,"BC");
            c1.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, DESede_ALG));
            return c1.doFinal(src);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    public static String encryptDESede(String src, String key) {
        byte[] result = encryptDESede(hexString2Bytes(src),hexString2Bytes(key));
        if (result != null)
        {
            return bytes2HexString(result);
        }
        return null;
    }

    /**
     * 3DES解密
     *
     * @param src
     * @param key
     * @return
     */
    public static byte[] decryptDESede(byte[] src, byte[] key) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            Cipher c1 = Cipher.getInstance(DESede_ALG,"BC");
            c1.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, DESede_ALG));
            return c1.doFinal(src);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    public static String decryptDESede(String src, String key) {
        byte[] result = decryptDESede(hexString2Bytes(src),hexString2Bytes(key));
        if (result != null)
        {
            return bytes2HexString(result);
        }
        return null;
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
    private static byte[] hexString2Bytes(String src) {
        byte[] ret = new byte[src.length()/2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < src.length()/2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }
}
