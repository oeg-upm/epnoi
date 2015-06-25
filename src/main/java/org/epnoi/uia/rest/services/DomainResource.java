package org.epnoi.uia.rest.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.epnoi.model.Domain;
import org.epnoi.model.DublinCoreMetadataElementsSetHelper;
import org.epnoi.model.ResearchObject;
import org.epnoi.uia.informationstore.dao.rdf.RDFHelper;

import com.sun.jersey.api.Responses;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/uia/domains/domain")
@Api(value = "/uia/domains/domain", description = "Operations for handling a domain")
public class DomainResource extends UIAService {
	private static final String LABEL_PROPERTY = "label";
	private static final String EXPRESSION_PROPERTY = "expression";
	private static final String TYPE_PROPERTY = "type";

	private static final String resourcesPathSubfix = "/resources";

	@Context
	ServletContext context;

	// ----------------------------------------------------------------------------------------
	@PostConstruct
	public void init() {
		logger = Logger.getLogger(DomainResource.class.getName());
		logger.info("Initializing DomainResource");
		this.core = this.getUIACore();

	}

	// -----------------------------------------------------------------------------------------

	@PUT
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Creates an empty domain", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The domain has been created"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA") })
	public Response createDomain(
			@ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String newDomainURI) {
		logger.info("PUT domain > " + newDomainURI);

		URI domainURI = null;
		try {
			domainURI = new URI(newDomainURI);
		} catch (URISyntaxException e) {
			throw new WebApplicationException();
		}

		// We create the new empty domain, just with its URI and a reference to
		// the research object
		Domain domain = new Domain();
		domain.setURI(newDomainURI);
		domain.setResources(newDomainURI + resourcesPathSubfix);

		// We create an empty research object
		ResearchObject resources = new ResearchObject();
		resources.setURI(newDomainURI + resourcesPathSubfix);

		this.core.getInformationHandler().put(resources,
				org.epnoi.model.Context.getEmptyContext());

		this.core.getInformationHandler().put(domain,
				org.epnoi.model.Context.getEmptyContext());

		return Response.created(domainURI).build();
	}

	// -----------------------------------------------------------------------------------------

	@PUT
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Stores a domain", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The domain has been created"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA") })
	public Response createDomain(Domain domain) {
		logger.info("PUT Domain > " + domain);

		URI domainURI = null;
		try {
			domainURI = new URI(domain.getURI());
		} catch (URISyntaxException e) {
			throw new WebApplicationException();
		}

		// We create an empty research object
		ResearchObject resources = new ResearchObject();
		resources.setURI(domain.getResources());

		this.core.getInformationHandler().put(resources,
				org.epnoi.model.Context.getEmptyContext());

		this.core.getInformationHandler().put(domain,
				org.epnoi.model.Context.getEmptyContext());

		return Response.created(domainURI).build();
	}

	// -----------------------------------------------------------------------------------------

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The domain has been successfully retrieved"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A domain with such URI could not be found") })
	@ApiOperation(value = "Returns the domain with the provided URI", notes = "", response = Domain.class)
	public Response getResearchObject(
			@ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String uri) {

		logger.info("GET uri=" + uri);

		Domain domain = (Domain) core.getInformationHandler().get(uri,
				RDFHelper.DOMAIN_CLASS);

		if (domain != null) {

			return Response.ok(domain, MediaType.APPLICATION_JSON).build();
		}
		return Response.status(Responses.NOT_FOUND).build();

	}

	// -----------------------------------------------------------------------------------------

	@POST
	@Path("/resources")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Adds a resource to the domain", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The resource has been add to the domain"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A domain with such URI could not be found") })
	public Response addAggregatedResource(
			@ApiParam(value = "Research Object uri", required = true, allowMultiple = false) @QueryParam("uri") String URI,
			@ApiParam(value = "Resource to be aggregated to the Research Object", required = true, allowMultiple = false) @QueryParam("resourceuri") String resourceURI) {
		logger.info("POST /resources uri" + URI + " resourceuri " + resourceURI);

		Domain domain = (Domain) core.getInformationHandler().get(URI,
				RDFHelper.DOMAIN_CLASS);

		if (domain != null) {
			ResearchObject researchObject = (ResearchObject) core
					.getInformationHandler().get(URI + resourcesPathSubfix,
							RDFHelper.RESEARCH_OBJECT_CLASS);

			if (researchObject != null) {
				if (!researchObject.getAggregatedResources().contains(
						resourceURI)) {
					researchObject.getAggregatedResources().add(resourceURI);
					core.getInformationHandler().update(researchObject);
				}
			}

			return Response.ok().build();
		} else {
			return Response.status(Responses.NOT_FOUND).build();
		}

	}

	// -----------------------------------------------------------------------------------------

	@POST
	@Path("/properties/{PROPERTY}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Sets a domain property", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The domain property has been updated"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A domain with such URI, or a property with such could not be found") })
	public Response updateDCProperty(
			@ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String URI,
			@ApiParam(value = "Domain property name", required = true, allowMultiple = false, allowableValues = "label,type,expression,title,description,date,creator") @PathParam("PROPERTY") String propertyName,
			@ApiParam(value = "Domain property value", required = true, allowMultiple = false) @QueryParam("value") String value) {
		logger.info("POST /properties/" + propertyName + " domain " + URI
				+ "value " + value);
		System.out.println("Updating the property "
				+ DublinCoreMetadataElementsSetHelper
						.getPropertyURI(propertyName) + " with value " + value);

		String propertyURI = DublinCoreMetadataElementsSetHelper
				.getPropertyURI(propertyName);
		// If the propertyName is among considered dublin core properties, the
		// returned URI should not be null
		if (propertyURI != null) {
			// We are updating a dublin core property of the research object
			// that represents the domain resources
			_updateResourcesProperty(URI, propertyURI, value);

		} else {
			// We must be updating a property of the domain
			_updateDomainProperty(URI, propertyName, value);
		}

		return Response.ok().build();

	}

	// -----------------------------------------------------------------------------------------

	private void _updateResourcesProperty(String URI, String propertyURI,
			String value) {
		ResearchObject researchObject = (ResearchObject) core
				.getInformationHandler().get(URI + resourcesPathSubfix,
						RDFHelper.RESEARCH_OBJECT_CLASS);

		researchObject.getDcProperties().addPropertyValue(propertyURI, value);
		this.core.getInformationHandler().update(researchObject);
	}

	// -----------------------------------------------------------------------------------------

	private void _updateDomainProperty(String URI, String propertyName,
			String value) {
		Domain domain = (Domain) core.getInformationHandler().get(URI,
				RDFHelper.RESEARCH_OBJECT_CLASS);

		switch (propertyName) {
		case DomainResource.EXPRESSION_PROPERTY:
			domain.setExpression(value);
			break;
		case DomainResource.LABEL_PROPERTY:
			domain.setLabel(value);
			break;
		case DomainResource.TYPE_PROPERTY:
			domain.setType(value);
			break;
		default:
		}
		this.core.getInformationHandler().update(domain);
	}

	// -----------------------------------------------------------------------------------------

	@DELETE
	@Path("")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Removes Domain", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The domain with such URI has been deleted"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "A domain with such URI could not be found") })
	public Response removeResearchObject(
			@ApiParam(value = "Domain uri", required = true, allowMultiple = false) @QueryParam("uri") String URI) {
		logger.info("DELETE > " + URI);

		Domain researchObject = (Domain) core.getInformationHandler().get(URI,
				RDFHelper.DOMAIN_CLASS);
		if (researchObject != null) {
			this.core.getInformationHandler().remove(URI,
					RDFHelper.DOMAIN_CLASS);
			return Response.ok().build();
		} else {
			return Response.status(Responses.NOT_FOUND).build();
		}

	}

	// -----------------------------------------------------------------------------------------

	@DELETE
	@Path("/resources")
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Removes an aggregated resource from a Researh Object", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "The resource has been deleted from the Domain"),
			@ApiResponse(code = 500, message = "Something went wrong in the UIA"),
			@ApiResponse(code = 404, message = "Either a Domain or the an aggregated resource with such URI could not be found") })
	public Response removeAggregatedResource(
			@ApiParam(value = "Domain URI", required = true, allowMultiple = false) @QueryParam("uri") String URI,
			@ApiParam(value = "Aggregated resource URI to be deleted", required = true, allowMultiple = false) @QueryParam("resourceuri") String resourceURI) {

		if (core.getInformationHandler().contains(URI, RDFHelper.DOMAIN_CLASS)) {

			ResearchObject researchObject = (ResearchObject) core
					.getInformationHandler().get(URI + resourcesPathSubfix,
							RDFHelper.RESEARCH_OBJECT_CLASS);
			if (researchObject != null
					&& researchObject.getAggregatedResources().contains(
							resourceURI)) {
				researchObject.getAggregatedResources().remove(resourceURI);
				this.core.getInformationHandler().update(researchObject);
				return Response.ok().build();
			} else {

				return Response.status(Responses.NOT_FOUND).build();
			}
		} else {

			return Response.status(Responses.NOT_FOUND).build();
		}
	}
}
