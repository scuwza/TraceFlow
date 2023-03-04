package com.wza.TraceFlow.handler.receiver.rocketmq;

import com.alibaba.fastjson.JSON;
import com.wza.TraceFlow.common.domain.TaskInfo;
import com.wza.TraceFlow.handler.receiver.service.ConsumeService;
import com.wza.TraceFlow.support.constans.MessageQueuePipeline;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 *
 * @author elpsycongroo
 * create date: 2022/7/16
 */
@Component
@ConditionalOnProperty(name = "TraceFlow.mq.pipeline", havingValue = MessageQueuePipeline.ROCKET_MQ)
@RocketMQMessageListener(topic = "${TraceFlow.business.topic.name}",
        consumerGroup = "${TraceFlow.rocketmq.biz.consumer.group}",
        selectorType = SelectorType.TAG,
        selectorExpression = "${TraceFlow.business.tagId.value}"
)
public class RocketMqBizReceiver implements RocketMQListener<String> {

    @Autowired
    private ConsumeService consumeService;

    @Override
    public void onMessage(String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        List<TaskInfo> taskInfoLists = JSON.parseArray(message, TaskInfo.class);
        consumeService.consume2Send(taskInfoLists);
    }
}
