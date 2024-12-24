package Server.server.impl;

import Server.provider.ServiceProvider;
import Server.server.RPCServer;
import Server.server.work.WorkThread;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
@Log
public class SimpleRPCRPCServer implements RPCServer {
    private ServiceProvider serviceProvider;
    @Override
    public void start(int port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            log.info("RPC start");
            while(true) {
                Socket socket = serverSocket.accept();
                new Thread(new WorkThread(socket, serviceProvider)).start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop(){}
}
