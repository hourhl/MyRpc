package Client.serviceCenter;

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


    //初始化zookeeper客户端
    public ZKServiceCenter() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        // 指定zookeeper地址
        this.client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(40000).retryPolicy(retryPolicy).namespace(ROOT_PATH).build();
        this.client.start();
        log.info("ZKServiceCenter started");
    }

    @Override
    public InetSocketAddress serviceDiscovery(String serviceName) {
        try {
            List<String> strings = this.client.getChildren().forPath("/" + serviceName);
            String address = strings.get(0);
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
}
