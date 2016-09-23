package com.okay.zwj;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        int port = 1099;
        String url = "rmi://localhost:1098/demo.zookeeper.remoting.server.HelloServiceImpl" ;
        LocateRegistry. createRegistry(port);
        Naming. rebind(url, new HelloServiceImpl());
    }
}
