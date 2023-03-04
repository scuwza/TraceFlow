package com.wza.TraceFlow.handler.deduplication.build;

import com.wza.TraceFlow.common.domain.TaskInfo;
import com.wza.TraceFlow.common.enums.AnchorState;
import com.wza.TraceFlow.common.enums.DeduplicationType;
import com.wza.TraceFlow.handler.deduplication.DeduplicationParam;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * @author huskey
 * @date 2022/1/18
 */
@Service
public class ContentDeduplicationBuilder extends AbstractDeduplicationBuilder implements Builder {

    public ContentDeduplicationBuilder() {
        deduplicationType = DeduplicationType.CONTENT.getCode();
    }

    @Override
    public DeduplicationParam build(String deduplication, TaskInfo taskInfo) {
        // deduplication = {"deduplication_10":{"num":1,"time":300},"deduplication_20":{"num":5}}
        DeduplicationParam deduplicationParam = getParamsFromConfig(deduplicationType, deduplication, taskInfo);
        if (Objects.isNull(deduplicationParam)) {
            return null;
        }
        deduplicationParam.setAnchorState(AnchorState.CONTENT_DEDUPLICATION);
        return deduplicationParam;

    }
}
