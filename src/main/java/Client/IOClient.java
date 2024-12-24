package Client;

import common.Message.RpcRequest;
import common.Message.RpcResponse;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@Log
public class IOClient {

    public static RpcResponse sendRequest(String host , int port, RpcRequest request){
        try {
            // 建立连接
            Socket socket = new Socket(host, port);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(request);
            oos.flush();

            RpcResponse rpcResponse = (RpcResponse) ois.readObject();
            log.info("Response : " + rpcResponse);
            return rpcResponse;
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
}
