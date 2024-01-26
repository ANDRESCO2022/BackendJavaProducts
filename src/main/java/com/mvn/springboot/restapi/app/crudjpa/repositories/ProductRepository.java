package com.mvn.springboot.restapi.app.crudjpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.mvn.springboot.restapi.app.crudjpa.entities.Product;

public interface ProductRepository extends CrudRepository <Product, Long> {
    boolean existsBySku(String sku);

}
