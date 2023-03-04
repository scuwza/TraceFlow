package com.wza.TraceFlow.handler.receiver.eventbus;

import com.google.common.eventbus.Subscribe;
import com.wza.TraceFlow.common.domain.TaskInfo;
import com.wza.TraceFlow.handler.receiver.service.ConsumeService;
import com.wza.TraceFlow.support.constans.MessageQueuePipeline;
import com.wza.TraceFlow.support.domain.MessageTemplate;
import com.wza.TraceFlow.support.mq.eventbus.EventBusListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 3y
 */
@Component
@ConditionalOnProperty(name = "TraceFlow.mq.pipeline", havingValue = MessageQueuePipeline.EVENT_BUS)
public class EventBusReceiver implements EventBusListener {

    @Autowired
    private ConsumeService consumeService;

    @Override
    @Subscribe
    public void consume(List<TaskInfo> lists) {
        consumeService.consume2Send(lists);

    }

    @Override
    @Subscribe
    public void recall(MessageTemplate messageTemplate) {
        consumeService.consume2recall(messageTemplate);
    }
}
