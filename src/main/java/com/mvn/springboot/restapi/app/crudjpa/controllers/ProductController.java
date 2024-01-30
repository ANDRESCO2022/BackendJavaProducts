package com.mvn.springboot.restapi.app.crudjpa.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

//import com.mvn.springboot.restapi.app.crudjpa.ProductValidation;
import com.mvn.springboot.restapi.app.crudjpa.Services.ProductService;
import com.mvn.springboot.restapi.app.crudjpa.entities.Product;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    // @Autowired
    // private ProductValidation validation;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN','USER')")
    public List<Product> list() {
        return productService.findAll();

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN','USER')")
    public ResponseEntity<?> view(@PathVariable Long id) {
        Optional<Product> prOptional = productService.findById(id);
        if (prOptional.isPresent()) {
            return ResponseEntity.ok(prOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody Product product, BindingResult result) {
        // validation.validate(product,result);
        if (result.hasFieldErrors()) {
            return validationResult(result);

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(product));

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @CrossOrigin(origins = "http://localhost:4200",originPatterns = "*") // CORS
    public ResponseEntity<?> update(@Valid @RequestBody Product product, BindingResult result,
            @PathVariable Long id) {
        // validation.validate(product,result);
        if (result.hasFieldErrors()) {
            return validationResult(result);

        }

        Optional<Product> productOptional = productService.update(id, product);
        if (productOptional.isPresent()) {

            return ResponseEntity.status(HttpStatus.CREATED).body(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Product> productOptional = productService.delete(id);
        if (productOptional.isPresent()) {
            return ResponseEntity.ok(productOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<?> validationResult(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
