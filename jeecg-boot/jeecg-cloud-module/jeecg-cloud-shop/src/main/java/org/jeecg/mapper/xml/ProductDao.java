package org.jeecg.mapper.xml;

import org.jeecg.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductDao extends ElasticsearchRepository<Product, Integer> {

    public List<Product> findByName(String name);

    public List<Product> findByNameAndCategory(String name, String category);

    public Page<Product> findByNameAndCategory(String name, String category, Pageable pageable);

    public List<Product> deleteByName(String name);

}