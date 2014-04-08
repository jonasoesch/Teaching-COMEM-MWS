package ch.heigvd.nrj.services.to;

import ch.heigvd.nrj.model.PlugConsumption;
import ch.heigvd.nrj.to.PublicPlugConsumptionTO;
import javax.ejb.Local;

/**
 * This interface defines the contract fulfilled by the PlugConsumptionsTOService,
 * 
 * @author rschmutz
 */
@Local
public interface PlugConsumptionsTOServiceLocal {
	
	/**
	 * This method builds a TO instance from a JPA entity instance. 
	 * 
	 * @param source the JPA entity
	 * @return the TO
	 */
	public PublicPlugConsumptionTO buildPublicPlugConsumptionTO(PlugConsumption source);
	
	/**
	 * This method updates an existing JPA entity by merging the state of the
	 * provided TO instance. 
	 * 
	 * @param existingEntity the existing entity that we want to update
	 * @param newState a TO that contains new state (subset of the entity state)
	 */
	public void updatePlugConsumptionEntity(PlugConsumption existingEntity, PublicPlugConsumptionTO newState);
}