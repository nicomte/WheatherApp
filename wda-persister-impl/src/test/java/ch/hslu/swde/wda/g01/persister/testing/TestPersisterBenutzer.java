package ch.hslu.swde.wda.g01.persister.testing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import ch.hslu.swde.wda.g01.domain.Benutzer;
import ch.hslu.swde.wda.g01.persister.api.*;
import ch.hslu.swde.wda.g01.persister.impl.BenutzerDaoImpl;

class TestPersisterBenutzer {
	
	BenutzerDao persister = new BenutzerDaoImpl();
	
	@BeforeEach
	public void setUp() throws Exception{
		for(Benutzer b : persister.alle(Benutzer.class)) {
			persister.loeschen(b);
		}
	}
	
	@Test
	 void addAdminUser() {
		
		Benutzer admin = new Benutzer("DM_Admin", "Dario", "Meyer", "admin");
		admin.setPasswort("admin");
		
		Benutzer saved = persister.speichern(admin);
		 
		 //Assert
		 assertEquals(admin.getBenutzerName(), saved.getBenutzerName());
		 assertEquals(admin.getBenutzerName(), persister.findByBenutzername(saved.getBenutzerName()).getBenutzerName());
		 
		 
	 }

	@Test
	 void testSpeichern() {
	 
		 //Arrange
		 List<Benutzer> liste = TestUtil.createBenutzerListe();
		 
		 //Act
		 for (Benutzer b : liste) {
			 persister.speichern(b);
		 }
		 
		 //Assert
		 assertEquals(liste.size(), persister.alle(Benutzer.class).size());
		 
		 
	 }
	
	@Test
	 void testPasswortSpeichern() {
	 
		 //Arrange
		 List<Benutzer> liste = TestUtil.createBenutzerListe();
		 
		 //Act
		 for (Benutzer b : liste) {
			 persister.speichern(b);
		 }
	
		 Benutzer b1 = new Benutzer("bname", "bVorname", "bNachname", "user");
		 b1.setPasswort("gutesPasswort");
	 
		 liste.add(b1);
		 
		 persister.speichern(b1);
	 
		 assertEquals(liste.size(), persister.alle(Benutzer.class).size());

}
	
	@Test
	 void testLoeschen() {
	 
		List<Benutzer> liste = TestUtil.createBenutzerListe();
		
		for (Benutzer b : liste) {
			persister.speichern(b);
		}
		 
		List<Benutzer> listeFromDb = persister.alle(Benutzer.class);
		assertEquals(liste.size(), listeFromDb.size());
		
		Benutzer ben = listeFromDb.get(1);
		persister.loeschen(ben);
		
		assertEquals(liste.size()-1, persister.alle(Benutzer.class).size());
}
	
	
	@Test
	 void testAktualisieren() {
	 
	 List<Benutzer> liste = TestUtil.createBenutzerListe();
		
		for (Benutzer b : liste) {
			persister.speichern(b);
		}
		
		List<Benutzer> listeFromDb = persister.alle(Benutzer.class);
		assertEquals(liste.size(), listeFromDb.size());
		
		Benutzer ben = listeFromDb.get(1);
		ben.setBenutzerName("admin");
		ben.setNachName("adNachname");
		ben.setVorName("adVorname");
		Benutzer newBen = persister.aktualisieren(ben);
		
		assertEquals(newBen.getBenutzerName(), ben.getBenutzerName());
		assertEquals(newBen.getNachName(), ben.getNachName());
		assertEquals(newBen.getVorName(), ben.getVorName());
}
	

	@Test
	void testFind() {
		
		List<Benutzer> liste = TestUtil.createBenutzerListe();
		 
		for (Benutzer b : liste) {
			persister.speichern(b);
		}
		 
		List<Benutzer> listeFromDb = persister.alle(Benutzer.class);
		assertEquals(liste.size(), listeFromDb.size());
		 
		Benutzer ben = listeFromDb.get(1);
		ben.setBenutzerName("findBenutzer");
		ben.setNachName("findNachname");
		ben.setVorName("findVorname");
		persister.aktualisieren(ben);
		
		assertEquals(ben.getId(), persister.find(Benutzer.class, ben.getId()).getId());
		
	}
	
	@Test
	void testFindByBenutzername() {
		List<Benutzer> liste = TestUtil.createBenutzerListe();
		for (Benutzer b : liste) {
			persister.speichern(b);
		}
		
		
		
		assertNotNull(persister.findByBenutzername("mmustermann"));
		
		Benutzer b1 = liste.get(2);
		assertEquals(b1.getBenutzerName(), persister.findByBenutzername(b1.getBenutzerName()).getBenutzerName());
	}
	
	@Test
	void testAlleSpeichern() {
		
	
		 List<Benutzer> liste = TestUtil.createBenutzerListe();
		 
		 
			 persister.alleSpeichern(liste);
		 
		 
		 List<Benutzer> listeFromDb = persister.alle(Benutzer.class);
	     assertEquals(liste.size(), listeFromDb.size());
		
	}
	
	@Test
	 void testAlle() {
	 
		 List<Benutzer> liste = TestUtil.createBenutzerListe();
		 
		 for (Benutzer b : liste) {
			persister.speichern(b);
		 }
		 
		 List<Benutzer> listeFromDb = persister.alle(Benutzer.class);
		 assertEquals(liste.size(), listeFromDb.size());
	 
	}
	
}
