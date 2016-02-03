package org.epnoi.model;

import org.epnoi.model.domain.Resource;

public class ExternalResource extends Resource {

	private String uri;
	private String description;



	// ------------------------------------------------------------------------------------------

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDescription() {
		return description;
	}

	// ------------------------------------------------------------------------------------------

	public void setDescription(String description) {
		this.description = description;
	}

	// ------------------------------------------------------------------------------------------

	public String toString() {
		return "ER[URI: " + this.uri + " , description: " + this.description
				+ "]";
	}

}
