package common.Service.Impl;

import common.Service.UserService;
import common.pojo.User;

public class UserServiceImpl implements UserService {

    @Override
    public User getUserById(int id) {
        return User.builder()
                .id(id)
                .name("test" + Integer.toString(id))
                .email("test" + Integer.toString(id) + "@test.com").build();
    }
}
