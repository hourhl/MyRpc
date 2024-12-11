package common.Service.Impl;

import common.Service.UserService;
import common.pojo.User;

public class UserServiceImpl implements UserService {

    @Override
    public User getUserById(int id) {
        User user = new User();
        user.setId(id);
        user.setName("test");
        user.setEmail("test@test.com");
        return user;
    }
}
