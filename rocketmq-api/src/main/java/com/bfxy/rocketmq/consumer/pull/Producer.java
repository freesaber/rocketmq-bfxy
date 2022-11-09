package com.bfxy.rocketmq.consumer.pull;

import com.bfxy.rocketmq.constants.Const;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class Producer {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        String group_name = "test_pull_producer_name";
        DefaultMQProducer producer = new DefaultMQProducer(group_name);
        producer.setNamesrvAddr(Const.NAMESRV_ADDR);
        producer.start();

        for (int i = 0; i < 10; i++) {
            try {
                Message msg = new Message(
                        "test_pull_topic",
                        "TagA",
                        ("Hello RocketMQ "+i).getBytes()
                );

                SendResult sendResult = producer.send(msg);
                System.out.println(sendResult);
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(3000);
            }
        }

        producer.shutdown();
    }
}
