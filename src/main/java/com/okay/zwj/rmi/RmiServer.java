package com.okay.zwj.rmi;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
 
public class RmiServer {
 
	public static void main( String[] args ) throws Exception
    {
		  if (args .length != 2) {
	            System. err.println("please using command: java Server <rmi_host> <rmi_port>");
	            System. exit(-1);
	        }
	 
	        String host =args [0];//"localhost"; 
	        int port = Integer.parseInt (args [1]);//1098;
	 
	        ServiceProvider provider = new ServiceProvider();
	 
	        HelloService helloService = new HelloServiceImpl();
	        provider.publish(helloService , host , port );
	 
	        Thread. sleep(Long.MAX_VALUE);
    }
}
