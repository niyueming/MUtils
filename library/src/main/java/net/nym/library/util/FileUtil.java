/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author nym
 * @date 2015/3/31 0031.
 * @since 1.0
 */
public class FileUtil {

    /**
     * 复制单个文件
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(InputStream in, String newPath) {
        File file = new File(newPath);
        if (file.getParentFile() != null)
        {
            if (!file.getParentFile().exists())
            {
                file.getParentFile().mkdirs();
            }
        }
        try {
            int bytesum = 0;
            int byteread = 0;
            FileOutputStream fs = new FileOutputStream(file);
            byte[] buffer = new byte[1444];
            int length;
            while ( (byteread = in.read(buffer)) != -1) {
                bytesum += byteread; //字节数 文件大小
                fs.write(buffer, 0, byteread);
            }
            in.close();
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 复制单个文件
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        File oldfile = new File(oldPath);
        if (oldfile.exists()) { //文件存在时
            try {
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                copyFile(inStream,newPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
