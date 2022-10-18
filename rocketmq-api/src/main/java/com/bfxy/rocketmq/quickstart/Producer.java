package com.bfxy.rocketmq.quickstart;

import com.bfxy.rocketmq.constants.Const;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

public class Producer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("test_quick_producer_name");
        producer.setNamesrvAddr(Const.NAMESRV_ADDR);
        producer.start();

        for (int i = 0; i < 5; i++) {
            // 创建消息
            Message message = new Message(
                    "test_quick_topic", // 主题
                    "TagA", // TAG
                    "key" + i, // KEY：用作唯一标识
                    ("Hello RocketMQ").getBytes() // 内容
            );
            // 发送消息
            producer.send(message);
        }

    }
}
