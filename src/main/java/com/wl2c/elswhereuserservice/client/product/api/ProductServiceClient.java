package com.wl2c.elswhereuserservice.client.product.api;

import com.wl2c.elswhereuserservice.client.product.dto.request.RequestProductIdListDto;
import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSingleProductDto;
import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSummarizedProductDto;
import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSummarizedProductForHoldingDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @GetMapping("/v1/product/{productId}")
    ResponseSingleProductDto getProduct(@PathVariable Long productId);

    @PostMapping("/v1/product/list")
    List<ResponseSummarizedProductDto> listByProductIds(@Valid @RequestBody RequestProductIdListDto requestProductIdListDto);

    @PostMapping("/v1/product/holding/list")
    List<ResponseSummarizedProductForHoldingDto> holdingListByProductIds(@Valid @RequestBody RequestProductIdListDto requestProductIdListDto);

    @PostMapping("/v1/product/like/dump")
    void dumpLike(@RequestHeader("requestId") String requestId);
}
