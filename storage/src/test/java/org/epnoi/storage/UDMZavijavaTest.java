package org.epnoi.storage;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.Event;
import org.epnoi.model.WikidataView;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.*;
import org.epnoi.model.modules.BindingKey;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusSubscriber;
import org.epnoi.model.modules.RoutingKey;
import org.epnoi.storage.generator.URIGenerator;
import org.epnoi.storage.system.document.domain.WordDocument;
import org.epnoi.storage.system.document.repository.WordDocumentRepository;
import org.epnoi.storage.system.graph.repository.edges.DealsWithFromDocumentEdgeRepository;
import org.epnoi.storage.system.graph.repository.nodes.DocumentGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.DomainGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.SourceGraphRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.neo4j.ogm.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cbadenes on 01/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "epnoi.cassandra.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.cassandra.port = 5011",
        "epnoi.cassandra.keyspace = research",
        "epnoi.elasticsearch.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.elasticsearch.port = 5021",
        "epnoi.neo4j.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.neo4j.port = 5030",
        "epnoi.eventbus.uri = amqp://epnoi:drinventor@zavijava.dia.fi.upm.es:5041/drinventor"})
public class UDMZavijavaTest {

    private static final Logger LOG = LoggerFactory.getLogger(UDMZavijavaTest.class);

    @Autowired
    UDM udm;


    @Test
    public void fix(){

        String sourceUri = "http://epnoi.org/sources/75b7c67d-f9e1-4b7d-8e96-543bc621d3a3";

        Domain domain = Resource.newDomain();
        domain.setName("siggraph");
        domain.setDescription("siggraph corpus");
        udm.save(domain);

        udm.save(Relation.newComposes(sourceUri,domain.getUri()));


        List<String> documents = Arrays.asList(new String[]{"http://epnoi.org/documents/736fe1a9-75e4-4ceb-a3d8-f90b3d92df09",
                "http://epnoi.org/documents/abeaf5f4-d556-4403-96bf-05a906096087",
                "http://epnoi.org/documents/b358c685-92e1-4b37-8715-411352844f3e",
                "http://epnoi.org/documents/bb321c85-1126-4e40-b3ae-c683fce0eaa3",
                "http://epnoi.org/documents/968bb76e-3266-4450-b4a0-619f069b1d84",
                "http://epnoi.org/documents/f12ac9b3-cd37-4592-b210-0a3fea10c7f4",
                "http://epnoi.org/documents/b11253d1-a9f8-4d99-a039-893441f26976",
                "http://epnoi.org/documents/f8280f55-689e-4baa-9657-ce1427b7d691",
                "http://epnoi.org/documents/8fdbcb9d-bb44-406c-8f36-2ab1ea8abfad",
                "http://epnoi.org/documents/65424a97-8283-432f-bdec-27bd2a9c6050",
                "http://epnoi.org/documents/24ad62bf-e24e-407c-a6de-1e734055652f",
                "http://epnoi.org/documents/de01b537-e1f6-4725-a7cf-62062ce38e06",
                "http://epnoi.org/documents/960d6bd5-a813-4c36-bbc4-150ce72f6cee",
                "http://epnoi.org/documents/dcc3487f-44c2-423e-9ee5-493283826339",
                "http://epnoi.org/documents/1601ef96-8541-46ec-9de5-0c270486adc6",
                "http://epnoi.org/documents/93695456-699e-483f-8e6e-2f317d77903c",
                "http://epnoi.org/documents/76117666-54ac-4431-b017-c8c9f01e5b7a",
                "http://epnoi.org/documents/bec57928-8894-4b8c-b622-d6d5832ef4af",
                "http://epnoi.org/documents/22a082f1-823e-4cca-84cd-7e89b32285b9",
                "http://epnoi.org/documents/752f68ce-4218-4776-a8f5-265b4a40c882",
                "http://epnoi.org/documents/b66c86f0-040a-4aa3-af16-c9ba6fc908af",
                "http://epnoi.org/documents/c64db0f9-6cf4-4b78-af86-c177ac9af5da",
                "http://epnoi.org/documents/3c6d7162-9ea8-4c18-9d22-be8475a13f93",
                "http://epnoi.org/documents/404a36e6-4723-4418-a59d-fedc1aba5e88",
                "http://epnoi.org/documents/7b153949-af7e-4dde-9fab-1911aa90994c",
                "http://epnoi.org/documents/9f850db4-7792-40f2-aaa0-630615c8a25b",
                "http://epnoi.org/documents/1dca864e-6742-47a2-a88e-5a177e6308d3",
                "http://epnoi.org/documents/ec409090-44d5-4a2e-a73d-b8a732c036e5",
                "http://epnoi.org/documents/65701510-e525-41e4-9743-e0cc54932b7d",
                "http://epnoi.org/documents/e073d7eb-69c6-46e0-9835-39fa456e0985",
                "http://epnoi.org/documents/2cdfb106-6a7e-40ee-b992-50b80f3d4302",
                "http://epnoi.org/documents/60c3b0a5-e535-4490-89d9-6107b0402bcd",
                "http://epnoi.org/documents/073d0e16-9498-4098-b962-ba2e7590e8ab",
                "http://epnoi.org/documents/d3424538-c596-4d25-a8ab-34665d8aece2",
                "http://epnoi.org/documents/eeaf9441-9d90-4c2f-a1e1-9cb0c76cdd17",
                "http://epnoi.org/documents/0cb2d26b-08e3-4593-88e3-3e6c7093bf90",
                "http://epnoi.org/documents/c058923a-1660-49d0-b324-ec66f9474fb3",
                "http://epnoi.org/documents/2e24b518-605c-40e5-9976-d191346dcc12",
                "http://epnoi.org/documents/63b33f02-6df0-4387-9288-89ea36baab78",
                "http://epnoi.org/documents/e506836c-ac56-4890-9b8e-d3985260f485",
                "http://epnoi.org/documents/98e3573b-0b38-4ef4-bd74-900cca950bca",
                "http://epnoi.org/documents/7b695457-8699-4b30-9022-c907211c1210",
                "http://epnoi.org/documents/3ac54c93-ce0d-4715-8378-fd9c785af1a7",
                "http://epnoi.org/documents/b8290705-159e-4c3c-9b7d-e2da70a55a5a",
                "http://epnoi.org/documents/0c1e8633-8427-423e-a44b-37e7b18c1736",
                "http://epnoi.org/documents/deb9ff96-5071-4296-8b65-b20d0a829fd4",
                "http://epnoi.org/documents/4e6bc4b6-ce14-49fe-9e60-a3a2d291ea1c",
                "http://epnoi.org/documents/b35a2fee-4d30-42a5-b23c-76d116a13d7d",
                "http://epnoi.org/documents/8a2dd258-8b78-4be3-8a1d-01955e3ddf24",
                "http://epnoi.org/documents/0fbf60e7-9ad7-4721-b502-2bf87a481e21",
                "http://epnoi.org/documents/3de8229c-c91f-41a5-9d77-6f47728e5d27",
                "http://epnoi.org/documents/048a8724-e969-481a-9906-dec5b702bca3",
                "http://epnoi.org/documents/472a53f8-65d8-4981-a361-20c628b24ab0",
                "http://epnoi.org/documents/42806a0c-0217-4d00-b058-c1c55834fd53",
                "http://epnoi.org/documents/f5233113-965c-408f-8f33-5c56a01e5446",
                "http://epnoi.org/documents/b4064de6-2f99-4496-8182-1e33427b0977",
                "http://epnoi.org/documents/1839439f-5e5f-4f39-b4ef-0b46b063a46d",
                "http://epnoi.org/documents/b8eb8771-f4fc-43c2-8ebe-bfbd3649cdde",
                "http://epnoi.org/documents/418779f3-ffe2-4bc8-96e4-29308368e18a",
                "http://epnoi.org/documents/b1d314c2-59c1-4fd1-9917-0b63e65a297f",
                "http://epnoi.org/documents/4b38ea00-d042-4eef-a483-265060d72dfe",
                "http://epnoi.org/documents/58d7890d-45ca-4cc7-9410-9ee4217ef104",
                "http://epnoi.org/documents/99481f7b-c88c-409c-a090-fff142ba828d",
                "http://epnoi.org/documents/cf28b6a0-56fd-483a-9f79-781014264548",
                "http://epnoi.org/documents/e805ee3d-3755-4063-a2db-f9895d5d46b0",
                "http://epnoi.org/documents/8b8a7ce0-269b-4534-8a92-0fe980630f5c",
                "http://epnoi.org/documents/e5f0eab3-f55a-4891-8ee2-6ade3edee838",
                "http://epnoi.org/documents/0e8567d9-f7fc-4946-9feb-9dafbac2ec53",
                "http://epnoi.org/documents/fe07dd3e-09b8-4e7a-bb20-9d87a2baa2ed",
                "http://epnoi.org/documents/d5e3595b-c27b-4392-bf18-c64e637c7d7b",
                "http://epnoi.org/documents/87d0a68a-7d91-45ee-a41f-cfbc2c5c00fb",
                "http://epnoi.org/documents/a04c6cf5-2cc4-4087-8a47-7c9e44e50ff8",
                "http://epnoi.org/documents/919a58e6-2c77-49fc-9aa7-a9c31824129e",
                "http://epnoi.org/documents/71f7c90f-830a-4de9-9c61-232621aa78b8",
                "http://epnoi.org/documents/2b8f33ac-173f-4956-b6c2-c300c8f4f55c",
                "http://epnoi.org/documents/a2dd73b0-a5d5-401e-a7f8-43f6cc5c341c",
                "http://epnoi.org/documents/a36fd381-ac0d-4417-b559-150b584c93e2",
                "http://epnoi.org/documents/0dcb543e-4120-4219-a633-d49806d7b650",
                "http://epnoi.org/documents/8d77c062-63c5-4463-8c8e-6af9d20b9221",
                "http://epnoi.org/documents/9f57fe35-a3e1-4e51-88e1-b9b0732a7b90",
                "http://epnoi.org/documents/d02d6ba1-5b35-4e57-b30e-cc199fcb6a9e",
                "http://epnoi.org/documents/d2cee936-5008-45a8-83d4-0cd58243d9d8",
                "http://epnoi.org/documents/9a143983-38a5-4dc9-b6a4-f4b5301ab705",
                "http://epnoi.org/documents/1c86516d-c1c8-4c2f-b282-87bb5a0fbe74",
                "http://epnoi.org/documents/85eb0ea7-6703-439d-bb7a-f42a75243cfc",
                "http://epnoi.org/documents/fb79c9f8-c732-424c-90c7-b3c1caa15aeb"});

        for (String uri: documents){
            udm.save(Relation.newContains(domain.getUri(),uri));
        }
    }

    @Test
    public void newTopicModel(){

        String domain = "http://epnoi.org/domains/ce123683-512a-4f2a-a539-c77b666a8b79";

        Analysis analysis = Resource.newAnalysis();
        analysis.setDomain(domain);
        analysis.setConfiguration("sample");
        analysis.setDescription("Topic Model");
        analysis.setType("topic-model");
        udm.save(analysis);
    }

}
