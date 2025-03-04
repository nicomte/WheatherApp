package ch.hslu.wda.g01.business.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import ch.hslu.swde.wda.g01.domain.*;

public interface Wda{

	public List<Benutzer> alleBenutzer();
	public Benutzer hinzufuegenBenutzer(Benutzer benutzer);
	public Benutzer loeschenBenutzer(Benutzer benutzer);
	public Benutzer anpassenBenutzer(Benutzer benutzerName);
	public Benutzer anpassenBenutzername(Benutzer benutzer, String benutzername);
	public Benutzer anpassenNachname(Benutzer benutzer, String nachname);
	public Benutzer anpassenVorname(Benutzer benutzer, String vorname);
	public Benutzer anpassenPasswort(Benutzer benutzer, String vorname);
	public Benutzer anpassenRolle(Benutzer benutzer, String rolle);
	public List<Ort> alleOrtschaften();
	public void ortschaftenExport(List<Ort> orte, int exportDatentyp);
	public void wetterDatenExportDurchschnitt(WetterDaten wetterDaten, int exportDatentyp, String zeitRaum);
	public void wetterDatenExportMinMaxEinzel(List<WetterDaten> wetterDaten, int exportDatentyp, String zeitRaum, String art);
	public List<WetterDaten> abrufenEinzelwerte(String ort, LocalDate datumVon, LocalDate datumBis, LocalTime zeitVon, LocalTime zeitBis);
	public WetterDaten abrufenDurchschnittswerte(List<WetterDaten> einzelWerte);
	public List<WetterDaten> abrufenMinMax(List<WetterDaten> einzelWerte);
	public List<WetterDaten> minMaxOrt(LocalDate datum, LocalTime zeit, String artDerAbfrage);
	public void minMaxOrtExport(List<WetterDaten> wetterDaten, String zeitPunkt, int exportTyp, String artDerAbfrage);
	public Benutzer validiereBenutzername(String benutzerName);
	public String passwortVorhanden(Benutzer benutzer);
	public boolean passwortSetzen(Benutzer benutzer, String passwort);
	public boolean validierePasswort(String passwortEingabe, String passwortDB);
	public String validiereRolle(Benutzer benutzer);
	public Benutzer findeBenutzerById(int id);
	public boolean aktualisierenAPI();
	public boolean ersterBezugAPI();
	public boolean ortVorhanden(String ort);
	
	
}
