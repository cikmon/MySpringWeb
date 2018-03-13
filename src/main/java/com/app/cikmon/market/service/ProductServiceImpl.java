package com.app.cikmon.market.service;

import com.app.cikmon.market.dao.ProductRepository;
import com.app.cikmon.market.model.Product;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link ProductService} interface.
 *
 * @author cikmon
 * @version 1.0
 */
@Repository
@Transactional
@Service("productService")
public class ProductServiceImpl implements ProductService{
    private ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return Lists.newArrayList(productRepository.findAll()); }

    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findOne(id);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAllByPage(Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id){productRepository.delete(id); }

    @Override
    @Transactional
    public List<Product> iterable(Iterable<Long> iterable){
        return Lists.newArrayList(productRepository.findAll(iterable));}

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository; }
}
