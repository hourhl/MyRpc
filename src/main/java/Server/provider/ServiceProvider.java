package Server.provider;

import Server.registerCenter.RegisterCenter;
import Server.registerCenter.ZKRegisterCenter;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashMap;

@Log
public class ServiceProvider {
    private String host;
    private int port;
    private RegisterCenter registerCenter;

    // 存放服务实例
    private HashMap<String, Object> interfaceProvider;

    public ServiceProvider(String host, int port) {
        this.interfaceProvider = new HashMap<>();
        this.host = host;
        this.port = port;
        this.registerCenter = new ZKRegisterCenter();
    }

    // 注册服务
    public void provideServiceInterface(Object service) {
        String serviceName = service.getClass().getName();
        log.info("provideServiceInterface - serviceName :" + serviceName);
        Class<?>[] interfaceName = service.getClass().getInterfaces();

        for(Class<?> clazz : interfaceName){
            interfaceProvider.put(clazz.getName(), service);
            registerCenter.serviceRegister(clazz.getName(), new InetSocketAddress(host, port));
            log.info("add interface - " + clazz.getName() + " : " + service);
        }
    }

    // 获取服务
    public Object getService(String interfaceName) {
        return interfaceProvider.get(interfaceName);
    }
}
