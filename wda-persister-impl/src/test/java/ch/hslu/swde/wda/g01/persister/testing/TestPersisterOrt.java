package ch.hslu.swde.wda.g01.persister.testing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.hslu.swde.wda.g01.domain.Ort;
import ch.hslu.swde.wda.g01.domain.WetterDaten;
import ch.hslu.swde.wda.g01.persister.api.*;
import ch.hslu.swde.wda.g01.persister.impl.OrtDaoImpl;
import ch.hslu.swde.wda.g01.persister.impl.WetterDatenDaoImpl;


class TestPersisterOrt {
	
	OrtDao persister = new OrtDaoImpl();
	WetterDatenDao persisterW = new WetterDatenDaoImpl();

	@BeforeEach
	public void setUp() throws Exception{
		for(WetterDaten w : persisterW.alle(WetterDaten.class)) {
			persisterW.loeschen(w);
		}
		for(Ort o : persister.alle(Ort.class)) {
			persister.loeschen(o);
		}
	}

	@Test
	 void testSpeichern() {
	
		//Arrange
		 List<Ort> liste = TestUtil.createOrtListe();
		 
		 //Act
		 for (Ort o : liste) {
			 persister.speichern(o);
		 }
		 
		 //Assert
		 assertEquals(liste.size(), persister.alle(Ort.class).size());
		 
	
	}
	
	@Test
	void testLoeschen() {
		
		 List<Ort> liste = TestUtil.createOrtListe();
		 
		 for (Ort o : liste) {
			 persister.speichern(o);
		 }
		 
		 List<Ort> listeFromDb = persister.alle(Ort.class);
		 assertEquals(liste.size(), listeFromDb.size());
		 
		 Ort o = listeFromDb.get(1);
		 persister.loeschen(o);
		 
		 assertEquals(liste.size()-1, persister.alle(Ort.class).size());
	}
	
	@Test
	void testAktualisieren() {
		
		List<Ort> liste = TestUtil.createOrtListe();
		 
		 for (Ort o : liste) {
			 persister.speichern(o);
		 }
		 
		 List<Ort> listeFromDb = persister.alle(Ort.class);
		 assertEquals(liste.size(), listeFromDb.size());
		 
		 Ort o = listeFromDb.get(1);
		 o.setZip(6032);
		 o.setName("Emmen");
		 Ort newO = persister.aktualisieren(o);
		 
		 assertEquals(newO.getZip(), o.getZip());
		 assertEquals(newO.getName(), o.getName());
	}
	
	@Test
	void testFind() {
		
		List<Ort> liste = TestUtil.createOrtListe();
		 
		 for (Ort o : liste) {
			 persister.speichern(o);
		 }
		 
		 List<Ort> listeFromDb = persister.alle(Ort.class);
		 assertEquals(liste.size(), listeFromDb.size());
		 
		 Ort o = listeFromDb.get(1);
		 o.setName("Emmen");
		 o.setZip(6032);
		 persister.aktualisieren(o);
		 
		 assertEquals(o.getId(), persister.find(Ort.class, o.getId()).getId());
	}
	
	@Test
	void testAlleSpeichern() {
		
		List<Ort> liste = TestUtil.createOrtListe();
		
		persister.alleSpeichern(liste);
		
		List<Ort> listeFromDb = persister.alle(Ort.class);
		assertEquals(liste.size(), listeFromDb.size());
		
	}

	@Test
	void testAlle() {
		
		List<Ort> liste = TestUtil.createOrtListe();
		
		for (Ort o : liste) {
			persister.speichern(o);
		}
		
		List<Ort> listeFromDb = persister.alle(Ort.class);
		assertEquals(liste.size(), listeFromDb.size());
		
	}
	
	@Test
	void testFindByZip() {
		
		List<Ort> liste = TestUtil.createOrtListe();
		
		for (Ort o : liste) {
			persister.speichern(o);		
		}
		
		assertNotNull(persister.findByZip(6300));
		
	}
	
	@Test
    void testFindByOrt() {
        List<Ort> liste = TestUtil.createOrtListe();

        for (Ort o : liste) {
            persister.speichern(o);
        }

     
        Ort ort = liste.get(0);
        List<Ort> results = persister.findByOrt(ort.getName());
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(ort.getName(), results.get(0).getName());

        
        results = persister.findByOrt("ExistiertNicht");
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }
	
	
}
