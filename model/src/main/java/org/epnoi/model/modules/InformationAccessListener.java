package org.epnoi.model.modules;

import org.epnoi.model.domain.resources.Resource;

public interface InformationAccessListener {
	public void notify(String eventType, Resource resource);
}
