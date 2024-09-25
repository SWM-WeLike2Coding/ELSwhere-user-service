package com.wl2c.elswhereuserservice.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wl2c.elswhereuserservice.domain.user.exception.UserNotFoundException;
import com.wl2c.elswhereuserservice.domain.user.model.dto.ProductLikeMessage;
import com.wl2c.elswhereuserservice.domain.user.model.entity.LikeState;
import com.wl2c.elswhereuserservice.domain.user.model.entity.ProductLike;
import com.wl2c.elswhereuserservice.domain.user.model.entity.User;
import com.wl2c.elswhereuserservice.domain.user.repository.UserProductLikeRepository;
import com.wl2c.elswhereuserservice.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductLikeMessageReceiver {

    private final UserRepository userRepository;
    private final UserProductLikeRepository productLikeRepository;

    @Transactional
    @KafkaListener(topics = "product-like", groupId = "product-like-consumer", containerFactory = "kafkaConsumerContainerFactory")
    public void receive(String stringMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ProductLikeMessage productLikeMessage = objectMapper.readValue(stringMessage, ProductLikeMessage.class);
        log.info("product-like Message Consumed : " + stringMessage);

        User user = userRepository.findById(productLikeMessage.getUserId()).orElseThrow(UserNotFoundException::new);

        if (productLikeMessage.getLikeState().equals(LikeState.LIKED)) {
            productLikeRepository.save(new ProductLike(user, productLikeMessage.getProductId()));
        } else if (productLikeMessage.getLikeState().equals(LikeState.CANCELLED)) {
            productLikeRepository.deleteByProductIdAndUserId(productLikeMessage.getProductId(), user.getId());
        }
    }
}
