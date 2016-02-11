package org.epnoi.nlp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by cbadenes on 10/02/16.
 */
@Configuration
@ComponentScan({"org.epnoi.nlp","org.epnoi.storage","org.epnoi.eventbus"})
@PropertySource({"classpath:nlp.properties","classpath:storage.properties","classpath:eventbus.properties"})
public class Config {

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
