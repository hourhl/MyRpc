package Server.provider;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Log
public class ServiceProvider {

    // 存放服务实例
    private HashMap<String, Object> interfaceProvider;

    public ServiceProvider() {
        this.interfaceProvider = new HashMap<>();
    }

    // 注册服务
    public void provideServiceInterface(Object service) {
        String serviceName = service.getClass().getName();
        log.info("provideServiceInterface - serviceName :" + serviceName);
        Class<?>[] interfaceName = service.getClass().getInterfaces();

        for(Class<?> clazz : interfaceName){
            interfaceProvider.put(clazz.getName(), service);
            log.info("add interface - " + clazz.getName() + " : " + service);
        }
    }

    // 获取服务
    public Object getService(String interfaceName) {
        return interfaceProvider.get(interfaceName);
    }
}
