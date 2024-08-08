package Client;

import Client.proxy.ClientProxy;
import Common.pojo.User;
import Common.service.UserService;

public class TestClient {
    public static void main(String[] args) throws InterruptedException{
//        ClientProxy clientProxy = new ClientProxy("127.0.0.1", 999);
        ClientProxy clientProxy = new ClientProxy();
        UserService proxy = clientProxy.getProxy(UserService.class);

        User user = proxy.getUserByUserId(100);
        System.out.println("从服务端得到user : " + user.toString());

        User u = User.builder().id(200).userName("xu").gender(true).build();
        Integer id = proxy.insertUserId(u);
        System.out.println("向服务端插入user : id = " + id);
    }
}
