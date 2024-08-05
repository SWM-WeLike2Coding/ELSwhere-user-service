package com.wl2c.elswhereuserservice.global.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wl2c.elswhereuserservice.global.model.dto.ResponseSuccessDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class VoidSuccessResponseInterceptor implements HandlerInterceptor {
    private final ObjectMapper objectMapper;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception ex) throws Exception {
        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        if (!status.is2xxSuccessful()) {
            return;
        }

        if (response.getContentType() != null) {
            return;
        }

        String wrappedBody = objectMapper.writeValueAsString(new ResponseSuccessDto());

        byte[] bytesData = wrappedBody.getBytes();
        response.setContentType("application/json");
        response.resetBuffer();
        response.getOutputStream().write(bytesData, 0, bytesData.length);
    }
}
