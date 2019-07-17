package com.db.product.api.service;

import com.db.base.BaseResponse;
import com.db.member.output.dto.ProductDto;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * 商品搜索服务接口
 */
public interface ProductSearchService {
    /**
     * 根据关键字返回信息
     * @param name 搜索关键字
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<ProductDto>> search(String name);
}
