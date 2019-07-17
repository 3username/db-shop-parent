package com.db.product.api.service.impl;

import com.db.base.BaseApiService;
import com.db.base.BaseResponse;
import com.db.member.output.dto.ProductDto;
import com.db.product.api.service.ProductSearchService;
import com.db.product.es.reposiory.ProductReposiory;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductSearchServiceImpl extends BaseApiService<List<ProductDto>> implements ProductSearchService {

	private final ProductReposiory productReposiory;
	@Autowired
	public ProductSearchServiceImpl(ProductReposiory productReposiory) {
		this.productReposiory = productReposiory;
	}

	@Override
	public BaseResponse<List<ProductDto>> search(String name) {
		// 1.拼接查询条件
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		// 2.模糊查询name字段
		builder.must(QueryBuilders.fuzzyQuery("name", name));
		Pageable pageable = new QPageRequest(0, 5);
		// 3.调用ES接口查询
		Page<com.db.product.es.entity.ProductEntity> page = productReposiory.search(builder, pageable);
		// 4.获取集合数据
		List<com.db.product.es.entity.ProductEntity> content = page.getContent();
		// 5.将entity转换dto
		MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		List<ProductDto> mapAsList = mapperFactory.getMapperFacade().mapAsList(content, ProductDto.class);
		return setResultSuccess(mapAsList);
	}
}
