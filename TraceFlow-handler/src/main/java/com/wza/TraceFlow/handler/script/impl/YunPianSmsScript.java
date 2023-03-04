package com.wza.TraceFlow.handler.script.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.wza.TraceFlow.common.constant.CommonConstant;
import com.wza.TraceFlow.common.dto.account.sms.YunPianSmsAccount;
import com.wza.TraceFlow.common.enums.SmsStatus;
import com.wza.TraceFlow.handler.domain.sms.SmsParam;
import com.wza.TraceFlow.handler.domain.sms.YunPianSendResult;
import com.wza.TraceFlow.handler.script.SmsScript;
import com.wza.TraceFlow.support.domain.SmsRecord;
import com.wza.TraceFlow.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author 3y
 * @date 2022年5月23日
 * 发送短信接入文档：https://www.yunpian.com/official/document/sms/zh_CN/domestic_list
 */
@Slf4j
@Component("YunPianSmsScript")
public class YunPianSmsScript implements SmsScript {
    @Autowired
    private AccountUtils accountUtils;

    @Override
    public List<SmsRecord> send(SmsParam smsParam) {

        try {
            YunPianSmsAccount account = accountUtils.getSmsAccountByScriptName(smsParam.getScriptName(), YunPianSmsAccount.class);
            Map<String, Object> params = assembleParam(smsParam, account);

            String result = HttpRequest.post(account.getUrl())
                    .header(Header.CONTENT_TYPE.getValue(), CommonConstant.CONTENT_TYPE_FORM_URL_ENCODE)
                    .header(Header.ACCEPT.getValue(), CommonConstant.CONTENT_TYPE_JSON)
                    .form(params)
                    .timeout(2000)
                    .execute().body();
            YunPianSendResult yunPianSendResult = JSON.parseObject(result, YunPianSendResult.class);
            return assembleSmsRecord(smsParam, yunPianSendResult, account);
        } catch (Exception e) {
            log.error("YunPianSmsScript#send fail:{},params:{}", Throwables.getStackTraceAsString(e), JSON.toJSONString(smsParam));
            return null;
        }

    }

    @Override
    public List<SmsRecord> pull(String scriptName) {
        YunPianSmsAccount account = accountUtils.getSmsAccountByScriptName(scriptName, YunPianSmsAccount.class);
        // .....
        return null;
    }

    /**
     * 组装参数
     *
     * @param smsParam
     * @param account
     * @return
     */
    private Map<String, Object> assembleParam(SmsParam smsParam, YunPianSmsAccount account) {
        Map<String, Object> params = new HashMap<>(8);
        params.put("apikey", account.getApikey());
        params.put("mobile", StringUtils.join(smsParam.getPhones(), StrUtil.C_COMMA));
        params.put("tpl_id", account.getTplId());
        params.put("tpl_value", "");
        return params;
    }


    private List<SmsRecord> assembleSmsRecord(SmsParam smsParam, YunPianSendResult response, YunPianSmsAccount account) {
        if (Objects.isNull(response) || ArrayUtil.isEmpty(response.getData())) {
            return null;
        }

        List<SmsRecord> smsRecordList = new ArrayList<>();

        for (YunPianSendResult.DataDTO datum : response.getData()) {
            SmsRecord smsRecord = SmsRecord.builder()
                    .sendDate(Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)))
                    .messageTemplateId(smsParam.getMessageTemplateId())
                    .phone(Long.valueOf(datum.getMobile()))
                    .supplierId(account.getSupplierId())
                    .supplierName(account.getSupplierName())
                    .msgContent(smsParam.getContent())
                    .seriesId(datum.getSid())
                    .chargingNum(Math.toIntExact(datum.getCount()))
                    .status(CommonConstant.ZERO.equals(datum.getCode()) ? SmsStatus.SEND_SUCCESS.getCode() : SmsStatus.SEND_FAIL.getCode())
                    .reportContent(datum.getMsg())
                    .created(Math.toIntExact(DateUtil.currentSeconds()))
                    .updated(Math.toIntExact(DateUtil.currentSeconds()))
                    .build();

            smsRecordList.add(smsRecord);
        }

        return smsRecordList;
    }


}

