package ch.heigvd.nrj.rest;

import ch.heigvd.nrj.exceptions.EntityNotFoundException;
import ch.heigvd.nrj.model.ConsumptionObs;
import ch.heigvd.nrj.services.business.StreamProcessorLocal;
import ch.heigvd.nrj.services.crud.ConsumptionsObsManagerLocal;
import ch.heigvd.nrj.services.crud.PlugsManagerLocal;
import ch.heigvd.nrj.services.to.ConsumptionsObsTOServiceLocal;
import ch.heigvd.nrj.services.to.PlugsTOServiceLocal;
import ch.heigvd.nrj.to.PublicConsumptionObsTO;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * This is the REST API endpoint for the Consumptions resource. When REST
 * clients send HTTP requests, they will be routed to this class (because of the
 *
 * @Path annotation). They will then be routed to the appropriate methods
 * (because of the
 * @GET,
 * @POST and other annotations).
 *
 * @author nicolas
 */
@Stateless
@Path("consumptionsObs")
public class ConsumptionsObsResource {

    @Context
    private UriInfo context;
    @EJB
    ConsumptionsObsManagerLocal consumptionsManager;
    @EJB
    ConsumptionsObsTOServiceLocal consumptionsTOService;
    @EJB
    PlugsManagerLocal plugsManager;
    @EJB
    StreamProcessorLocal streamProcessor;
    @EJB
    PlugsTOServiceLocal plugsTOService;

    /**
     * Creates a new instance of ConsumptionsResource
     */
    public ConsumptionsObsResource() {
    }

    /**
     * Creates a new ConsumptionObs resource from the provided representation
     *
     *
     * @return an instance of PublicConsumptionObsTO
     */
    @POST
    @Consumes({"application/json"})
    public Response createResource(PublicConsumptionObsTO newConsumptionTO) {
	ConsumptionObs newConsumption = new ConsumptionObs();
	consumptionsTOService.updateConsumptionObsEntity(newConsumption, newConsumptionTO);
	//newConsumption = consumptionsTOService.buildPublicConsumptionObsTO(newConsumption);
	System.out.println("coco2" + newConsumption);
	long newConsId = consumptionsManager.create(newConsumption);
	System.out.println("coco3" + newConsumption);
	URI createdURI = context.getAbsolutePathBuilder().path(Long.toString(newConsId)).build();
	streamProcessor.onConsumption(newConsumption);

	// Si la plug pour cette Consommation n'existe pas en BD
	if (newConsumption.getPlug() == null) {
	    return Response.status(500).build();
	} else {
	    //return Response.ok().build();
	    return Response.created(createdURI).build();
	}

    }

    /**
     * Retrieves a representation of a list of Plug resources
     *
     * @return an instance of PublicPlugTO
     */
    @GET
    @Produces({"application/json"})
    public List<PublicConsumptionObsTO> getResourceList() {
	List<ConsumptionObs> consumptionsObs = consumptionsManager.findAll();
	List<PublicConsumptionObsTO> result = new LinkedList<>();
	for (ConsumptionObs co : consumptionsObs) {
	    PublicConsumptionObsTO o = consumptionsTOService.buildPublicConsumptionObsTO(co);
	    // PublicConsumptionObsTO o = new PublicConsumptionObsTO();

	    result.add(o);
	}
	/*System.out.println(result);*/
	/*List<PublicConsumptionObsTO> result2 = new LinkedList<PublicConsumptionObsTO>();
	 PublicConsumptionObsTO o = new PublicConsumptionObsTO();
	 result.add(o);*/
	return result;
    }

    /**
     * Retrieves representation of an ConsumptionsObs resource
     *
     * @param id this id of the consumption
     * @return an instance of PublicConsumptionObsTO
     * @throws ch.heigvd.skeleton.exceptions.EntityNotFoundException
     */
    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public PublicConsumptionObsTO getResource(@PathParam("id") long id) throws EntityNotFoundException {
	ConsumptionObs consumption = consumptionsManager.findById(id);
	PublicConsumptionObsTO consumptionTO = consumptionsTOService.buildPublicConsumptionObsTO(consumption);
	System.out.println(consumptionTO);
	return consumptionTO;
    }

    /**
     * Updates an ConsumptionsObs resource
     *
     * @param id this id of the consumption
     * @param updatedConsumptionTO a TO containing the consumption data
     * @return an instance of PublicConsumptionTO
     * @throws ch.heigvd.skeleton.exceptions.EntityNotFoundException
     */
    @PUT
    @Path("{id}")
    @Consumes({"application/json"})
    public Response Resource(PublicConsumptionObsTO updatedConsumptionObsTO, @PathParam("id") long id) throws EntityNotFoundException {
	ConsumptionObs consumptionObsToUpdate = consumptionsManager.findById(id);
	consumptionsTOService.updateConsumptionObsEntity(consumptionObsToUpdate, updatedConsumptionObsTO);
	consumptionsManager.update(consumptionObsToUpdate);
	return Response.ok().build();
    }

    /**
     * Deletes an ConsumptionsObs resource
     *
     * @param id this id of the consumption
     * @return an instance of PublicConsumptionTO
     * @throws ch.heigvd.skeleton.exceptions.EntityNotFoundException
     */
    @DELETE
    @Path("{id}")
    public Response deleteResource(@PathParam("id") long id) throws EntityNotFoundException {
	consumptionsManager.delete(id);
	return Response.ok().build();
    }
}