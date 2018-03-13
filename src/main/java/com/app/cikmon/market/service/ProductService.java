package com.app.cikmon.market.service;

import com.app.cikmon.market.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    Product save(Product product);
    Page<Product> findAllByPage(Pageable pageable);
    void delete(Long id);
    List<Product> iterable(Iterable<Long> iterable);
}
