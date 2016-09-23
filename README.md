# RmiServerByZooKeeper
使用zookeeper构建高可用的rmi服务
1. 搭建搭建好zookeeper 环境 
   测试环境配置文件 :
   clientPort=2181
  initLimit=10
  autopurge.purgeInterval=24
  syncLimit=5
  tickTime=2000
  dataDir=/hadoop/zookeeper
  autopurge.snapRetainCount=30
  server.1=192.168.160.90:2888:3888
  server.2=192.168.160.90:2888:3888
  server.3=192.168.160.90:2888:3888
2. 执行mvn clean package  编译 
3. 运行服务端:
  服务1：
  mvn exec:java -Dexec.mainClass="com.okay.zwj.rmi.RmiServer" -Dexec.args="localhost 10001"
  服务2：
  mvn exec:java -Dexec.mainClass="com.okay.zwj.rmi.RmiServer" -Dexec.args="localhost 10002"
  服务3：
  mvn exec:java -Dexec.mainClass="com.okay.zwj.rmi.RmiServer" -Dexec.args="localhost 10003"
4. 运行客户端：
mvn exec:java -Dexec.mainClass="com.okay.zwj.rmi.RmiClient"
5. 通过停止服务器1 或2 或3， 观察客户端的运行情况 
