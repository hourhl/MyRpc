package Common.service.Impl;

import Common.pojo.User;
import Common.service.UserService;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    @Override
    public User getUserByUserId(Integer id){
        System.out.println("客户端查询用户 ： id = " + id);
        // 模拟从数据库中获取用户的行为
        // todo
        Random random = new Random();
        User user = User.builder().userName(UUID.randomUUID().toString())
                .id(id)
                .gender(random.nextBoolean()).build();
        return user;
    }

    @Override
    public Integer insertUserId(User user) {
        System.out.println("插入数据: userName = "+user.getUserName());
        return user.getId();
    }
}
