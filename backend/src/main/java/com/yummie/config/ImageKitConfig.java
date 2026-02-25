package com.yummie.config;

import io.imagekit.sdk.ImageKit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageKitConfig {

    @Value("${imagekit.privateKey}")
    private String privateKey;

    @Bean
    public ImageKit imageKit() {
        io.imagekit.sdk.config.Configuration configuration = new io.imagekit.sdk.config.Configuration(null, privateKey, null);
        ImageKit imageKit = ImageKit.getInstance();
        imageKit.setConfig(configuration);
        return imageKit;
    }
}
