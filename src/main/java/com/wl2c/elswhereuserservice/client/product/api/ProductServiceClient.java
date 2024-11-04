package com.wl2c.elswhereuserservice.client.product.api;

import com.wl2c.elswhereuserservice.client.product.dto.request.RequestProductIdListDto;
import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSingleProductDto;
import com.wl2c.elswhereuserservice.client.product.dto.list.SummarizedProductDto;
import com.wl2c.elswhereuserservice.client.product.dto.response.ResponseSummarizedProductForHoldingDto;
import com.wl2c.elswhereuserservice.global.model.dto.ResponsePage;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "product-service")
public interface ProductServiceClient {

    @GetMapping("/v1/product/{productId}")
    ResponseSingleProductDto getProduct(@PathVariable Long productId);

    @GetMapping("/v1/product/on-sale")
    ResponsePage<SummarizedProductDto> listByOnSale(@RequestParam(name = "type") String type, @ParameterObject Pageable pageable);

    @PostMapping("/v1/product/list")
    List<SummarizedProductDto> listByProductIds(@Valid @RequestBody RequestProductIdListDto requestProductIdListDto);

    @PostMapping("/v1/product/holding/list")
    List<ResponseSummarizedProductForHoldingDto> holdingListByProductIds(@Valid @RequestBody RequestProductIdListDto requestProductIdListDto);

    @PostMapping("/v1/product/like/dump")
    void dumpLike(@RequestHeader("requestId") String requestId);
}
