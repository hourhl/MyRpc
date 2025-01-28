package Server.registerCenter;

import lombok.extern.java.Log;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;


@Log
public class ZKRegisterCenter implements RegisterCenter{
    private CuratorFramework client;
    private static final String ROOT_PATH = "MyRPC";
    private static final String RETRY = "canRetry";

    public ZKRegisterCenter() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000).retryPolicy(retryPolicy).namespace(ROOT_PATH).build();
        this.client.start();
        log.info("ZKRegisterCenter started");
    }

    @Override
    public void serviceRegister(String serviceName, InetSocketAddress serviceAddress, boolean canRetry){
        try {
            if(client.checkExists().forPath("/" + serviceName) == null){
                this.client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            }
            String path = "/" + serviceName + "/" + getServiceAddress(serviceAddress);
            this.client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            log.info("ZKRegisterCenter service registered " + serviceName + " and it's path is " + path);
            if (canRetry) {
                path = "/" + RETRY + serviceName;
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getServiceAddress(InetSocketAddress serviceAddress){
        return serviceAddress.getHostName() + ":"+ serviceAddress.getPort();
    }
}
