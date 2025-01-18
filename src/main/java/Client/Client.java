package Client;

import common.Service.UserService;
import common.pojo.User;
import lombok.extern.java.Log;

@Log
public class Client {
    public static void main(String[] args) {
//        Proxy clientProxy = new Proxy("127.0.0.1", 666, 1);
        Proxy clientProxy = new Proxy();
        UserService userServiceProxy = clientProxy.getProxy(UserService.class);

        User user = userServiceProxy.getUserById(6);
        log.info("getUserById from server : " + user.toString());
    }
}
