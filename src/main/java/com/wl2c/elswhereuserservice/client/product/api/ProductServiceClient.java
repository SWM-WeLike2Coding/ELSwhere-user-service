package com.wl2c.elswhereuserservice.client.product.api;

import com.wl2c.elswhereuserservice.client.product.dto.request.RequestProductIdListDto;
import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSingleProductDto;
import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSummarizedProductDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @GetMapping("/product/{productId}")
    ResponseSingleProductDto getProduct(@PathVariable Long productId);

    @PostMapping("/product/list")
    List<ResponseSummarizedProductDto> listByProductIds(@Valid @RequestBody RequestProductIdListDto requestProductIdListDto);
}
