package com.app.cikmon.market.dao;

import com.app.cikmon.market.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product,Long> {
}
