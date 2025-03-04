package ch.hslu.swde.wda.g01.persister.testing;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import ch.hslu.swde.wda.g01.domain.*;

public class TestUtil {

private TestUtil() {
		
	}
	
	public static List<Benutzer> createBenutzerListe(){
		
		List<Benutzer> liste = new ArrayList<>();
		
		liste.add(new Benutzer("mmustermann", "Max", "Mustermann", "user"));
		liste.add(new Benutzer("MMUHAHAH", "Peter", "Witzbold", "user"));
		liste.add(new Benutzer("cheffe", "Markus", "Weber", "user"));
		
		return liste;
	}
	
	public static List<Ort> createOrtListe(){
		
		List<Ort> liste = new ArrayList<>();
		
		liste.add(new Ort(6000, "Luzern"));
		liste.add(new Ort(6300, "Zug"));
		liste.add(new Ort(8000, "Zuerich"));
		
		return liste;
		
	}
	
	public static List<WetterDaten> createWetterDatenListe(){
		
		List<WetterDaten> liste = new ArrayList<>();
		
		liste.add(new WetterDaten(new Ort(6000, "Luzern"), LocalTime.of(16, 0), LocalDate.of(2024, 5, 16), 1020, 48, 25.8));
		liste.add(new WetterDaten(new Ort(6300, "Zug"), LocalTime.of(8, 0), LocalDate.of(2024, 5, 6), 980, 20, 20.2));
		liste.add(new WetterDaten(new Ort(8000, "Zuerich"), LocalTime.of(0, 0), LocalDate.of(2024, 5, 31), 1000, 87, 14.6));
		liste.add(new WetterDaten(new Ort(9999, "Test"), LocalTime.of(16, 0), LocalDate.of(2024, 5,31), 0, 0, 0.0));
		
		return liste;
	}
	
}
