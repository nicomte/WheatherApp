package ch.hslu.swde.wda.g01.persister.impl;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.swde.wda.g01.domain.Ort;
import ch.hslu.swde.wda.g01.domain.WetterDaten;
import ch.hslu.swde.wda.g01.persister.api.*;
import ch.hslu.swde.wda.g01.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class WetterDatenDaoImpl extends DataDaoImpl<WetterDaten> implements WetterDatenDao {


	OrtDao oPersister = new OrtDaoImpl();
	
	public WetterDaten speichern(WetterDaten wetterDaten) {
		EntityManager em = JpaUtil.createEntityManager();
		try {
			em.getTransaction().begin();
			List<Ort> foundOrtList = oPersister.findByZip(wetterDaten.getOrt().getZip());
			if(foundOrtList.isEmpty()) {
				em.persist(wetterDaten.getOrt());
			}else {
				Ort foundOrt = foundOrtList.get(0);
				wetterDaten.setOrt(foundOrt);
			}
			em.persist(wetterDaten);
			em.getTransaction().commit();
			return wetterDaten;
		}catch(Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}throw e;
		}finally {
			if(em.isOpen())
				em.close();
		}
	}
	
	public List<WetterDaten> alleSpeichern(List<WetterDaten> wetterDatenListe){
		EntityManager em = JpaUtil.createEntityManager();
		try {
			em.getTransaction().begin();
			for(WetterDaten wetterDaten : wetterDatenListe) {
				List<Ort> foundOrtList = oPersister.findByZip(wetterDaten.getOrt().getZip());
				if(foundOrtList.isEmpty()) {
					em.persist(wetterDaten.getOrt());
				}else {
					Ort foundOrt = foundOrtList.get(0);
					wetterDaten.setOrt(foundOrt);
				}
				em.persist(wetterDaten);
			}
			em.getTransaction().commit();
			return wetterDatenListe;
		}catch(Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}throw e;
		}finally {
			if(em.isOpen())
				em.close();
		}
	}
	
	
	
	
}
