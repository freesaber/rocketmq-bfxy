# rocketmq-bfxy
[慕课网 RocketMQ核心技术精讲与高并发抗压实战](https://coding.imooc.com/class/chapter/292.html)

- 本地开启RocketMQ
```yaml
version: '3.5'
services:
  rmqnamesrv:
    image: rocketmqinc/rocketmq
    container_name: rmqnamesrv
    # restart: always
    ports:
      - 9876:9876
    environment:
    #内存分配
      JAVA_OPT_EXT: "-server -Xms1g -Xmx1g"
    volumes:
      - D:\docker-v\rocketmq\namesrv\logs:/root/logs
    command: sh mqnamesrv
    networks:
      rmq:
        aliases:
          - rmqnamesrv
          
  rmqbroker:
    image: rocketmqinc/rocketmq
    container_name: rmqbroker
    # restart: always
    depends_on:
      - rmqnamesrv
    ports:
      - 10909:10909
      - 10911:10911
    volumes:
      - D:\docker-v\rocketmq\broker\logs:/root/logs
      - D:\docker-v\rocketmq\broker\store:/root/store
      - D:\docker-v\rocketmq\broker\conf\broker.conf:/opt/rocketmq-4.4.0/conf/broker.conf
    command: sh mqbroker  -c /opt/rocketmq-4.4.0/conf/broker.conf
    environment:
      NAMESRV_ADDR: "rmqnamesrv:9876"
      JAVA_OPT_EXT: "-server -Xms1g -Xmx1g -Xmn1g"
    networks:
      rmq:
        aliases:
          - rmqbroker
          
  rmqconsole:
    image: styletang/rocketmq-console-ng
    container_name: rocketmq-console
    # restart: always
    ports:
      - 9090:8080
    depends_on:
      - rmqnamesrv
    volumes:
      - D:\docker-v\rocketmq\console\logs:/root/logs
    environment:
      JAVA_OPTS: "-Drocketmq.namesrv.addr=rmqnamesrv:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false"
      TZ: "Asia/Shanghai"
    networks:
      rmq:
        aliases:
          - rmqconsole
          
networks:
  rmq:
    name: rmq
    driver: bridge
```

- broker.conf
```properties
brokerName = broker-a  
brokerId = 0  
deleteWhen = 04  
fileReservedTime = 48  
brokerRole = ASYNC_MASTER  
flushDiskType = ASYNC_FLUSH  
# 如果是本地程序调用云主机 mq，这个需要设置成 云主机 IP
brokerIP1=192.168.10.152
```