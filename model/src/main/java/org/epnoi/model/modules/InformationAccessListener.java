package org.epnoi.model.modules;

import org.epnoi.model.domain.Resource;

public interface InformationAccessListener {
	public void notify(String eventType, Resource resource);
}
