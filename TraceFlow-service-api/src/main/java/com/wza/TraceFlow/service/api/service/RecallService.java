package com.wza.TraceFlow.service.api.service;

import com.wza.TraceFlow.service.api.domain.SendRequest;
import com.wza.TraceFlow.service.api.domain.SendResponse;

/**
 * 撤回接口
 *
 * @author 3y
 */
public interface RecallService {


    /**
     * 根据模板ID撤回消息
     *
     * @param sendRequest
     * @return
     */
    SendResponse recall(SendRequest sendRequest);
}
