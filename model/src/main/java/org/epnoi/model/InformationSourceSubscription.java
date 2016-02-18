package org.epnoi.model;

import org.epnoi.model.domain.resources.Resource;

import java.util.ArrayList;
import java.util.List;

public class InformationSourceSubscription extends Resource {

	String uri;
	String informationSource;
	List<String> keywords = new ArrayList<String>();
	Integer numberOfItems;

	// -----------------------------------------------------------------------------

	public Integer getNumberOfItems() {
		return numberOfItems;
	}

	public void setNumberOfItems(Integer numberOfResults) {
		this.numberOfItems = numberOfResults;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getInformationSource() {
		return informationSource;
	}

	// -----------------------------------------------------------------------------

	public void setInformationSource(String informationSource) {
		this.informationSource = informationSource;
	}

	// -----------------------------------------------------------------------------
	
	public String getUri() {
		return uri;
	}

	// -----------------------------------------------------------------------------

	public void setUri(String uri) {
		this.uri = uri;
	}

	// -----------------------------------------------------------------------------

	@Override
	public String toString() {
		return "InformationSourceSubscription [URI=" + uri
				+ ", informationSource=" + informationSource + ", keywords="
				+ keywords + ", numberOfResults=" + numberOfItems + "]";
	}

	@Override
	public Type getResourceType() {
		return null;
	}

	// -----------------------------------------------------------------------------

}
