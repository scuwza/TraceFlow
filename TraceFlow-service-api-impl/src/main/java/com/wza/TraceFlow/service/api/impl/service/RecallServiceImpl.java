package com.wza.TraceFlow.service.api.impl.service;

import com.wza.TraceFlow.common.vo.BasicResultVO;
import com.wza.TraceFlow.service.api.domain.SendRequest;
import com.wza.TraceFlow.service.api.domain.SendResponse;
import com.wza.TraceFlow.service.api.impl.domain.SendTaskModel;
import com.wza.TraceFlow.service.api.service.RecallService;
import com.wza.TraceFlow.support.pipeline.ProcessContext;
import com.wza.TraceFlow.support.pipeline.ProcessController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 撤回接口
 *
 * @author 3y
 */
@Service
public class RecallServiceImpl implements RecallService {

    @Autowired
    private ProcessController processController;

    @Override
    public SendResponse recall(SendRequest sendRequest) {
        SendTaskModel sendTaskModel = SendTaskModel.builder()
                .messageTemplateId(sendRequest.getMessageTemplateId())
                .build();
        ProcessContext context = ProcessContext.builder()
                .code(sendRequest.getCode())
                .processModel(sendTaskModel)
                .needBreak(false)
                .response(BasicResultVO.success()).build();
        ProcessContext process = processController.process(context);
        return new SendResponse(process.getResponse().getStatus(), process.getResponse().getMsg());
    }
}
