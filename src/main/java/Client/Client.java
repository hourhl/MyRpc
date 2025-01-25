package Client;

import com.alibaba.fastjson.JSONObject;
import common.Message.RpcRequest;
import common.Service.UserService;
import common.pojo.User;
import lombok.extern.java.Log;

@Log
public class Client {
    public static void main(String[] args) throws InterruptedException {
//        Proxy clientProxy = new Proxy("127.0.0.1", 666, 1);
        Proxy clientProxy = new Proxy();
        UserService userServiceProxy = clientProxy.getProxy(UserService.class);

        User user1 = userServiceProxy.getUserById(9);
        log.info("getUserById from server : " + user1.toString());

        User user2 = userServiceProxy.getUserById(6);
        log.info("getUserById from server : " + user2.toString());

        User user3 = userServiceProxy.getUserById(3);
        log.info("getUserById from server : " + user3.toString());
    }
}
