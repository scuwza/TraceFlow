package com.wza.TraceFlow.stream;

import com.wza.TraceFlow.common.domain.AnchorInfo;
import com.wza.TraceFlow.stream.constants.TraceFlowFlinkConstant;
import com.wza.TraceFlow.stream.function.TraceFlowFlatMapFunction;
import com.wza.TraceFlow.stream.sink.TraceFlowSink;
import com.wza.TraceFlow.stream.utils.MessageQueueUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * flink启动类
 *
 * @author 3y
 */
@Slf4j
public class TraceBootStrap {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        /**
         * 1.获取KafkaConsumer
         */
        KafkaSource<String> kafkaConsumer = MessageQueueUtils.getKafkaConsumer(TraceFlowFlinkConstant.TOPIC_NAME, TraceFlowFlinkConstant.GROUP_ID, TraceFlowFlinkConstant.BROKER);
        DataStreamSource<String> kafkaSource = env.fromSource(kafkaConsumer, WatermarkStrategy.noWatermarks(), TraceFlowFlinkConstant.SOURCE_NAME);


        /**
         * 2. 数据转换处理
         */
        SingleOutputStreamOperator<AnchorInfo> dataStream = kafkaSource.flatMap(new TraceFlowFlatMapFunction()).name(TraceFlowFlinkConstant.FUNCTION_NAME);

        /**
         * 3. 将实时数据多维度写入Redis(已实现)，离线数据写入hive(未实现)
         */
        dataStream.addSink(new TraceFlowSink()).name(TraceFlowFlinkConstant.SINK_NAME);
        env.execute(TraceFlowFlinkConstant.JOB_NAME);

    }

}
