package com.db.product.es.reposiory;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.db.product.es.entity.ProductEntity;

public interface ProductReposiory extends ElasticsearchRepository<ProductEntity, Long> {

}
 