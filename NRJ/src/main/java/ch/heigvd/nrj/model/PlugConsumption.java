/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ch.heigvd.nrj.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author rschmutz
 */
@Entity
public class PlugConsumption implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date timeStamp;
    private Double avgKW;

    public Date getTimeStamp() {
	return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
	this.timeStamp = timeStamp;
    }

    public Double getAvgKW() {
	return avgKW;
    }

    public void setAvgKW(Double avgKW) {
	this.avgKW = avgKW;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (id != null ? id.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof PlugConsumption)) {
	    return false;
	}
	PlugConsumption other = (PlugConsumption) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "ch.heigvd.nrj.model.PlugConsumption[ id=" + id + " ]";
    }

}
