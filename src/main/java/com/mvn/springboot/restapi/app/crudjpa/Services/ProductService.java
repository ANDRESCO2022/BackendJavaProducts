package com.mvn.springboot.restapi.app.crudjpa.Services;

import java.util.List;
import java.util.Optional;

import com.mvn.springboot.restapi.app.crudjpa.entities.Product;

public interface ProductService {

    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(Product product);
    Optional<Product> update(Long id, Product product);
    Optional<Product> delete(Long id);
    boolean existSku(String sku);
   
}
