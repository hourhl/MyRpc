package Server;

import Common.service.Impl.UserServiceImpl;
import Common.service.UserService;
import Server.provider.ServiceProvider;
import Server.server.RpcServer;
import Server.server.impl.SimpleRPCRPCServer;

public class TestServer {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.providerServiceInterface(userService);

        RpcServer rpcServer = new SimpleRPCRPCServer(serviceProvider);
        rpcServer.start(999);
    }
}
