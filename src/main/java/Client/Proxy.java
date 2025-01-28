package Client;

import Client.retry.guavaRetry;
import Client.rpc.BaseClient;
import Client.rpc.NettyClient;
import Client.rpc.SocketClient;
import Client.serviceCenter.ServiceCenter;
import Client.serviceCenter.ZKServiceCenter;
import com.alibaba.fastjson.JSONObject;
import common.Message.RpcRequest;
import common.Message.RpcResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.java.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Log
public class Proxy implements InvocationHandler {
    private BaseClient client;
    private ServiceCenter serviceCenter;
    public Proxy() throws InterruptedException {
        serviceCenter = new ZKServiceCenter();
        client = new NettyClient(serviceCenter);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("Proxy.invoke: " + method.getName());
        //create request
        RpcRequest request = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes()).build();

        log.info("Send request : " + request);
        RpcResponse response;
        if (serviceCenter.checkRetry(request.getInterfaceName())){
            response = new guavaRetry().sendServiceWithRetry(request, client);
        } else {
            response = client.sendRequest(request);
        }
        return response.getData();
    }

    public <T>T getProxy(Class<T> clazz) {
        Object o = java.lang.reflect.Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T) o;
    }
}
