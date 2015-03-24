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

import java.util.Random;

/**
 * @author nym
 * @date 2015/3/24 0024.
 * @since 1.0
 */
public class NumberUtil {


    //生成随机的0-9 10个数字，且值各不相同
    private static int[] getRandomNum() {
        Random random = new Random();
        int[] data = new int[10];
        boolean b;
        boolean b2 = false;
        boolean b3 = true;
        int x;
        for (int i = 0; i < 10; i++) {
            b = true;
            while (b) {
                x = random.nextInt(10);
                if (x == 0 && b3) {
                    b3 = false;
                    b = false;
                }
                for (int y : data) {
                    if (x == y) {
                        b2 = false;
                        break;
                    } else {
                        b2 = true;
                    }
                }
                if (b2) {
                    data[i] = x;
                    b = false;
                    break;
                }
            }

        }
        return data;
    }
}
