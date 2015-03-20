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

import net.nym.library.util.Log;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 3DES加密工具类 
 */
public class Des3 {
    // 密钥  密钥长度大于等于24,截取前24位;否则前补0
//    private final static String secretKey = "000000000000000000001111";
    private String secretKey;
    // 向量  
    private final static String iv = "12345678";
    // 加解密统一使用的编码方式  
    private final static String encoding = "utf-8";

    public Des3(String key)
    {
        this(key,24);
    }
    public Des3(String key,int num)
    {
        if (key == null)
        {
            throw new NullPointerException("key cannot be null");
        }
        if (key.length() >= num)
        {
            secretKey = key.substring(0,num);
        }
        else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0 ; i< num - key.length();i ++)
            {
                sb.append("0");
            }
            secretKey = sb.toString() + key;
        }
        Log.println(secretKey);
    }

    public String getKey()
    {
        return secretKey;
    }

    /**
     * 3DES加密 
     *
     * @param plainText 普通文本 
     * @return
     * @throws Exception
     */
    public String encode(String plainText) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);

        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
        return Base64.encode(encryptData);
    }

    /**
     * 3DES解密 
     *
     * @param encryptText 加密文本 
     * @return
     * @throws Exception
     */
    public String decode(String encryptText) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);

        byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));

        return new String(decryptData, encoding);
    }

    public static void main(String[] args) throws Exception {
        String str = "5004|188100704468|1234567890|1111567890|wangwei|";
        Des3 des = new Des3("000000000000000000001111");
        String enstr = des.encode(str);
        System.out.println("加密后：" + enstr);
        String destr = des.decode(enstr);
        System.out.println("解密后：" + destr);
    }
}  