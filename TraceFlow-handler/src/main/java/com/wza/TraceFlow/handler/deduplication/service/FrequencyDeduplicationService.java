package com.wza.TraceFlow.handler.deduplication.service;

import cn.hutool.core.util.StrUtil;
import com.wza.TraceFlow.common.domain.TaskInfo;
import com.wza.TraceFlow.common.enums.DeduplicationType;
import com.wza.TraceFlow.handler.deduplication.limit.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * @author 3y
 * @date 2021/12/12
 * 频次去重服务
 */
@Service
public class FrequencyDeduplicationService extends AbstractDeduplicationService {


    @Autowired
    public FrequencyDeduplicationService(@Qualifier("SimpleLimitService") LimitService limitService) {

        this.limitService = limitService;
        deduplicationType = DeduplicationType.FREQUENCY.getCode();

    }

    private static final String PREFIX = "FRE";

    /**
     * 业务规则去重 构建key
     * <p>
     * key ： receiver + templateId + sendChannel
     * <p>
     * 一天内一个用户只能收到某个渠道的消息 N 次
     *
     * @param taskInfo
     * @param receiver
     * @return
     */
    @Override
    public String deduplicationSingleKey(TaskInfo taskInfo, String receiver) {
        return PREFIX + StrUtil.C_UNDERLINE
                + receiver + StrUtil.C_UNDERLINE
                + taskInfo.getMessageTemplateId() + StrUtil.C_UNDERLINE
                + taskInfo.getSendChannel();
    }
}
