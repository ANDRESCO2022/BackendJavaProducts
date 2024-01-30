package com.mvn.springboot.restapi.app.crudjpa.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mvn.springboot.restapi.app.crudjpa.Services.UserService;
import com.mvn.springboot.restapi.app.crudjpa.entities.User;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:4200",originPatterns = "*") // cors para  acceso de  rutas  frontend 
@RequestMapping("/api/users")
public class UserController {

   @Autowired
   private UserService userService;

   @GetMapping
   public List<User> list() {
      return userService.findAll();
   }

   @PostMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
      if (result.hasFieldErrors()) {
         return validationResult(result);

      }

      return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
   }

   @PostMapping("/register")
   public ResponseEntity<?> register(@Valid @RequestBody User user, BindingResult result) {

      user.setAdmin(false);
      return create(user, result);
   }

   private ResponseEntity<?> validationResult(BindingResult result) {
      Map<String, String> errors = new HashMap<>();
      result.getFieldErrors().forEach(err -> {
         errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
      });
      return ResponseEntity.badRequest().body(errors);
   }
}
