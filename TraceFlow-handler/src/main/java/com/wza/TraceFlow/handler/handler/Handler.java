package com.wza.TraceFlow.handler.handler;

import com.wza.TraceFlow.common.domain.TaskInfo;
import com.wza.TraceFlow.support.domain.MessageTemplate;

/**
 * @author 3y
 * 消息处理器
 */
public interface Handler {

    /**
     * 处理器
     *
     * @param taskInfo
     */
    void doHandler(TaskInfo taskInfo);

    /**
     * 撤回消息
     *
     * @param messageTemplate
     * @return
     */
    void recall(MessageTemplate messageTemplate);

}
