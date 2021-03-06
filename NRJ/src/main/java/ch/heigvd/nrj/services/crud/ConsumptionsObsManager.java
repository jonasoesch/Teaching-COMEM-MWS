package ch.heigvd.nrj.services.crud;

import ch.heigvd.nrj.exceptions.EntityNotFoundException;
import ch.heigvd.nrj.model.ConsumptionObs;
import ch.heigvd.nrj.model.Plug;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * DAO service, implementing CRUD operations on the ConsumptionObs entity. The
 * class uses the JPA entity manager to interact with the DB. It returns JPA
 * entities to its clients.
 *
 * @author nicolas.
 */
@Stateless
public class ConsumptionsObsManager implements ConsumptionsObsManagerLocal {

    @EJB
    PlugsManagerLocal plugsManager;
    @PersistenceContext
    private EntityManager em;

    /**
     * Create a consumption in persistant DB.
     *
     * @param consumption the consumption to create.
     * @return the id generate.
     */
    @Override
    public long create(ConsumptionObs consumption) {
	// Add la consommation à la plug
	Plug plug = consumption.getPlug();
	System.out.println("hu hu" + plug);

	try {
	    // Rechercher la plug de cette consumption
	    plug = plugsManager.findById(plug.getId());
	} catch (EntityNotFoundException ex) {
	    System.out.println("ERREUR DANS CONSUMPTIONOBSMANAGER.CREATE(consumption)");
	    Logger.getLogger(ConsumptionsObsManager.class.getName()).log(Level.SEVERE, null, ex);
	}
	// Add la plug à cette consommation
	consumption.setPlug(plug);
	em.persist(consumption);
	plug.addConsumptionObs(consumption);
	em.flush();
	return consumption.getId();
    }

    /**
     * Update a consumption persistant.
     *
     * @param newState the consumption to update.
     * @throws EntityNotFoundException
     */
    @Override
    public void update(ConsumptionObs newState) throws EntityNotFoundException {
	em.merge(newState);
    }

    /**
     * Delete a persistant consumption.
     *
     * @param id the id to the persistant consumption to delete
     * @throws EntityNotFoundException
     */
    @Override
    public void delete(long id) throws EntityNotFoundException {
	ConsumptionObs apartmentToDelete = findById(id);
	em.remove(apartmentToDelete);
    }

    /**
     * Find a consumption by id.
     *
     * @param id the id to the consumption to find.
     * @return the consumption finded.
     * @throws EntityNotFoundException
     */
    @Override
    public ConsumptionObs findById(long id) throws EntityNotFoundException {
	ConsumptionObs existingConsumption = em.find(ConsumptionObs.class, id);
	if (existingConsumption == null) {
	    throw new EntityNotFoundException();
	}
	return existingConsumption;
    }

    /**
     * Find all consumption in persistant DB.
     *
     * @return list of all consumptions.
     */
    @Override
    public List<ConsumptionObs> findAll() {
// Note: the findAllConsumptions JPQL query is defined in the ConsumptionObs.java file
	List consumptionsObs = em.createNamedQuery("ConsumptionObs.findAllConsumptionsObs").getResultList();
	return consumptionsObs;
    }
}
