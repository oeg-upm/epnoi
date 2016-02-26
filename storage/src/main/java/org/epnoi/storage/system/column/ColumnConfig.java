package org.epnoi.storage.system.column;

import com.datastax.driver.core.SocketOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by cbadenes on 21/12/15.
 */
@Configuration
@ComponentScan({"org.epnoi.storage.system.column.repository"})
@EnableCassandraRepositories(basePackages = {"org.epnoi.storage.system.column.repository"})
@EnableTransactionManagement
public class ColumnConfig extends AbstractCassandraConfiguration{

    private static final Logger LOG = LoggerFactory.getLogger(ColumnConfig.class);

    @Autowired
    private Environment env;

    @Value("${epnoi.cassandra.contactpoints}")
    String hosts;

    @Value("${epnoi.cassandra.port}")
    Integer port;

    @Bean
    public CassandraClusterFactoryBean cluster(){
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        try{
            cluster.setContactPoints(hosts);
            cluster.setPort(port);
            SocketOptions options = new SocketOptions();
            options.setReadTimeoutMillis(60000);
            options.setConnectTimeoutMillis(20000);
            cluster.setSocketOptions(options);
            LOG.info("Initialized Cassandra connection to: " + hosts + " " + port);
        }catch (Exception e){
            LOG.error("Error configuring cassandra connection parameters: ",e);
        }
        return cluster;
    }

    @Bean
    public CassandraMappingContext mappingContext() {
        return new BasicCassandraMappingContext();
    }

    @Bean
    public CassandraConverter converter() {
        return new MappingCassandraConverter(mappingContext());
    }

    @Override
    protected String getKeyspaceName() {
        return env.getProperty("epnoi.cassandra.keyspace");
    }


}
