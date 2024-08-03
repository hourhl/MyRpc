package Server.server.impl;

import Server.provider.ServiceProvider;
import Server.server.RpcServer;
import Server.server.work.WorkThread;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
public class SimpleRPCRPCServer implements RpcServer {
    private ServiceProvider serviceProvider;

    @Override
    public void start(int port){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("服务器已启动");
            while(true) {
                // 如果没有连接，会堵塞在这里
                Socket socket = serverSocket.accept();
                // 有连接，创建一个新的线程执行处理
                new Thread(new WorkThread(socket, serviceProvider)).start();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void stop(){
        // todo
    }
}
