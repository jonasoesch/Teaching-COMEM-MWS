package ch.heigvd.nrj.services.crud;

import ch.heigvd.nrj.exceptions.EntityNotFoundException;
import ch.heigvd.nrj.model.Employee;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * This is an example for a DAO service, implementing CRUD operations on the
 * Employee entity. The class uses the JPA entity manager to
 * interact with the DB. It returns JPA entities to its clients.
 * 
 * @author Olivier Liechti
 */
@Stateless
public class EmployeesManager implements EmployeesManagerLocal {

	@PersistenceContext
	private EntityManager em;

	@Override
	public long create(Employee employeeData) {
		Employee newEmployee = new Employee(employeeData);
		em.persist(newEmployee);
                em.flush();
		return newEmployee.getId();
	}

	@Override
	public void update(Employee newState) throws EntityNotFoundException {
		em.merge(newState);
	}

	@Override
	public void delete(long id) throws EntityNotFoundException {
		Employee employeeToDelete = findById(id);
		em.remove(employeeToDelete);
	}

	@Override
	public Employee findById(long id) throws EntityNotFoundException {
		Employee existingEmployee = em.find(Employee.class, id);
		if (existingEmployee == null) {
			throw new EntityNotFoundException();
		}
		return existingEmployee;
	}

	@Override
	public List<Employee> findAll() {
		// Note: the findAllEmployees JPQL query is defined in the Employee.java file
        Query query = em.createNamedQuery("Employee.findAllEmployees");
		List employees = query.getResultList();

		return employees;
	}
	
}
