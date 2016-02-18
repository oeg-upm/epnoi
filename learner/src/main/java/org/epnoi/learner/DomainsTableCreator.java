package org.epnoi.learner;

import org.epnoi.learner.helper.LearningHelper;
import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class DomainsTableCreator {
	private static final Logger LOG = LoggerFactory.getLogger(DomainsTableCreator.class);

	private List<String> consideredDomains;
	private String targetDomain;

	private LearningHelper helper;
	private DomainsTable domainsTable;

	public void init(LearningHelper helper, List<String> considered, String target) {
		LOG.info("Initializing the DomainsTableCreator for the following domains: " + considered + " and the target:" + target);
		LOG.info(helper.toString());
		this.helper = helper;
		this.consideredDomains = considered;
		this.domainsTable = new DomainsTable();
		this.targetDomain = target;
	}

	public DomainsTable create() {
		LOG.info("Creating the DomainsTable");
		this.consideredDomains.forEach(this::addDomainToTable);
		this.domainsTable.setTargetDomain(targetDomain);
		return this.domainsTable;
	}

	public DomainsTable create(String domainUri) {
		addDomainToTable(domainUri);
		this.domainsTable.setTargetDomain(targetDomain);
		return this.domainsTable;
	}

	private void addDomainToTable(String domainUri){

		Optional<Resource> res = helper.getUdm().read(Resource.Type.DOMAIN).byUri(domainUri);

		if (!res.isPresent()){
			LOG.warn("No domain found with uri: " + domainUri);
			return;
		}

		Domain domain = (Domain) res.get();
		this.domainsTable.addDomain(domain);
		LOG.info("Adding the domain " + domain);
		List<String> foundURIs = helper.getUdm().find(Resource.Type.DOCUMENT).in(Resource.Type.DOMAIN, domain.getUri());
		LOG.info("Found initially " + foundURIs.size() + " elements in the domain");
		this.domainsTable.addDomainResources(domain.getUri(), foundURIs);

	}

}
