package Server.provider;

import Server.serviceRegister.ServiceRegister;
import Server.serviceRegister.impl.ZKServiceRegister;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

// 本地服务存放器
public class ServiceProvider {
    // 集合中存放服务的实例
    private Map<String,Object> interfaceProvider;
    private int port;
    private String host;
    private ServiceRegister serviceRegister;
    public ServiceProvider(String host, int port) {
        // 需要传入服务端自身的网络地址
        this.host = host;
        this.port = port;
        this.interfaceProvider = new HashMap<>();
        this.serviceRegister = new ZKServiceRegister();
    }

    // 本地注册服务
    public void providerServiceInterface(Object service){
        String serviceName = service.getClass().getName();
        Class<?>[] interfaceName = service.getClass().getInterfaces();

        for(Class<?> clazz : interfaceName) {
            // 本机的映射表
            interfaceProvider.put(clazz.getName(), service);
            // 在注册中心注册服务
            serviceRegister.register(clazz.getName(), new InetSocketAddress(host, port));
        }
    }

    // 获取服务实例
    public Object getService(String interfaceName) {
        return interfaceProvider.get(interfaceName);
    }
}

