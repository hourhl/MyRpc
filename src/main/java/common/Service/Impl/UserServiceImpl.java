package common.Service.Impl;

import common.Service.UserService;
import common.pojo.User;

public class UserServiceImpl implements UserService {

    @Override
    public User getUserById(int id) {
        return User.builder()
                .id(id)
                .name("test")
                .email("test@test.com").build();
    }
}
