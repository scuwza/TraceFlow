package com.wza.TraceFlow.handler.deduplication.service;


import com.wza.TraceFlow.handler.deduplication.DeduplicationParam;

/**
 * @author huskey
 * @date 2022/1/18
 */
public interface DeduplicationService {

    /**
     * 去重
     *
     * @param param
     */
    void deduplication(DeduplicationParam param);
}
