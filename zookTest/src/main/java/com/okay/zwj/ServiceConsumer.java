package com.okay.zwj;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
 
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
 
public class ServiceConsumer {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConsumer. class);
 

    private CountDownLatch latch = new CountDownLatch(1);
 
    
    private volatile List<String> urlList = new ArrayList<>();
 
   
    public ServiceConsumer() {
        ZooKeeper zk = connectServer(); 
        if (zk != null) {
            watchNode( zk); 
        }
    }
 
   
    public <T extends Remote> T lookup() {
        T service = null ;
        int size = urlList.size();
        if (size > 0) {
            String url;
            if (size == 1) {
                url = urlList .get(0); 
                LOGGER.debug("using only url: {}" , url );
            } else {
                url = urlList.get(ThreadLocalRandom.current().nextInt( size)); 
                LOGGER.debug("using random url: {}" , url );
            }
            service = lookupService( url); 
        }
        return service ;
    }
 
    
    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(Constant.ZK_CONNECTION_STRING, Constant.ZK_SESSION_TIMEOUT , new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event .getState() == Event.KeeperState.SyncConnected ) {
                        latch.countDown(); 
                    }
                }
            });
            latch.await(); 
        } catch (IOException | InterruptedException e) {
            LOGGER.error("" , e );
        }
        return zk ;
    }
 
 
    private void watchNode(final ZooKeeper zk) {
        try {
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event .getType() == Event.EventType.NodeChildrenChanged ) {
                        watchNode( zk); 
                    }
                }
            });
            List<String> dataList = new ArrayList<>();
            for (String node : nodeList) {
                byte[] data = zk.getData(Constant. ZK_REGISTRY_PATH + "/" + node, false , null); 
                dataList.add(new String(data));
            }
            LOGGER.debug("node data: {}" , dataList );
            urlList = dataList ; 
        } catch (KeeperException | InterruptedException e) {
            LOGGER.error("" , e );
        }
    }
 

    @SuppressWarnings( "unchecked")
    private <T> T lookupService(String url) {
        T remote = null ;
        try {
            remote = (T) Naming.lookup( url);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            if (e instanceof ConnectException) {
               
                LOGGER.error("ConnectException -> url: {}" , url );
                if (urlList .size() != 0) {
                    url = urlList .get(0);
                    return lookupService( url);
                }
            }
            LOGGER.error("" , e );
        }
        return remote ;
    }
}