package Server.registerCenter;

import lombok.extern.java.Log;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;


@Log
public class ZKRegisterCenter implements RegisterCenter{
    private CuratorFramework client;
    private static final String ROOT_PATH = "MyRPC";

    public ZKRegisterCenter() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000).retryPolicy(retryPolicy).build();
        this.client.start();
        log.info("ZKRegisterCenter started");
    }

    @Override
    public void serviceRegister(String serviceName, InetSocketAddress serviceAddress){
        try {
            if(client.checkExists().forPath("/" + serviceName) == null){
                client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }
            String path = "/" + serviceName + "/" + getServiceAddress(serviceAddress);
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getServiceAddress(InetSocketAddress serviceAddress){
        return serviceAddress.getHostName() + ":"+ serviceAddress.getPort();
    }
}
