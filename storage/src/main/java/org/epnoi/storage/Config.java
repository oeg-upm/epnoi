package org.epnoi.storage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by cbadenes on 21/12/15.
 */
@Configuration("storage")
@ComponentScan({"org.epnoi.storage","org.epnoi.eventbus"})
@PropertySource({"classpath:storage.properties","classpath:eventbus.properties"})
public class Config {

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
