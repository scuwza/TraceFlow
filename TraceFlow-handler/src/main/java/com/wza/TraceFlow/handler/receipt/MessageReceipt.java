package com.wza.TraceFlow.handler.receipt;


import com.google.common.base.Throwables;
import com.wza.TraceFlow.handler.receipt.stater.ReceiptMessageStater;
import com.wza.TraceFlow.support.config.SupportThreadPoolConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 拉取回执信息 入口
 *
 * @author 3y
 */
@Component
@Slf4j
public class MessageReceipt {

    @Autowired
    private List<ReceiptMessageStater> receiptMessageStaterList;

    @PostConstruct
    private void init() {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (true) {
                try {
                    for (ReceiptMessageStater receiptMessageStater : receiptMessageStaterList) {
                        //receiptMessageStater.start();
                    }
                    Thread.sleep(2000);
                } catch (Exception e) {
                    log.error("MessageReceipt#init fail:{}", Throwables.getStackTraceAsString(e));
                }
            }
        });
    }
}
