package Client;

import common.Service.UserService;
import common.pojo.User;
import lombok.extern.java.Log;

@Log
public class Client {
    public static void main(String[] args) {
        Proxy clientProxy = new Proxy("127.0.0.1", 666);
        UserService proxy = clientProxy.getProxy(UserService.class);

        User user = proxy.getUserById(3);
        log.info("getUserById from server : " + user.toString());
    }
}
