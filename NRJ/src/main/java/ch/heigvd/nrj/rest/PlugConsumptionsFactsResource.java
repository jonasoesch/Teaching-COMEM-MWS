package ch.heigvd.nrj.rest;

import ch.heigvd.nrj.exceptions.EntityNotFoundException;
import ch.heigvd.nrj.model.PlugConsumptionFact;
import ch.heigvd.nrj.services.crud.PlugConsumptionsFactsManagerLocal;
import ch.heigvd.nrj.services.to.PlugConsumptionsFactsTOServiceLocal;
import ch.heigvd.nrj.to.PublicPlugConsumptionFactsTO;
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
 * This is the REST API endpoint for the PlugConsumptionFact resource. When REST
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
@Path("plugConsumptionsFacts")
public class PlugConsumptionsFactsResource {

    @Context
    private UriInfo context;
    @EJB
    PlugConsumptionsFactsManagerLocal plugConsumptionsFactsManager;
    @EJB
    PlugConsumptionsFactsTOServiceLocal plugConsumptionsFactsTOService;

    /**
     * Creates a new instance of ConsumptionsResource
     */
    public PlugConsumptionsFactsResource() {
    }

    /**
     * Creates a new Consumption resource from the provided representation
     *
     * @param newPlugConsumptionObsTO
     * @return an instance of PublicPlugConsumptionObsTO
     */
    @POST
    @Consumes({"application/json"})
    public Response createResource(PublicPlugConsumptionFactsTO newPlugConsumptionFactTO) {
        PlugConsumptionFact newPlugConsumptionFact = new PlugConsumptionFact();
        plugConsumptionsFactsTOService.updatePlugConsumptionFactEntity(newPlugConsumptionFact, newPlugConsumptionFactTO);
        long newPlugConsumptionFactId = plugConsumptionsFactsManager.create(newPlugConsumptionFact);
        URI createdURI = context.getAbsolutePathBuilder().path(Long.toString(newPlugConsumptionFactId)).build();
        return Response.created(createdURI).build();
    }

    /**
     * Retrieves a representation of a list of PlugConsumptionFact resources
     *
     * @return an instance of PublicPlugTO
     */
    @GET
    @Produces({"application/json", "application/xml"})
    public List<PublicPlugConsumptionFactsTO> getResourceList() {
        List<PlugConsumptionFact> plugConsumptionsFacts = plugConsumptionsFactsManager.findAll();
        List<PublicPlugConsumptionFactsTO> result = new LinkedList<>();
        for (PlugConsumptionFact plugConsumptionFact : plugConsumptionsFacts) {
            result.add(plugConsumptionsFactsTOService.buildPublicPlugConsumptionFactTO(plugConsumptionFact));
        }
        return result;
    }

    /**
     * Retrieves a representation of an PlugConsumptionFact resource
     *
     * @param id this id of the consumption
     * @return an instance of PublicPlugConsumptionFactTO
     * @throws ch.heigvd.nrj.exceptions.EntityNotFoundException
     */
    @GET
    @Path("{id}")
    @Produces({"application/json", "application/xml"})
    public PublicPlugConsumptionFactsTO getResource(@PathParam("id") long id) throws EntityNotFoundException {
        PlugConsumptionFact plugConsumptionFacts = plugConsumptionsFactsManager.findById(id);
        PublicPlugConsumptionFactsTO plugConsumptionTO = plugConsumptionsFactsTOService.buildPublicPlugConsumptionFactTO(plugConsumptionFacts);
        return plugConsumptionTO;
    }

    /**
     * Updates a PlugConsumptionsFact resource
     *
     * @param id this id of the consumption
     * @param updatedPlugConsumptionFactTO a TO containing the consumption data
     * @return an instance of PublicConsumptionTO
     * @throws ch.heigvd.skeleton.exceptions.EntityNotFoundException
     */
    @PUT
    @Path("{id}")
    @Consumes({"application/json"})
    public Response Resource(PublicPlugConsumptionFactsTO updatedPlugConsumptionFactTO, @PathParam("id") long id) throws EntityNotFoundException {
        PlugConsumptionFact plugConsumptionFactToUpdate = plugConsumptionsFactsManager.findById(id);
        plugConsumptionsFactsTOService.updatePlugConsumptionFactEntity(plugConsumptionFactToUpdate, updatedPlugConsumptionFactTO);
        plugConsumptionsFactsManager.update(plugConsumptionFactToUpdate);
        return Response.ok().build();
    }

    /**
     * Deletes a FactConsumptionsFact resource
     *
     * @param id this id of the consumption
     * @return an instance of PublicConsumptionTO
     * @throws ch.heigvd.skeleton.exceptions.EntityNotFoundException
     */
    @DELETE
    @Path("{id}")
    public Response deleteResource(@PathParam("id") long id) throws EntityNotFoundException {
        plugConsumptionsFactsManager.delete(id);
        return Response.ok().build();
    }
}
