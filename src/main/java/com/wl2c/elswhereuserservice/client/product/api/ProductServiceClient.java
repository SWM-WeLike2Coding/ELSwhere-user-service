package com.wl2c.elswhereuserservice.client.product.api;

import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSingleProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @GetMapping("/product/{productId}")
    ResponseSingleProductDto getProduct(@PathVariable Long productId);
}
