package Client.serviceCenter;

import Client.cache.serviceCache;
import Client.serviceCenter.balance.impl.HashBalance;
import Client.serviceCenter.zkWatcher.ZKWatcher;
import lombok.extern.java.Log;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.net.InetSocketAddress;
import java.util.List;

@Log
public class ZKServiceCenter implements ServiceCenter{
    private CuratorFramework client;
    private static final String ROOT_PATH = "MyRPC";
    private static final String RETRY = "CanRetry";
    private serviceCache cache;

    //初始化zookeeper客户端
    public ZKServiceCenter() throws InterruptedException {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        // 指定zookeeper地址
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000).retryPolicy(retryPolicy).namespace(ROOT_PATH).build();
        this.client.start();
        log.info("ZKServiceCenter started");

        // 初始化本地缓存
        this.cache = new serviceCache();
        // 注册监听器
        ZKWatcher zkWatcher = new ZKWatcher(this.client, this.cache);
        zkWatcher.watchToUpdate("/");
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            List<String> serviceLists = this.cache.getAddressFromCache(serviceName);
            if(serviceLists == null){
                log.info("Cannot find serviceLists");
                serviceLists = this.client.getChildren().forPath("/" + serviceName);
            }
            log.info("serviceLists : " + serviceLists);
            String address = new HashBalance().balance(serviceLists);
            return parseAddress(address);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private InetSocketAddress parseAddress(String address){
        String[] result = address.split(":");
        return new InetSocketAddress(result[0], Integer.parseInt(result[1]));
    }

    @Override
    public boolean checkRetry(String serviceName) {
        boolean canRetry = false;
        try {
            List<String> serviceList = this.client.getChildren().forPath("/" + RETRY);
            for (String s : serviceList) {
                if(s.equals(serviceName)){
                    log.info("Service : " + serviceName + "can retry");
                    canRetry = true;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return canRetry;
    }
}
