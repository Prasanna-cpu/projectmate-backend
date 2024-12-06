package com.spring.backend.Service.Abstraction;

import com.spring.backend.Model.User;

public interface UserService {

    User findUserProfileByJwt(String jwt);

    User findUserByEmail(String email);

    User findUserById(Long userId);

    User updateUsersProjectSize(User user,int number);

}
