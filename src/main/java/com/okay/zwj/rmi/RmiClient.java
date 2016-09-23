package com.okay.zwj.rmi;
 
public class RmiClient {
 
    public static void main(String[] args) throws Exception {
//        String url = "rmi://localhost:1099/demo.zookeeper.remoting.server.HelloServiceImpl" ;
//        HelloService helloService = (HelloService) Naming.lookup( url);
//        String result = helloService .sayHello("Jack");
//        System. out.println(result );
    	ServiceConsumer consumer = new ServiceConsumer();
    	 
        while (true ) {
            HelloService helloService = consumer .lookup();
            String result = helloService .sayHello("Jack");
            System. out.println(result );
            Thread. sleep(3000);
        }
    
    }
}