package com.wza.TraceFlow.handler.receiver.rocketmq;

import com.alibaba.fastjson.JSON;
import com.wza.TraceFlow.handler.receiver.service.ConsumeService;
import com.wza.TraceFlow.support.constans.MessageQueuePipeline;
import com.wza.TraceFlow.support.domain.MessageTemplate;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author elpsycongroo
 * create date: 2022/7/16
 */
@Component
@ConditionalOnProperty(name = "TraceFlow.mq.pipeline", havingValue = MessageQueuePipeline.ROCKET_MQ)
@RocketMQMessageListener(topic = "${TraceFlow.business.recall.topic.name}",
        consumerGroup = "${TraceFlow.rocketmq.recall.consumer.group}",
        selectorType = SelectorType.TAG,
        selectorExpression = "${TraceFlow.business.tagId.value}"
)
public class RocketMqRecallReceiver implements RocketMQListener<String> {

    @Autowired
    private ConsumeService consumeService;

    @Override
    public void onMessage(String message) {
        if (StringUtils.isBlank(message)) {
            return;
        }
        MessageTemplate messageTemplate = JSON.parseObject(message, MessageTemplate.class);
        consumeService.consume2recall(messageTemplate);
    }
}
