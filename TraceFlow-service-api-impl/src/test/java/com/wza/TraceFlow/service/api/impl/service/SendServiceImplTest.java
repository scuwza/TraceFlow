package com.wza.TraceFlow.service.api.impl.service;

import com.wza.TraceFlow.common.enums.RespStatusEnum;
import com.wza.TraceFlow.common.vo.BasicResultVO;
import com.wza.TraceFlow.service.api.domain.MessageParam;
import com.wza.TraceFlow.service.api.domain.SendRequest;
import com.wza.TraceFlow.service.api.domain.SendResponse;
import com.wza.TraceFlow.service.api.enums.BusinessCode;
import com.wza.TraceFlow.service.api.impl.domain.SendTaskModel;
import com.wza.TraceFlow.support.pipeline.BusinessProcess;
import com.wza.TraceFlow.support.pipeline.ProcessContext;
import com.wza.TraceFlow.support.pipeline.ProcessController;
import com.wza.TraceFlow.support.pipeline.ProcessTemplate;
import com.wza.TraceFlow.support.utils.ContentHolderUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class SendServiceImplTest {

    @Spy
    private ProcessController processController;

    @Mock
    private Map<String, ProcessTemplate> templateConfig;

    @Spy
    private ProcessTemplate processTemplate;

    @Mock
    private BusinessProcess businessProcess;

    @InjectMocks
    private SendServiceImpl sendServiceImplUnderTest;

    @Test
    void testAssembleAction() {
        String originValue = "{$title}";
        Map<String,String> variables = new HashMap<>();
        variables.put("title","haha");
        String resultValue = ContentHolderUtil.replacePlaceHolder(originValue, variables);
        System.out.println(resultValue);
    }

    @Test
    void testSend() {

        // params
        final SendRequest sendRequest = new SendRequest("send", 1L,
                new MessageParam("13711111111", new HashMap<>(), new HashMap<>()));

        // predict result
        final ProcessContext<SendTaskModel> processContext = new ProcessContext<>(sendRequest.getCode(), new SendTaskModel(), false, new BasicResultVO<>(
                RespStatusEnum.SUCCESS, "data"));
        final SendResponse expectedResult = new SendResponse(processContext.getResponse().getStatus(), processContext.getResponse().getMsg());


        // stub
        Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);
        processTemplate.setProcessList(Arrays.asList(businessProcess));
        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), processTemplate);

        processController.setTemplateConfig(templateConfig);


        // Run the test
        final SendResponse result = sendServiceImplUnderTest.send(sendRequest);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testBatchSend() {
//        // Setup
//        final BatchSendRequest batchSendRequest = new BatchSendRequest("code", 0L,
//                Arrays.asList(new MessageParam("receiver", new HashMap<>(), new HashMap<>())));
//        final SendResponse expectedResult = new SendResponse("status", "msg");
//
//        // Configure ProcessController.process(...).
//        final ProcessContext processContext = new ProcessContext<>("code", null, false, new BasicResultVO<>(
//                RespStatusEnum.SUCCESS, "data"));
//        when(processController.process(new ProcessContext<>("code", null, false, new BasicResultVO<>(
//                RespStatusEnum.SUCCESS, "data")))).thenReturn(processContext);
//
//        // Run the test
//        final SendResponse result = sendServiceImplUnderTest.batchSend(batchSendRequest);
//
//        // Verify the results
//        assertEquals(expectedResult, result);
    }
}
