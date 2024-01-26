package com.mvn.springboot.restapi.app.crudjpa.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvn.springboot.restapi.app.crudjpa.entities.Product;
import com.mvn.springboot.restapi.app.crudjpa.repositories.ProductRepository;

@Service
public class ProductServicesImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        return (List<Product>) productRepository.findAll();

    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);

    }

    @Transactional
    @Override
    public Product save(Product product) {
        return productRepository.save(product);

    }

    @Override
    @Transactional
    public Optional<Product> update(Long id, Product product) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product productDb = productOptional.orElseThrow();
            productDb.setSku(product.getSku());
            productDb.setName(product.getName());
            productDb.setPrice(product.getPrice());
            productDb.setDescription(product.getDescription());
            return Optional.of(productRepository.save(productDb));

        }
        ;
        return productOptional;
    }

    @Transactional
    @Override
    public Optional<Product> delete(Long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        productOptional.ifPresent(productDb -> {
            productRepository.delete(productDb);
        });
        return productOptional;
    }
    @Transactional(readOnly = true)
    @Override
    public boolean existSku(String sku) {
        return productRepository.existsBySku(sku);
       
    }

  

}
