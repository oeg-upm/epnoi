package org.epnoi.parser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by cbadenes on 11/02/16.
 */
@Configuration("parser")
@ComponentScan({"org.epnoi.parser","org.epnoi.storage","org.epnoi.eventbus"})
@PropertySource({"classpath:parser.properties","classpath:eventbus.properties","classpath:storage.properties"})
public class Config {

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
