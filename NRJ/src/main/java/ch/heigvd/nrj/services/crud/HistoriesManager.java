package ch.heigvd.nrj.services.crud;

import ch.heigvd.nrj.exceptions.EntityNotFoundException;
import ch.heigvd.nrj.model.History;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * DAO service, implementing CRUD operations on the Historie entity. The class
 * uses the JPA entity manager to interact with the DB. It returns JPA entities
 * to its clients.
 *
 * @author Olivier Liechti
 */
@Stateless
public class HistoriesManager implements HistoriesManagerLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long create(History historyData) {
        History newHistory = new History(historyData);
        em.persist(newHistory);
        return newHistory.getId();
    }

    @Override
    public void update(History newState) throws EntityNotFoundException {
        em.merge(newState);
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        History apartmentToDelete = findById(id);
        em.remove(apartmentToDelete);
    }

    @Override
    public History findById(long id) throws EntityNotFoundException {
        History existingHistorie = em.find(History.class, id);
        if (existingHistorie == null) {
            throw new EntityNotFoundException();
        }
        return existingHistorie;
    }

    @Override
    public List<History> findAll() {
// Note: the findAllHistories JPQL query is defined in the Historie.java file
        List apartments = em.createNamedQuery("Historie.findAllHistories").getResultList();
        return apartments;
    }
}