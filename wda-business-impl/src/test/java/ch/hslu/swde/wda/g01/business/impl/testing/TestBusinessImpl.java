package ch.hslu.swde.wda.g01.business.impl.testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ch.hslu.swde.wda.g01.domain.Benutzer;
import ch.hslu.swde.wda.g01.domain.Ort;
import ch.hslu.swde.wda.g01.domain.WetterDaten;
import ch.hslu.wda.g01.business.api.*;
import ch.hslu.wda.g01.business.impl.*;
import ch.hslu.swde.wda.g01.persister.api.*;
import ch.hslu.swde.wda.g01.persister.impl.*;

class TestBusinessImpl {

	Wda wda = new WdaImpl();
	BenutzerDao bPersister = new BenutzerDaoImpl();
	WetterDatenDao wdPersister = new WetterDatenDaoImpl();
	OrtDao oPersister = new OrtDaoImpl();

	@BeforeEach
	void SetUp() {
		for (Benutzer b : bPersister.alle(Benutzer.class)) {
			bPersister.loeschen(b);
		}
		for (WetterDaten w : wdPersister.alle(WetterDaten.class)) {
			wdPersister.loeschen(w);
		}
		for (Ort o : oPersister.alle(Ort.class)) {
			oPersister.loeschen(o);
		}

	}

	@Test
	void testHinzufuegenBenutzer() {
		List<Benutzer> bliste = TestUtil.createBenutzerListe();
		for (Benutzer b : bliste) {
			wda.hinzufuegenBenutzer(b);
		}

		assertEquals(bliste.size(), bPersister.alle(Benutzer.class).size());

	}

	@Test
	void testHinzufuegenBenutzerDoppelt() {
		List<Benutzer> bliste = TestUtil.createBenutzerListe();
		for (Benutzer b : bliste) {
			wda.hinzufuegenBenutzer(b);
		}

		assertEquals(bliste.size(), bPersister.alle(Benutzer.class).size());

		Benutzer b1 = new Benutzer("mmustermann", "Max", "Mustermann", "Benutzer");
		wda.hinzufuegenBenutzer(b1);

		assertEquals(bliste.size(), bPersister.alle(Benutzer.class).size());

	}

	@Test
	void testLoeschenBenutzer() {
		List<Benutzer> bliste = TestUtil.createBenutzerListe();
		for (Benutzer b : bliste) {
			wda.hinzufuegenBenutzer(b);
		}

		assertEquals(bliste.size(), bPersister.alle(Benutzer.class).size());

		wda.loeschenBenutzer(bliste.get(0));
		bliste.remove(0);

		assertEquals(bliste.size(), bPersister.alle(Benutzer.class).size());

	}

	@Test
	void testAnpassenBenutzer() {
		List<Benutzer> bliste = TestUtil.createBenutzerListe();
		for (Benutzer b : bliste) {
			wda.hinzufuegenBenutzer(b);
		}

		assertEquals(bliste.size(), bPersister.alle(Benutzer.class).size());

		Benutzer b1 = bliste.get(0);
		b1.setBenutzerName("NewUserName");
		b1.setVorName("NewName");
		b1.setNachName("NewSurname");
		Benutzer aktualisierterBenutzer = wda.anpassenBenutzer(b1);

		assertEquals(aktualisierterBenutzer.getBenutzerName(), b1.getBenutzerName());
		assertEquals(aktualisierterBenutzer.getVorName(), b1.getVorName());
		assertEquals(aktualisierterBenutzer.getNachName(), b1.getNachName());
	}

	@Test
	void testAnpassenBenutzername() {
		List<Benutzer> bliste = TestUtil.createBenutzerListe();
		for (Benutzer b : bliste) {
			wda.hinzufuegenBenutzer(b);
		}

		assertEquals(bliste.size(), bPersister.alle(Benutzer.class).size());

		Benutzer b1 = bliste.get(0);
		b1.setBenutzerName("NewUserName");
		Benutzer aktualisierterBenutzer = wda.anpassenBenutzer(b1);

		assertEquals(aktualisierterBenutzer.getBenutzerName(), b1.getBenutzerName());

	}

	@Test
	void testAnpassenNachname() {
		List<Benutzer> bliste = TestUtil.createBenutzerListe();
		for (Benutzer b : bliste) {
			wda.hinzufuegenBenutzer(b);
		}

		assertEquals(bliste.size(), bPersister.alle(Benutzer.class).size());

		Benutzer b1 = bliste.get(0);
		b1.setNachName("NewSurname");
		Benutzer aktualisierterBenutzer = wda.anpassenBenutzer(b1);

		assertEquals(aktualisierterBenutzer.getNachName(), b1.getNachName());

	}

	@Test
	void testAnpassenVorname() {
		List<Benutzer> bliste = TestUtil.createBenutzerListe();
		for (Benutzer b : bliste) {
			wda.hinzufuegenBenutzer(b);
		}

		assertEquals(bliste.size(), bPersister.alle(Benutzer.class).size());

		Benutzer b1 = bliste.get(0);
		b1.setVorName("NewName");
		Benutzer aktualisierterBenutzer = wda.anpassenBenutzer(b1);

		assertEquals(aktualisierterBenutzer.getVorName(), b1.getVorName());

	}

	@Test
	void testAnpassenPasswort() {
		List<Benutzer> bliste = TestUtil.createBenutzerListe();
		for (Benutzer b : bliste) {
			wda.hinzufuegenBenutzer(b);
		}

		assertEquals(bliste.size(), bPersister.alle(Benutzer.class).size());

		Benutzer b1 = bliste.get(0);
		b1.setPasswort("NewPassword");
		Benutzer aktualisierterBenutzer = wda.anpassenBenutzer(b1);

		assertEquals(aktualisierterBenutzer.getPasswort(), b1.getPasswort());

	}

	@Test
	void testAlleOrtschaftenAbrufen() {
		List<Ort> orte = TestUtil.createOrtListe();
		for (Ort o : orte) {
			oPersister.speichern(o);
		}

		assertEquals(orte.size(), wda.alleOrtschaften().size());

	}

	@Disabled
	void testOrtschaftenExportKonsole() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<Ort> orte = wda.alleOrtschaften();

		assertDoesNotThrow(() -> wda.ortschaftenExport(orte, 4));
	}

	@Disabled
	void testOrtschaftenExportCSV() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<Ort> orte = wda.alleOrtschaften();

		assertDoesNotThrow(() -> wda.ortschaftenExport(orte, 2));
	}

	@Disabled
	void testOrtschaftenExportPDF() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<Ort> orte = wda.alleOrtschaften();

		assertDoesNotThrow(() -> wda.ortschaftenExport(orte, 1));
	}

	@Disabled
	void testOrtschaftenExportJSON() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<Ort> orte = wda.alleOrtschaften();

		assertDoesNotThrow(() -> wda.ortschaftenExport(orte, 3));
	}

	@Disabled
	void testOrtschaftenExportEmptyFile() {
		List<WetterDaten> wd = new ArrayList<>();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<Ort> orte = wda.alleOrtschaften();

		assertDoesNotThrow(() -> wda.ortschaftenExport(orte, 3));
	}

	@Test
	void testAbrufenEinzelwerteNotNull() {
		List<WetterDaten> wd = new ArrayList<>();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		assertNotNull(wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0)));
	}

	@Test
	void testAbrufenEinzelwerteTimeFormatHHmm() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		assertEquals((wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0), LocalTime.of(17, 0)).get(0).getOrt().getName()), "Luzern");
	}

	@Test
	void testAbrufenEinzelwerteEmptyList() {
		List<WetterDaten> wd = new ArrayList<>();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		assertNotNull(wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 17), LocalDate.of(2024, 5, 18),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0)));
	}

	@Test
	void testAbrufenEinzelwerteExactDate() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		assertEquals((wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 16), LocalDate.of(2024, 5, 16),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0)).get(0).getOrt().getName()), "Luzern");
	}

	@Test
	void testAbrufenEinzelwerteDatumVonGrösserAlsDatumBis() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		assertEquals((wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 18), LocalDate.of(2024, 5, 15),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0)).get(0).getOrt().getName()), "Luzern");

	}

	@Test
	void testAbrufenEinzelwerteCorrectValue() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		assertEquals((wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0))).get(0).getOrt().getName(), "Luzern");
	}

	@Test
	void testAbrufenDurchschnittswerteCorrectValueLuftdruck() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		System.out.println(wdDB);

		// assertEquals(wda.abrufenDurchschnittswerte(wdDB).getLuftDruck(), 1530);
	}

	@Test
	void testAbrufenDurchschnittswerteCorrectValueLuftfeuchtigkeit() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		assertEquals(wda.abrufenDurchschnittswerte(wdDB).getLuftFeuchtigkeit(), 72);
	}

	@Test
	void testAbrufenDurchschnittswerteCorrectValueTemperatur() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		assertEquals(wda.abrufenDurchschnittswerte(wdDB).getTemperatur(), 30);
	}

	@Test
	void testAbrufenMinMaxCorrectValueLuftdruck() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		assertEquals(wda.abrufenMinMax(wdDB).get(0).getLuftDruck(), 1020);
		assertEquals(wda.abrufenMinMax(wdDB).get(1).getLuftDruck(), 2040);
	}

	@Test
	void testAbrufenMinMaxCorrectValueLuftfeuchtigkeit() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		assertEquals(wda.abrufenMinMax(wdDB).get(0).getLuftFeuchtigkeit(), 48);
		assertEquals(wda.abrufenMinMax(wdDB).get(1).getLuftFeuchtigkeit(), 96);
	}

	@Test
	void testAbrufenMinMaxCorrectValueTemperatur() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		assertEquals(wda.abrufenMinMax(wdDB).get(0).getTemperatur(), 25.8);
		assertEquals(wda.abrufenMinMax(wdDB).get(1).getTemperatur(), 34.2);
	}

	@Test
	void testValidiereBenutzername() {
		List<Benutzer> benutzer = TestUtil.createBenutzerListe();
		for (Benutzer b : benutzer) {
			bPersister.speichern(b);
		}

		assertEquals(wda.validiereBenutzername("cheffe").getNachName(), "Weber");
	}

	@Test
	void testFindeByBenutzername() {
		List<Benutzer> benutzer = TestUtil.createBenutzerListe();
		for (Benutzer b : benutzer) {
			bPersister.speichern(b);
		}
		assertEquals(wda.findeBenutzerById(bPersister.alle(Benutzer.class).get(0).getId()).getBenutzerName(),
				"mmustermann");
	}

	@Test
	void testFindeById() {
		List<Benutzer> benutzer = TestUtil.createBenutzerListe();
		for (Benutzer b : benutzer) {
			bPersister.speichern(b);
		}
		List<Benutzer> bListe = bPersister.alle(Benutzer.class);

		assertEquals(wda.findeBenutzerById(bListe.get(0).getId()).getBenutzerName(), bListe.get(0).getBenutzerName());
	}

	
	void testErsterBezugAPI() {
		assertTrue(wda.ersterBezugAPI());
	}

	@Disabled
	void testErsterAktualisierenAPI() {

		boolean test = wda.ersterBezugAPI();

		System.out.println(test);

		assertTrue(wda.aktualisierenAPI());
	}

	@Disabled
	void testWetterdatenExportKonsole() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}
		String zeitRaum = "15.05.2024 - 17.05.2024";
		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		assertDoesNotThrow(() -> wda.wetterDatenExportDurchschnitt(wdDB.get(0), 4, zeitRaum));
	}

	@Disabled
	void testWetterdatenExportCSV() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}
		String zeitRaum = "15.05.2024 - 17.05.2024";
		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		assertDoesNotThrow(() -> wda.wetterDatenExportDurchschnitt(wdDB.get(0), 2, zeitRaum));
	}

	@Disabled
	void testWetterdatenExportPDF() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}
		String zeitRaum = "15.05.2024 - 17.05.2024";
		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		assertDoesNotThrow(() -> wda.wetterDatenExportDurchschnitt(wdDB.get(0), 1, zeitRaum));
	}

	@Disabled
	void testWetterdatenExportJSON() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}
		String zeitRaum = "15.05.2024 - 17.05.2024";
		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		WetterDaten wdDBEinzel = wdDB.get(0);
		// System.out.println(wdDBEinzel);

		assertDoesNotThrow(() -> wda.wetterDatenExportDurchschnitt(wdDBEinzel, 3, zeitRaum));
	}

	@Disabled
	void testWetterdatenExportMehrereKonsole() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		assertDoesNotThrow(() -> wda.wetterDatenExportMinMaxEinzel(wdDB, 4, "Test", "Einzel"));
	}

	@Disabled
	void testWetterdatenExportMehrereCSV() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		assertDoesNotThrow(() -> wda.wetterDatenExportMinMaxEinzel(wdDB, 2, "Test", "Einzel"));
	}

	@Disabled
	void testWetterdatenExportMehrerePDF() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		assertDoesNotThrow(() -> wda.wetterDatenExportMinMaxEinzel(wdDB, 1, "Test", "Einzel"));
	}

	@Disabled
	void testWetterdatenExportMehrereJSON() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);
		}

		List<WetterDaten> wdDB = wda.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 15), LocalDate.of(2024, 5, 17),
				LocalTime.of(14, 0, 0), LocalTime.of(17, 0, 0));

		assertDoesNotThrow(() -> wda.wetterDatenExportMinMaxEinzel(wdDB, 3, "Test", "Einzel"));
	}

	@Test
	void testPasswortVorhanden() {
		List<Benutzer> benutzer = TestUtil.createBenutzerListe();
		for (Benutzer b : benutzer) {
			bPersister.speichern(b);
		}

		Benutzer benutzerDB = bPersister.alle(Benutzer.class).get(0);

		assertNull(wda.passwortVorhanden(benutzerDB));
	}

	@Test
	void testPasswortSetzen() {
		List<Benutzer> benutzer = TestUtil.createBenutzerListe();
		for (Benutzer b : benutzer) {
			bPersister.speichern(b);
		}

		Benutzer benutzerDB = bPersister.alle(Benutzer.class).get(0);
		assertTrue(wda.passwortSetzen(benutzerDB, "Test"));
	}

	@Test
	void testPasswortValidieren() {
		Benutzer b = new Benutzer();
		b.setBenutzerName("admin");
		b.setPasswort("admin");
		bPersister.speichern(b);

		Benutzer benutzerDB = bPersister.alle(Benutzer.class).get(0);
		assertTrue(wda.validierePasswort("admin", benutzerDB.getPasswort()));
	}

	@Test
	void testRolleValidieren() {
		List<Benutzer> benutzer = TestUtil.createBenutzerListe();
		for (Benutzer b : benutzer) {
			bPersister.speichern(b);
		}

		Benutzer benutzerDB = bPersister.alle(Benutzer.class).get(0);
		assertEquals(wda.validiereRolle(benutzer.get(0)), benutzerDB.getRolle());
	}

	@Test
	void testMinMaxOrt() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);

		}
		
		assertNotNull(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "Temperatur"));
		assertNotNull(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "LuftDruck"));
		assertNotNull(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "LuftFeuchtigkeit"));
	}
	@Disabled
	void testMinMaxOrtExport() {
		List<WetterDaten> wd = TestUtil.createWetterDatenListe();
		for (WetterDaten w : wd) {
			wdPersister.speichern(w);

		}
		
		assertDoesNotThrow(() -> wda.minMaxOrtExport(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "Temperatur"), "31.05.2024 um 00:00 Uhr", 1, "Temperatur"));
		assertDoesNotThrow(() -> wda.minMaxOrtExport(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "Temperatur"), "31.05.2024 um 00:00 Uhr", 2, "Temperatur"));
		assertDoesNotThrow(() -> wda.minMaxOrtExport(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "Temperatur"), "31.05.2024 um 00:00 Uhr", 3, "Temperatur"));
		assertDoesNotThrow(() -> wda.minMaxOrtExport(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "Temperatur"), "31.05.2024 um 00:00 Uhr", 4, "Temperatur"));
		assertDoesNotThrow(() -> wda.minMaxOrtExport(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "LuftDruck"), "31.05.2024 um 00:00 Uhr", 1, "LuftDruck"));
		assertDoesNotThrow(() -> wda.minMaxOrtExport(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "LuftDruck"), "31.05.2024 um 00:00 Uhr", 2, "LuftDruck"));
		assertDoesNotThrow(() -> wda.minMaxOrtExport(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "LuftDruck"), "31.05.2024 um 00:00 Uhr", 3, "LuftDruck"));
		assertDoesNotThrow(() -> wda.minMaxOrtExport(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "LuftDruck"), "31.05.2024 um 00:00 Uhr", 4, "LuftDruck"));
		assertDoesNotThrow(() -> wda.minMaxOrtExport(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "LuftFeuchtigkeit"), "31.05.2024 um 00:00 Uhr", 1, "LuftFeuchtigkeit"));
		assertDoesNotThrow(() -> wda.minMaxOrtExport(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "LuftFeuchtigkeit"), "31.05.2024 um 00:00 Uhr", 2, "LuftFeuchtigkeit"));
		assertDoesNotThrow(() -> wda.minMaxOrtExport(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "LuftFeuchtigkeit"), "31.05.2024 um 00:00 Uhr", 3, "LuftFeuchtigkeit"));
		assertDoesNotThrow(() -> wda.minMaxOrtExport(wda.minMaxOrt(LocalDate.of(2024, 5, 31), LocalTime.of(0, 0), "LuftFeuchtigkeit"), "31.05.2024 um 00:00 Uhr", 4, "LuftFeuchtigkeit"));
	}
}
