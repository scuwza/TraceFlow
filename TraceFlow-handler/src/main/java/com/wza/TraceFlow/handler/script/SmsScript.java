package com.wza.TraceFlow.handler.script;


import com.wza.TraceFlow.handler.domain.sms.SmsParam;
import com.wza.TraceFlow.support.domain.SmsRecord;

import java.util.List;


/**
 * 短信脚本 接口
 *
 * @author 3y
 */
public interface SmsScript {

    /**
     * 发送短信
     *
     * @param smsParam
     * @return 渠道商发送接口返回值
     */
    List<SmsRecord> send(SmsParam smsParam);


    /**
     * 拉取回执
     *
     * @param scriptName 标识账号的脚本名
     * @return 渠道商回执接口返回值
     */
    List<SmsRecord> pull(String scriptName);

}
