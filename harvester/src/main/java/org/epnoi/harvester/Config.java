package org.epnoi.harvester;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.idempotent.FileIdempotentRepository;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import java.io.File;
import java.util.List;

/**
 * Created by cbadenes on 01/12/15.
 */
@Configuration
@ComponentScan({"org.epnoi.harvester","org.epnoi.storage"})
@PropertySource("classpath:harvester.properties")
public class Config {


    @Autowired
    List<RouteBuilder> builders;

    @Autowired
    private Environment env;

    @Value("${epnoi.harvester.folder.input}")
    String inputFolder;

    @Bean
    public SpringCamelContext camelContext(ApplicationContext applicationContext) throws Exception {
        SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
        for(RouteBuilder builder : builders){
            camelContext.addRoutes(builder);
        }
        return camelContext;
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name = "fileStore")
    public FileIdempotentRepository getFileStore(){
        FileIdempotentRepository repository = new FileIdempotentRepository();
        repository.setFileStore(new File(inputFolder+File.separator+".fileStore.dat"));
        repository.setMaxFileStoreSize(512000);
        repository.setCacheSize(1000);
        return repository;
    }

}
