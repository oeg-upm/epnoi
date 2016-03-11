package org.epnoi.storage.system.graph;

import org.epnoi.storage.system.graph.domain.edges.Edge;
import org.epnoi.storage.system.graph.domain.nodes.Node;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.conversion.MetaDataDrivenConversionService;
import org.springframework.data.neo4j.event.AfterDeleteEvent;
import org.springframework.data.neo4j.event.AfterSaveEvent;
import org.springframework.data.neo4j.event.BeforeSaveEvent;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by cbadenes on 21/12/15.
 */
@Configuration
@ComponentScan({"org.epnoi.storage.system.graph.repository"})
@EnableNeo4jRepositories(basePackages = {"org.epnoi.storage.system.graph.repository"})
@EnableTransactionManagement
public class GraphConfig extends Neo4jConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(GraphConfig.class);

    @Value("${epnoi.neo4j.contactpoints}")
    String hosts;

    @Value("${epnoi.neo4j.port}")
    Integer port;



//    @Override
//    @Bean
//    public Neo4jServer neo4jServer() {
//        RemoteServer server = new RemoteServer("http://" + hosts + ":" +port);
//        LOG.info("Initialized Neo4j connection to: " + hosts + " " + port);
//        return server;
//    }


//    @Bean
//    ApplicationListener<BeforeSaveEvent> beforeSaveEventApplicationListener() {
//        return new ApplicationListener<BeforeSaveEvent>() {
//            @Override
//            public void onApplicationEvent(BeforeSaveEvent event) {
//                Object entity = (Object) event.getEntity();
//                if (entity instanceof Node){
//                    ((Node) entity).setId(graphIdFactory.from(((Node) entity).getUri()));
//                }else if (entity instanceof Edge){
//                    ((Edge) entity).setId(graphIdFactory.from(((Edge) entity).getUri()));
//                }
//            }
//        };
//    }

//    @Bean
//    ApplicationListener<AfterSaveEvent> afterSaveEventApplicationListener() {
//        return new ApplicationListener<AfterSaveEvent>() {
//            @Override
//            public void onApplicationEvent(AfterSaveEvent event) {
//                AcmeEntity entity = (AcmeEntity) event.getEntity();
//                auditLog.onEventSaved(entity);
//            }
//
//        };
//    }

//    @Bean
//    ApplicationListener<AfterDeleteEvent> deleteEventApplicationListener() {
//        return new ApplicationListener<AfterDeleteEvent>() {
//            @Override
//            public void onApplicationEvent(AfterDeleteEvent event) {
//                Object entity = (Object) event.getEntity();
//                if (entity instanceof Node){
//                    graphIdFactory.delete(((Node) entity).getId());
//                }else if (entity instanceof Edge){
//                    graphIdFactory.delete(((Edge) entity).getId());
//                }
//            }
//        };
//    }


    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {
        org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
        config
                .driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
                .setURI("http://"+hosts+":"+port);
        return config;
    }

    @Override
    @Bean
    public SessionFactory getSessionFactory() {
        // with domain entity base package(s)
        return new SessionFactory(getConfiguration(),"org.epnoi.storage.system.graph.domain");
    }

    // needed for session in view in web-applications
//    @Bean
//    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public Session getSession() throws Exception {
//        return super.getSession();
//    }

    @Override
    @Bean
    @Scope(value = BeanDefinition.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
//    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
//    @Scope(value = "application", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session getSession() throws Exception {
        Session session = super.getSession();
        return session;
    }


    @Bean
    public ConversionService springConversionService() {
        return new MetaDataDrivenConversionService(getSessionFactory().metaData());
    }


    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
