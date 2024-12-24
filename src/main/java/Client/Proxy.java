package Client;

import common.Message.RpcRequest;
import common.Message.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.java.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Data
@Builder
@AllArgsConstructor
@Log
public class Proxy implements InvocationHandler {
    private String host;
    private int port;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //create request
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes()).build();

        log.info("Send request : " + request);
        RpcResponse response = IOClient.sendRequest(host, port, request);
        return response.getData();
    }

    public <T>T getProxy(Class<T> clazz) {
        Object o = java.lang.reflect.Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }
}
