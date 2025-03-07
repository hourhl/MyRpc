package Server;

import Server.provider.ServiceProvider;
import Server.server.RPCServer;
import Server.server.impl.NettyRPCServer;
import Server.server.impl.SimpleRPCRPCServer;
import Server.server.impl.ThreadPoolRPCRPCServer;
import common.Service.Impl.UserServiceImpl;
import common.Service.UserService;
import lombok.extern.java.Log;

public class Server {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1", 666);
        serviceProvider.provideServiceInterface(userService, true);

        // 简单利用线程来获取服务
//        RPCServer rpcServer = new SimpleRPCRPCServer(serviceProvider);
//        rpcServer.start(666);

        // 利用线程池来获取服务
//        RPCServer rpcServer = new ThreadPoolRPCRPCServer(serviceProvider);
//        rpcServer.start(666);

        // 利用Netty来通信
        RPCServer rpcServer = new NettyRPCServer(serviceProvider);
        rpcServer.start(666);
    }
}


