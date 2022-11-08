package com.bfxy.rocketmq.model;

import com.bfxy.rocketmq.constants.Const;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

public class Consumer1 {

    public Consumer1(){
        try {
            String group_name = "test_model_consumer_name";
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group_name);
            consumer.setNamesrvAddr(Const.NAMESRV_ADDR);
            consumer.subscribe("test_model_topic","*");
            // 集群模式，不知道消息会被投递到什么队列
            // 广播模式，同一个ConsumerGroup里的Consumer都消费订阅Topic全部信息
            consumer.setMessageModel(MessageModel.CLUSTERING); // 默认集群模式
            consumer.registerMessageListener(new Listener());
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Concurrently并发处理
    class Listener implements MessageListenerConcurrently{

        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
            try {
                for (MessageExt msg: msgs){
                    String topic = msg.getTopic();
                    String msgBody = new String(msg.getBody(), "utf-8");
                    String tags = msg.getTags();
                    System.out.println("收到消息："+" topic:"+topic+" ,tags:"+tags+" ,body:"+msgBody);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }

    public static void main(String[] args) {
        Consumer1 c1 = new Consumer1();
        System.out.println("c1 start..");
    }
}
