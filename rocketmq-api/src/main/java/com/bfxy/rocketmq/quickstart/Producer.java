package com.bfxy.rocketmq.quickstart;

import com.bfxy.rocketmq.constants.Const;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class Producer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("test_quick_producer_name");
        producer.setNamesrvAddr(Const.NAMESRV_ADDR);
        producer.start();

        // 视频自动创建，如果报错，尝试先手动创建topic
        for (int i = 0; i < 5; i++) {
            // 创建消息
            Message message = new Message(
                    "test_quick_topic", // 主题
                    "TagA", // TAG
                    "key" + i, // KEY：用作唯一标识
                    ("Hello RocketMQ" + i).getBytes() // 内容:字节类型
            );

            // 设置延迟发送
            if(i == 1){
                message.setDelayTimeLevel(3);
            }

            // 2.1 同步发送消息
            SendResult send = producer.send(message);
            SendStatus status = send.getSendStatus();// 失败，需要做补偿
            System.out.println("消息发出" + send);

            // 2.2 异步发送消息
//            producer.send(message, new SendCallback() {
//                @Override
//                public void onSuccess(SendResult sendResult) {
//                    System.out.println("msgId:" + sendResult.getMsgId() + ",status:"+sendResult.getSendStatus());
//                }
//
//                @Override
//                public void onException(Throwable throwable) {
//                    System.out.println("发送失败");
//                }
//            });
        }

        producer.shutdown();
    }
}
