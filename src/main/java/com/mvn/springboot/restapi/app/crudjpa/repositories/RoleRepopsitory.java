package com.mvn.springboot.restapi.app.crudjpa.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.mvn.springboot.restapi.app.crudjpa.entities.Role;

public interface RoleRepopsitory extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
