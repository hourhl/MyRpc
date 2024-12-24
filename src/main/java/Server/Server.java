package Server;

import Server.provider.ServiceProvider;
import Server.server.RPCServer;
import Server.server.impl.SimpleRPCRPCServer;
import common.Service.Impl.UserServiceImpl;
import common.Service.UserService;

public class Server {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();
        serviceProvider.provideServiceInterface(userService);

        RPCServer rpcServer = new SimpleRPCRPCServer(serviceProvider);
        rpcServer.start(666);
    }
}
