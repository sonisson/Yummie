package com.yummie.config;

import com.yummie.entity.CartItemEntity;
import com.yummie.entity.OrderItemEntity;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(CartItemEntity.class, OrderItemEntity.class)
                .addMappings(mapper -> mapper.skip(OrderItemEntity::setId));
        return modelMapper;
    }
}
