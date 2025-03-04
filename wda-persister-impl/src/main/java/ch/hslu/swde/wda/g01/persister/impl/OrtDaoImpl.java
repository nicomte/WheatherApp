package ch.hslu.swde.wda.g01.persister.impl;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.swde.wda.g01.domain.Ort;
import ch.hslu.swde.wda.g01.persister.api.OrtDao;
import ch.hslu.swde.wda.g01.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class OrtDaoImpl extends DataDaoImpl<Ort> implements OrtDao {

	
	public List<Ort> findByZip(int zip) {
	    EntityManager em = JpaUtil.createEntityManager();
	    try {
	        TypedQuery<Ort> tQry = em.createQuery("SELECT o FROM Ort o WHERE o.zip = " + zip, Ort.class);
	        List<Ort> results = tQry.getResultList();
	        return results != null ? results : new ArrayList <Ort>();
	    } catch (Exception e) {
	        throw e;
	    } finally {
	        if (em.isOpen()) {
	            em.close();
	        }
	    }
	}
	
	public List<Ort> findByOrt(String name) {
	    EntityManager em = JpaUtil.createEntityManager();
	    try {
	        TypedQuery<Ort> tQry = em.createQuery("SELECT o FROM Ort o WHERE o.name = :name", Ort.class);
	        tQry.setParameter("name", name);
	        List<Ort> results = tQry.getResultList();
	        return results != null ? results : new ArrayList<Ort>();
	    } catch (Exception e) {
	        throw e;
	    } finally {
	        if (em.isOpen()) {
	            em.close();
	        }
	    }
	}
	
	
	
}
