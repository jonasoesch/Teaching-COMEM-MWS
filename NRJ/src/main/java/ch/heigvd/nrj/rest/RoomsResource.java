package ch.heigvd.nrj.rest;

import ch.heigvd.nrj.exceptions.EntityNotFoundException;
import ch.heigvd.nrj.model.Plug;
import ch.heigvd.nrj.model.Room;
import ch.heigvd.nrj.services.crud.PlugsManagerLocal;
import ch.heigvd.nrj.services.crud.RoomsManagerLocal;
import ch.heigvd.nrj.services.to.PlugsTOServiceLocal;
import ch.heigvd.nrj.services.to.RoomsTOServiceLocal;
import ch.heigvd.nrj.to.PublicPlugTO;
import ch.heigvd.nrj.to.PublicRoomTO;

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
 * This is the REST API endpoint for the Rooms resource. When REST clients send
 * HTTP requests, they will be routed to this class (because of the
 *
 * @Path annotation). They will then be routed to the appropriate methods
 * (because of the
 * @GET,
 * @POST and other annotations).
 *
 * @author nicolas
 */
@Stateless
@Path("rooms")
public class RoomsResource {

    @Context
    private UriInfo context;
    @EJB
    RoomsManagerLocal roomsManager;
    @EJB
    RoomsTOServiceLocal roomsTOService;
    @EJB
    PlugsManagerLocal plugsManager;
    @EJB
    PlugsTOServiceLocal plugsTOService;

    /**
     * Creates a new instance of RoomsResource
     */
    public RoomsResource() {
    }

    /**
     * Creates a new Room resource from the provided representation
     *
     * @return an instance of PublicRoomTO
     */
    @POST
    @Consumes({"application/json"})
    public Response createResource(PublicRoomTO newRoomTO) {
        Room newRoom = new Room();
        roomsTOService.updateRoomEntity(newRoom, newRoomTO);
        long newRoomId = this.roomsManager.create(newRoom);
        URI createdURI = context.getAbsolutePathBuilder().path(Long.toString(newRoomId)).build();
        return Response.created(createdURI).build();
    }

    /**
     * Retrieves a representation of a list of Room
     *
     * @return an instance of PublicRoomTO
     */
    @GET
    @Produces({"application/json"})
    public List<PublicRoomTO> getResourceList() {
        List<Room> rooms = roomsManager.findAll();
        List<PublicRoomTO> result = new LinkedList<>();
        for (Room room : rooms) {
            result.add(roomsTOService.buildPublicRoomTO(room));
        }
        return result;
    }

    /**
     * Retrieves representation of a Room resource
     *
     * @param id this id of the Room
     * @return an instance of PublicRoomTO
     * @throws ch.heigvd.skeleton.exceptions.EntityNotFoundException
     */
    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public PublicRoomTO getResource(@PathParam("id") long id) throws EntityNotFoundException {
        Room room = roomsManager.findById(id);
        PublicRoomTO roomTO = roomsTOService.buildPublicRoomTO(room);
        return roomTO;
    }

    /**
     * Updates a Room resource
     *
     * @param id this id of the employee
     * @param updatedRoomTO a TO containing the employee data
     * @return an instance of PublicRoomTO
     * @throws ch.heigvd.skeleton.exceptions.EntityNotFoundException
     */
    @PUT
    @Path("{id}")
    @Consumes({"application/json"})
    public Response Resource(PublicRoomTO updatedRoomTO, @PathParam("id") long id) throws EntityNotFoundException {
        Room roomToUpdate = roomsManager.findById(id);
        roomsTOService.updateRoomEntity(roomToUpdate, updatedRoomTO);
        roomsManager.update(roomToUpdate);
        return Response.ok().build();
    }

    /**
     * Deletes a Room resource
     *
     * @param id this id of the employee
     * @return an instance of PublicRoomTO
     * @throws ch.heigvd.skeleton.exceptions.EntityNotFoundException
     */
    @DELETE
    @Path("{id}")
    public Response deleteResource(@PathParam("id") long id) throws EntityNotFoundException {
        roomsManager.delete(id);
        return Response.ok().build();
    }

    /**
     * Retrieves a representation of a List of Plugs of this Room
     *
     * @param id
     * @return a List of Plugs of this Room
     * @throws EntityNotFoundException
     */
    @GET
    @Path("{id}/plugs")
    @Produces({"application/json"})
    public List<PublicPlugTO> getRoomPlugs(@PathParam("id") long id) throws EntityNotFoundException {
        Room room = roomsManager.findById(id);
        List<Plug> plugs = room.getPlugs();
        List<PublicPlugTO> result = new LinkedList<>();
        for (Plug plug : plugs) {
            result.add(plugsTOService.buildPublicPlugTO(plug));
        }
        return result;
    }

    /**
     * Adds a List of Plugs to this Room
     *
     * @param id
     * @param newPlugTO
     * @return
     */
    @POST
    @Path("{id}/plugs")
    @Consumes({"application/json"})
    public Response createPlugInRoom(@PathParam("id") long id, PublicPlugTO newPlugTO) {
        PublicRoomTO r = new PublicRoomTO();
        r.setRoomId(id);
        newPlugTO.setRoom(r);

        Plug newPlug = new Plug();
        plugsTOService.updatePlugEntity(newPlug, newPlugTO);
        long newPlugId = this.plugsManager.create(newPlug);
        URI createdURI = context.getAbsolutePathBuilder().path(Long.toString(newPlugId)).build();
        return Response.created(createdURI).build();
    }
}
