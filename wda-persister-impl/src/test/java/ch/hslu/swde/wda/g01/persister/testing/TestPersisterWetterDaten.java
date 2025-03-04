package ch.hslu.swde.wda.g01.persister.testing;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.hslu.swde.wda.g01.persister.api.*;
import ch.hslu.swde.wda.g01.persister.impl.*;
import ch.hslu.swde.wda.g01.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import ch.hslu.swde.wda.g01.domain.*;

public class TestPersisterWetterDaten {
	
	

	WetterDatenDao wdPersister = new WetterDatenDaoImpl();
	OrtDao oPersister = new OrtDaoImpl();

	@BeforeEach
	public void setUp() throws Exception {

		for (WetterDaten a : wdPersister.alle(WetterDaten.class)) {
			wdPersister.loeschen(a);
		}
	}

	@Test
	void testSpeichern() {

		List<WetterDaten> listWd = TestUtil.createWetterDatenListe();

		for (WetterDaten w : listWd) {
			wdPersister.speichern(w);
		}
		assertEquals(listWd.size(), wdPersister.alle(WetterDaten.class).size());
	}

	@Test
	void testUpdaten() {
		

		List<WetterDaten> listWd = TestUtil.createWetterDatenListe();

		for (WetterDaten w : listWd) {
			wdPersister.speichern(w);
		}

		List<WetterDaten> listWdDb = wdPersister.alle(WetterDaten.class);
		assertEquals(listWd.size(), listWdDb.size());

		EntityManager em = JpaUtil.createEntityManager();
		try {
			em.getTransaction().begin();
			WetterDaten wd = listWdDb.get(0);
			
			Ort foundOrt = em.find(Ort.class, wd.getOrt().getId());
			foundOrt.setName("NewName");
			Ort updatedOrt = em.merge(foundOrt);
			

			
			em.getTransaction().commit();
			assertEquals(updatedOrt.getName(), wdPersister.find(WetterDaten.class, wd.getId()).getOrt().getName());
			
		}catch (Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}throw e;
		}if(em.isOpen()) {
			em.close();
		}
		
		WetterDaten w1 = listWd.get(1);
		w1.setLuftDruck(9999);
		WetterDaten w1updated = wdPersister.aktualisieren(w1);
		assertEquals(w1.getLuftDruck(), w1updated.getLuftDruck());

	}

	@Test
	void testDelete() {

		List<WetterDaten> listWd = TestUtil.createWetterDatenListe();

		for (WetterDaten w : listWd) {
			wdPersister.speichern(w);
		}
		
		List<WetterDaten> listWdDb = wdPersister.alle(WetterDaten.class);
		
		assertEquals(listWd.size(), listWdDb.size());
		
		WetterDaten wd = listWdDb.get(0);
		
		wdPersister.loeschen(wd);
		
		assertEquals(listWd.size() - 1, wdPersister.alle(WetterDaten.class).size());
	}
	@Test
	void testAlleSpeichern() {
		
		List<WetterDaten> listWd = TestUtil.createWetterDatenListe();
		
		wdPersister.alleSpeichern(listWd);
		
		assertEquals(listWd.size(), wdPersister.alle(WetterDaten.class).size());
	}

}
