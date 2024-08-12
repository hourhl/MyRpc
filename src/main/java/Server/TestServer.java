package Server;

import Common.service.Impl.UserServiceImpl;
import Common.service.UserService;
import Server.provider.ServiceProvider;
import Server.server.RpcServer;
import Server.server.impl.NettyRPCRPCServer;
import Server.server.impl.SimpleRPCRPCServer;

public class TestServer {
    public static void main(String[] args) throws InterruptedException{
        UserService userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider("127.0.0.1",999);
        serviceProvider.providerServiceInterface(userService, true);

//        RpcServer rpcServer = new SimpleRPCRPCServer(serviceProvider);
        RpcServer rpcServer = new NettyRPCRPCServer(serviceProvider);
        rpcServer.start(999);
    }
}
