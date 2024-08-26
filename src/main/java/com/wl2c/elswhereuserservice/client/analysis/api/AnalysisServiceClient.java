package com.wl2c.elswhereuserservice.client.analysis.api;

import com.wl2c.elswhereuserservice.client.analysis.dto.response.ResponsePriceRatioDto;
import com.wl2c.elswhereuserservice.client.product.dto.request.RequestProductIdListDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "analysis-service")
public interface AnalysisServiceClient {

    @PostMapping("/v1/product/price/ratio/list")
    List<ResponsePriceRatioDto> getPriceRatioList(@Valid @RequestBody RequestProductIdListDto requestProductIdListDto);
}
