package com.mvn.springboot.restapi.app.crudjpa.Services;

import java.util.List;

import com.mvn.springboot.restapi.app.crudjpa.entities.User;

public interface UserService {

    List<User> findAll();

    User save(User user);

    boolean existByUserName(String username);

}
