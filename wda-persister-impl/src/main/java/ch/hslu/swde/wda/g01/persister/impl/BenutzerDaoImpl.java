package ch.hslu.swde.wda.g01.persister.impl;


import java.util.ArrayList;
import java.util.List;

import ch.hslu.swde.wda.g01.domain.Benutzer;
import ch.hslu.swde.wda.g01.persister.api.BenutzerDao;
import ch.hslu.swde.wda.g01.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class BenutzerDaoImpl extends DataDaoImpl<Benutzer> implements BenutzerDao{

	
	public Benutzer findByBenutzername(String benutzername) {
		EntityManager em = JpaUtil.createEntityManager();
        try {
            TypedQuery<Benutzer> tQry = em.createQuery("SELECT b FROM Benutzer b WHERE b.benutzerName = :benutzerName", Benutzer.class);
            tQry.setParameter("benutzerName", benutzername);
            List<Benutzer> bList = tQry.getResultList();
            return bList.isEmpty() ? null : bList.get(0);
        } catch (Exception e) {
            throw e;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
	
	
	
	
}
