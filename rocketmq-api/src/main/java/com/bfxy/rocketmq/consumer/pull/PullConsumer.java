package com.bfxy.rocketmq.consumer.pull;

import com.bfxy.rocketmq.constants.Const;
import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PullConsumer {
    // key为指定的队列，value为这个队列拉取数据的最后位置
    // offset需要持久化存储，实例从新运行，offsetTable会清空
    public static final Map<MessageQueue, Long> offsetTable = new HashMap<MessageQueue, Long>();

    public static void main(String[] args) throws MQClientException {
        String group_name = "test_pull_consumer_name";
        DefaultMQPullConsumer consumer = new DefaultMQPullConsumer(group_name);
        consumer.setNamesrvAddr(Const.NAMESRV_ADDR);
        consumer.start();
        System.out.println("consumer start");

        // 从test_pull_topic这个主题去获取所有的队列（默认会有4个队列）
        Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("test_pull_topic");
        // 遍历每个队列，进行拉取数据
        for(MessageQueue mq : mqs){
            System.out.println("Consume from the queue:"+mq);

            SINGLE_MQ: while (true){
                try {
                    // 从queue中获取数据，从什么位置开始拉取数据 单次最多拉取32条记录
                    PullResult pullResult = consumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 32);
                    System.out.println(pullResult);
                    System.out.println(pullResult.getPullStatus());
                    System.out.println();

                    putMessageQueueOffset(mq, pullResult.getNextBeginOffset());
                    switch (pullResult.getPullStatus()){
                        case FOUND:
                            List<MessageExt> list = pullResult.getMsgFoundList();
                            for(MessageExt msg : list){
                                System.out.println(new String(msg.getBody()));
                            }
                            break;
                        case NO_MATCHED_MSG:
                            break SINGLE_MQ;
                        case NO_NEW_MSG:
                            System.out.println("没有新的数据啦...");
                            break SINGLE_MQ;
                        case OFFSET_ILLEGAL:
                            break SINGLE_MQ;
                        default:
                            break SINGLE_MQ;
                    }
                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        consumer.shutdown();
    }

    private static void putMessageQueueOffset(MessageQueue mq,long offset){
        offsetTable.put(mq, offset);
    }

    private static long getMessageQueueOffset(MessageQueue mq){
        Long offset = offsetTable.get(mq);
        if(offset != null)
            return offset;

        return 0L;
    }
}
