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

/**
 * ReturnCodeEnumeration.valueOf("C00" )获取枚举ReturnCodeEnumeration.C00
 * Created by nym on 2015/1/25.
 */
public enum ReturnCodeEnumeration {
    C00("交易成功"),
    C01("会员已存在"),
    C02("会员不存在"),
    C03("商户不存在"),
    C04("会员账号或密码不正确"),
    C05("卡已绑定"),
    C06("卡绑定数量已满"),
    C07("身份证证件号无效"),
    C08("短信发送失败"),
    C09("银联交互失败"),
    C10(""),
    C11("核心ARQC校验失败"),
    C12("核心卡号不存在"),
    C13("设备号不一致"),
    C14("核心原交易已冲正"),
    C15(""),
    C16(""),
    C17(""),
    C18(""),
    C19(""),
    C20("核心卡片余额大于账户余额"),
    C21("核心商户不存在"),
    C22("核心终端不存在"),
    C23("核心商户状态不正常"),
    C24("核心终端状态不正常"),
    C25("核心商户没有该交易权限"),
    C26("核心单日圈存次数超限"),
    C27("核心单日圈存金额超限"),
    C28("核心余额小于交易金额"),
    C29("核心超出电子现金上限"),
    C30(""),
    C31("核心数据库错误"),
    C32(""),
    C33(""),
    C34(""),
    C35(""),
    C36(""),
    C37(""),
    C38(""),
    C39(""),
    C40("核心密码错误"),
    C41("核心IC卡已挂失"),
    C42("核心IC卡已回收"),
    C43("核心IC卡已销卡"),
    C44("核心IC卡已换卡"),
    C45("核心IC卡已挂失补卡"),
    C46("核心IC卡已挂失销卡"),
    C47("核心IC卡已销卡结清"),
    C48("核心IC卡状态异常"),
    C49("核心密码已锁定"),
    C50("核心此卡已暂停充值服务"),
    C97("核心必填数据错误"),
    C98("数据错误"),
    C99("系统错误")

    ;

    private String value;
    ReturnCodeEnumeration(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}
