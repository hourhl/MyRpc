package Client.serviceCenter;

import java.net.InetSocketAddress;

public interface ServiceCenter {
    // 查询：根据服务名称查找地址
    InetSocketAddress serviceDiscovery(String serviceName);
}
