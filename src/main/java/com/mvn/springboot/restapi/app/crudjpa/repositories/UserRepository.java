package com.mvn.springboot.restapi.app.crudjpa.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.mvn.springboot.restapi.app.crudjpa.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

}
