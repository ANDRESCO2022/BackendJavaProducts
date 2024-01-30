package com.mvn.springboot.restapi.app.crudjpa.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvn.springboot.restapi.app.crudjpa.entities.Role;
import com.mvn.springboot.restapi.app.crudjpa.entities.User;
import com.mvn.springboot.restapi.app.crudjpa.repositories.RoleRepopsitory;
import com.mvn.springboot.restapi.app.crudjpa.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepopsitory roleRepopsitory;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();

    }

    @Override
    @Transactional
    public User save(User user) {
        Optional<Role> optionalRoleUser = roleRepopsitory.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        optionalRoleUser.ifPresent(roles::add);
        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepopsitory.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);// role->roles.add(role)

        }
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));// encriptar contraseña
        return userRepository.save(user);

    }

    @Override
    public boolean existByUserName(String username) {
        return userRepository.existsByUsername(username);
    }

}
