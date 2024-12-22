package Client;

import common.Service.UserService;
import common.pojo.User;

public class Client {
    public static void main(String[] args) {
        Proxy clientProxy = new Proxy("127.0.0.1", 6666);
        UserService proxy = clientProxy.getProxy(UserService.class);

        User user = proxy.getUserById(1);
        System.out.println("getUserById from server : " + user.toString());
    }
}
