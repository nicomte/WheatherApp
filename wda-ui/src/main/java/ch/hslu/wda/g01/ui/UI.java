package ch.hslu.wda.g01.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import ch.hslu.swde.wda.g01.domain.Benutzer;
import ch.hslu.swde.wda.g01.domain.WetterDaten;
import ch.hslu.swde.wda.g01.proxy.WdaProxy;
import ch.hslu.wda.g01.business.api.*;
import ch.hslu.wda.g01.business.impl.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Calss represents the UI that the User see's during run-time.
 * Also additional Methods are implemented to simplify the class as much as possible.
 * 
 * @author Dario Meyer, Mathis Trautmann, Agata Ciesielska, Nico Graf
 * @version 1.0.0
 */
public class UI {
	
	private static final Logger LOG = LogManager.getLogger(UI.class);
	// Menu-Konstanten

	private static final String MENU_AKTUALISIEREN = "Alle Wetterdaten von der API laden [1]     Nur die neusten Daten aus der API laden [2]     Keine Daten laden [0] \nAuswahl eingeben: ";
	private static final String MENU_LOGIN_USERNAME = "LOGIN:\nBenutzername eingeben: ";
	private static final String MENU_LOGIN_PASSWORD = "Passwort eingeben: ";
	private static final String UMENU_1_0_0 = "Wetterdaten abfragen [1]     Logout [2]     Applikation schliessen [0] \nAuswahl eingeben: ";
	private static final String UMENU_1_1_0 = "Ortschaften abrufen [1]     Daten zu bestimmten Zeitpunkt und Ort abrufen [2]     Hoechst- und Tiefstwerte Schweiz abrufen[3]     Zurueck [0] \nAuswahl eingeben: ";
	private static final String UMENU_PEZ_0 = "Print [1]     Export [2]     Zurueck [0] \nAuswahl eingeben: ";
	private static final String UMENU_PEZ_1 = "PDF [1]     CSV [2]     JSON [3]     Zurueck[0] \nAuswahl eingeben: ";
	private static final String UMENU_1_2_0 = "Einzelwerte [1]     Durchschnitte [2]     Min/Max [3]     Zurueck [0] \nAuswahl eingeben: ";
	private static final String UMENU_1_2_1 = "Hoechst- und Tiefsttemperatur in der Schweiz [1]     Hoechst und Tiefster Luftdruck in der Schweiz [2]    Hoechste und Tiefste Luftfeuchtigkeit in der Schweiz [3]     Zurueck [0]\nAuswahl eingeben: ";
	private static final String UMENU_1_2_PLACE = "Bitte einen Ort eingeben: " ;
	private static final String UMENU_1_2_VDATE = "Eingabe Datum von (Format: DD.MM.YYYY): ";
	private static final String UMENU_1_2_BDATE = "Eingabe Datum bis (Format: DD.MM.YYYY): ";
	private static final String UMENU_1_2_TIME = "Eingabe Zeit?     Ja [1]     Nein [2]\nAuswahl eingeben: ";
	private static final String UMENU_1_2_VTIME = "Eingabe Zeit von (Format: hh:mm): ";
	private static final String UMENU_1_2_BTIME = "Eingabe Zeit bis (Format: hh:mm): ";
	private static final String UMENU_1_2_PDATE = "Eingabe Datum (Format: DD.MM.YYYY): ";
	private static final String UMENU_1_2_PTIME = "Eingabe Zeitpunkt (Format: hh:mm): ";
	private static final String AMENU_1_0_0_0 = "Wetterdaten abfragen [1]     Benutzerdaten verwalten [2]     Logout [3]     Applikation schliessen [0]\nAuswahl eingeben: ";
	private static final String AMENU_1_2_0_0 = "Benutzer hinzufuegen [1]     Benutzerdaten anpassen [2]    Benutzer loeschen [3]     Alle Benutzer anzeigen [4]     Zurueck [0]\nAuswahl eingeben: ";
	private static final String AMENU_1_2_NAME = "Eingabe Vorname: ";
	private static final String AMENU_1_2_SURNAME = "Eingabe Nachname: ";
	private static final String AMENU_1_2_USERNAME = "Eingabe Benutzername: ";
	private static final String AMENU_1_2_ROLE = "Administrator [1]     Benutzer [2]\nRolle des Benutzers auswaehlen: ";
	private static final String AMENU_1_2_2_ID = "Eingabe Benuzer-ID: ";
	private static final String AMENU_1_2_2_0_EDIT = "Passwort aendern [1]     Benutzername aendern [2]     Vorname aendern [3]     Nachname aendern [4]     Rolle aendern [5]     Zurueck [0]\nAuswahl eingeben: ";
	private static final String AMENU_1_2_2_0_PASSWORD = "Neues Passwort: ";
	private static final String AMENU_1_2_2_0_USERNAME = "Neuer Benutzername: ";
	private static final String AMENU_1_2_2_0_NAME = "Neuer Vorname: ";
	private static final String AMENU_1_2_2_0_SURNAME = "Neuer Nachname: ";
	private static final String AMENU_1_2_2_0_ROLE = "Administrator [1]     Benutzer [2]\nNeue Rolle des Benutzers auswaehlen: ";
	private static final String AMENU_1_2_CRITERIA = "Suchkriterium waehlen:\nBenutzer-ID [1]     Benutzername [2]     Zurueck [0]\nAuswahl eingeben: ";


	private String menu = MENU_LOGIN_USERNAME;
	
	//Wda business = new WdaImpl();
	Wda business = new WdaProxy();
	Benutzer currentUser;
	boolean close;
	String ort;
	LocalDate vonDatumParsed = null; 
	LocalDate bisDatumParsed  = null;
	LocalTime vonZeitParsed = null; 
	LocalTime bisZeitParsed = null;
	String zeitRaum;
	LocalTime zeitPunktParsed;
	LocalDate datumParsed;
	String artDaten; //Fuer den Export relevant.
	boolean exportOrt;
	boolean exportEinzelwerte;
	boolean exportDurchschnitt;
	boolean exportMinMax;
	boolean exportHoechstTiefstTemp;
	boolean exportHoechstTiefstLDruck;
	boolean exportHoechstTiefstLFeucht;
	boolean exit; //gibt an ob der User das Programm beenden will oder nicht. 
	boolean zeitVergangenheit;
	
	
	/**
	 * Starting point of the UI. Method is loading none, all or just updating the weather Data into DB depending on the user Input. 
	 */
	public void executeUIStart() {
		menu = MENU_AKTUALISIEREN;
		showMenu();
		int eingabe = readUserInputChoice();
		//Via Google Search auf Threads gestossen und entsprechend implementiert. 
		Thread backgroundThread = new Thread(new Runnable() {
	        @Override
	        public void run() {
	        	
	        	if(eingabe == 1) {
		        	boolean getData = business.ersterBezugAPI();
		        	if(getData) {
		        		System.out.println();
		        		System.out.println("Wetterdaten erfolgreich geladen -- Willkommen:");
		        	}else {
		        		System.out.println();
		        		System.out.println("Beim laden der Daten ist etwas schief gegangen, bitte Applikation neu starten!");
		        	}
	        	}else if (eingabe == 2) {
	        		boolean getData = business.aktualisierenAPI();
		        	if(getData) {
		        		System.out.println("Wetterdaten errolgreich aktualisiert -- Willkommen:");
		        	}else {
		        		System.out.println("Beim laden der Daten ist etwas schief gegangen, bitte Applikation neu starten!");
		        	}
	        	}

	        }
	    });
	    backgroundThread.start();
	    if(eingabe == 1 || eingabe == 2) {
	    	System.out.println();
		    System.out.println();
		    System.out.println("Bitte habe einen Moment Geduld, bis alle Daten geladen sind.");
		    System.out.print("Loading...\n");
	        for (int i = 1; backgroundThread.isAlive(); i++) {
	        	System.out.print("*");
	        	if(i%50 == 0) {
	        		System.out.println();
	        		
	        	}
	            try {
	                Thread.sleep(350);
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	            }  
	        }
	    }else {
	    	System.out.println("Es wurden keine Daten geladen.");
	    }
	    
	    do {
	    	executeLogin();
	    	switch(business.validiereRolle(currentUser)) {
	    	case "user":
	    		executeUser();
	    		break;
	    	case "admin":
	    		executeAdmin();
	    		break;
	    	default:
	    		System.err.println("User hat keine Rolle, bitte an Admin wenden.");
	    		exit = true;
	    	}
	    }while (exit == false);
        
        System.out.print("Applikation wurde geschlossen, auf Wiedersehen.");
        
}

	/**
	 * Executes the Login of the Client/User. Is called in the executeUIStart method. 
	 */
	public void executeLogin() {
		String eingabe = null;
		String eingabe2 = "";
		menu = MENU_LOGIN_USERNAME;
		showMenu();
		eingabe = readUserInputString();
		do {
			if(menu.equals(MENU_LOGIN_USERNAME)) {
				while(business.validiereBenutzername(eingabe) == null) {
					System.err.println("Der eingegebene Benutzer ist nicht in der Datenbank. Bitte versuche es erneut oder wende dich an den Administrator!");
					showMenu();
					eingabe = readUserInputString();
				}
				currentUser = business.validiereBenutzername(eingabe);
				menu = MENU_LOGIN_PASSWORD;
				
			} else if(menu.equals(MENU_LOGIN_PASSWORD)) {
				if(business.passwortVorhanden(currentUser)==null) {
					System.out.println("Erstes Login, bitte setze zuerst dein Passwort.");
					System.out.print("Passwort setzen: ");
					eingabe = readUserInputString();
					System.out.print("Passwort zur Ueberpruefung erneut eingeben:");
					eingabe2 = readUserInputString();
					while(!(eingabe.equals(eingabe2))) {
						System.err.print("Die eingegebenen Passwoerter stimmen nicht überein, bitte taetige die eingaben erneut.");
						System.out.print("Passwort setzen: ");
						eingabe = readUserInputString();
						System.out.print("Passwort zur Ueberpruefung erneut eingeben: ");
						eingabe2 = readUserInputString();
					}
					
					boolean check = business.passwortSetzen(currentUser, eingabe2);
					if(!check) {
						System.err.print("Setzen Passwort auf Datenbank fehlgeschlagen. Bitte wende dich an den Administrator!");
					}
				}else {
					menu = MENU_LOGIN_PASSWORD;
					showMenu();
					eingabe = readUserInputString();
					while(!(business.validierePasswort(business.passwortVorhanden(currentUser), eingabe))) {
						System.err.print("Falsches Passwort, bitte Eingabe erneut taetigen: ");
						eingabe = readUserInputString();
					}
				}
				//Exit method
				break;
			}
			
			
		}while(true);
		
	}
	/**
	 * Execution of the User-UI. Is called in the executeUIStart method. 
	 */
	public void executeUser() {
		int eingabe = -1;
		if(currentUserAdminCheck()) {
			menu = UMENU_1_1_0;
		}else menu = UMENU_1_0_0;
		showMenu();
		eingabe = readUserInputChoice();
		do {
			if(menu.equals(UMENU_1_0_0)) {
				if(eingabe == 1) {
					menu = UMENU_1_1_0;
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe ==2) {
					exit = false;
					break;
				}else if(eingabe == 0) {
					exit = true;
					break;
				}
			} else if(menu.equals(UMENU_1_1_0)) {
				if(eingabe == 1) {
					menu = UMENU_PEZ_0;
					showMenu();
					eingabe = readUserInputChoice();
					if(eingabe == 2 || eingabe == 1) {
						exportOrt = true; //UMENU_PEZ_0 ist ein Export fuer Ort und Wetterdaten. Zum unterscheiden wird ein Boolischer wert gesetzt. 
					}
				}else if(eingabe ==2) {
					menu = UMENU_1_2_0;
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe ==3) {
					menu = UMENU_1_2_1;
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 0) {
					if(currentUserAdminCheck()) {
						break;
					}
					menu = UMENU_1_0_0;
					showMenu();
					eingabe = readUserInputChoice();
				}
			} else if(menu.equals(UMENU_PEZ_0)) {
				if(eingabe == 1) {					
					exportToFormatBasedOnUserInput(4);
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 2) {
					menu = UMENU_PEZ_1;
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 0) {
					exportOrt = false; //Zuruecksetzen der Variable auf false, welche gelegentlich in UMENU_1_1_0 gesetzt wurde. 
					exportOrt = false;
					exportEinzelwerte = false;
					exportDurchschnitt = false;
					exportMinMax = false;
					exportHoechstTiefstTemp = false;
					exportHoechstTiefstLDruck = false;
					exportHoechstTiefstLFeucht = false;
					menu = UMENU_1_1_0;
					showMenu();
					eingabe = readUserInputChoice();
				}
				
			} else if(menu.equals(UMENU_1_2_0)) {
				if(eingabe == 1) {
					menu = UMENU_1_2_PLACE;
					showMenu();
					ort = validiereOrt(readUserInputString());
					menu = UMENU_1_2_VDATE;
					showMenu();
					vonDatumParsed = validateLocalDateFormat(readUserInputString());
					menu = UMENU_1_2_BDATE;
					showMenu();
					bisDatumParsed = validateLocalDateFormat(readUserInputString());
					menu = UMENU_1_2_TIME;
					showMenu();
					eingabe = readUserInputChoice();
					exportEinzelwerte = true;
				}else if(eingabe == 2) {
					menu = UMENU_1_2_PLACE;
					showMenu();
					ort = validiereOrt(readUserInputString());
					menu = UMENU_1_2_VDATE;
					showMenu();
					vonDatumParsed = validateLocalDateFormat(readUserInputString());
					menu = UMENU_1_2_BDATE;
					showMenu();
					bisDatumParsed = validateLocalDateFormat(readUserInputString());
					menu = UMENU_1_2_TIME;
					showMenu();
					eingabe = readUserInputChoice();
					exportDurchschnitt = true;
				}else if(eingabe == 3) {
					menu = UMENU_1_2_PLACE;
					showMenu();
					ort = validiereOrt(readUserInputString());
					menu = UMENU_1_2_VDATE;
					showMenu();
					vonDatumParsed = validateLocalDateFormat(readUserInputString());;
					menu = UMENU_1_2_BDATE;
					showMenu();
					bisDatumParsed = validateLocalDateFormat(readUserInputString());
					menu = UMENU_1_2_TIME;
					showMenu();
					eingabe = readUserInputChoice();
					exportMinMax = true;
				}else if(eingabe == 0) {
					menu = UMENU_1_1_0;
					showMenu();
					eingabe = readUserInputChoice();
				}
				
			} else if(menu.equals(UMENU_PEZ_1)) {
				if(eingabe == 1) {
					exportToFormatBasedOnUserInput(eingabe);
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 2) {
					exportToFormatBasedOnUserInput(eingabe);
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 3) {
					exportToFormatBasedOnUserInput(eingabe);
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 0) {
					menu = UMENU_PEZ_0;
					showMenu();
					eingabe = readUserInputChoice();
				}
				
			} else if(menu.equals(UMENU_1_2_TIME)) {
				if(eingabe == 1) {
					menu = UMENU_1_2_VTIME;
					showMenu();
					vonZeitParsed = validateLocalTimeFormat(readUserInputString());
					zeitVergangenheit = checkTimeInPast(vonDatumParsed, vonZeitParsed);
					while(!zeitVergangenheit){
						System.out.println("Die von dir eingegebene Zeit liegt in der Zukunft. Bitte die aktuelle oder eine vergagene Zeit eingeben. ");
						showMenu();
						vonZeitParsed = validateLocalTimeFormat(readUserInputString());
						zeitVergangenheit = checkTimeInPast(vonDatumParsed, vonZeitParsed);
					}
					menu = UMENU_1_2_BTIME;
					showMenu();
					bisZeitParsed = validateLocalTimeFormat(readUserInputString());
					zeitVergangenheit = checkTimeInPast(bisDatumParsed, bisZeitParsed);
					while(!zeitVergangenheit){
						System.out.println("Die von dir eingegebene Zeit liegt in der Zukunft. Bitte die aktuelle oder eine vergagene Zeit eingeben. ");
						showMenu();
						bisZeitParsed = validateLocalTimeFormat(readUserInputString());
						zeitVergangenheit = checkTimeInPast(bisDatumParsed, bisZeitParsed);
					}
					menu = UMENU_PEZ_0;
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 2) {
					vonZeitParsed = validateLocalTimeFormat("00:00");
					bisZeitParsed = validateLocalTimeFormat("23:59");
					menu = UMENU_PEZ_0;
					showMenu();
					eingabe = readUserInputChoice();
				}
			} else if(menu.equals(UMENU_1_2_1)) {
				if(eingabe == 1) {
					menu = UMENU_1_2_PDATE;
					showMenu();
					String datum = readUserInputString();
					datumParsed = validateLocalDateFormat(datum);
					
					menu = UMENU_1_2_PTIME;
					showMenu();
					String zeitPunkt = readUserInputString();
					zeitPunktParsed = validateLocalTimeFormat(zeitPunkt);
					zeitVergangenheit = checkTimeInPast(datumParsed, zeitPunktParsed);
					while(!zeitVergangenheit){
						System.out.println("Die von dir eingegebene Zeit liegt in der Zukunft. Bitte die aktuelle oder eine vergagene Zeit eingeben. ");
						showMenu();
						zeitPunktParsed = validateLocalTimeFormat(readUserInputString());
						zeitVergangenheit = checkTimeInPast(datumParsed, zeitPunktParsed);
					}
					
					menu = UMENU_PEZ_0;
					showMenu();
					eingabe = readUserInputChoice();
					exportHoechstTiefstTemp = true;
					
				}else if(eingabe ==2) {
					menu = UMENU_1_2_PDATE;
					showMenu();
					String datum = readUserInputString();
					datumParsed = validateLocalDateFormat(datum);
					
					menu = UMENU_1_2_PTIME;
					showMenu();
					String zeitPunkt = readUserInputString();
					zeitPunktParsed = validateLocalTimeFormat(zeitPunkt);
					zeitVergangenheit = checkTimeInPast(datumParsed, zeitPunktParsed);
					while(!zeitVergangenheit){
						System.out.println("Die von dir eingegebene Zeit liegt in der Zukunft. Bitte die aktuelle oder eine vergagene Zeit eingeben. ");
						showMenu();
						zeitPunktParsed = validateLocalTimeFormat(readUserInputString());
						zeitVergangenheit = checkTimeInPast(datumParsed, zeitPunktParsed);
					}
					
					menu = UMENU_PEZ_0;
					showMenu();
					eingabe = readUserInputChoice();
					exportHoechstTiefstLDruck = true;
				}else if(eingabe ==3) {
					menu = UMENU_1_2_PDATE;
					showMenu();
					String datum = readUserInputString();
					datumParsed = validateLocalDateFormat(datum);
					
					menu = UMENU_1_2_PTIME;
					showMenu();
					String zeitPunkt = readUserInputString();
					zeitPunktParsed = validateLocalTimeFormat(zeitPunkt);
					zeitVergangenheit = checkTimeInPast(datumParsed, zeitPunktParsed);
					while(!zeitVergangenheit){
						System.out.println("Die von dir eingegebene Zeit liegt in der Zukunft. Bitte die aktuelle oder eine vergagene Zeit eingeben. ");
						showMenu();
						zeitPunktParsed = validateLocalTimeFormat(readUserInputString());
						zeitVergangenheit = checkTimeInPast(datumParsed, zeitPunktParsed);
					}
					
					menu = UMENU_PEZ_0;
					showMenu();
					eingabe = readUserInputChoice();
					exportHoechstTiefstLFeucht = true;
				}else if(eingabe == 0) {
					menu = UMENU_1_1_0;
					showMenu();
					eingabe = readUserInputChoice();
				}
			}
			
		}while (true);
	}
	
	/**
	 * Execution of the Admin-UI. Is called in the executeUIStart method. 
	 * During the method the Admin can also get the weather data like the user. For this the 
	 * executeUser method is called to reduce redundant code. 
	 */
	public void executeAdmin() {
		String vorname;
		String nachname;
		String benutzername;
		String rolleString;
		int rolle;
		Benutzer benutzerAendern = null;
		int eingabeId;
		int eingabe = -1;
		menu = AMENU_1_0_0_0;
		showMenu();
		eingabe = readUserInputChoice();
		do {
			if(menu.equals(AMENU_1_0_0_0)) {
				if(eingabe == 1) {
					executeUser();
					menu = AMENU_1_0_0_0;
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 2) {
					menu = AMENU_1_2_0_0;
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 3) {
					exit = false;
					break;
				}else if(eingabe == 0) {
					exit = true;
					break;
				}
				
			} else if(menu.equals(AMENU_1_2_0_0)) {
				if(eingabe == 1) {
					menu = AMENU_1_2_NAME;
					showMenu();
					vorname = readUserInputString();
					menu = AMENU_1_2_SURNAME;
					showMenu();
					nachname = readUserInputString();
					menu = AMENU_1_2_USERNAME;
					showMenu();
					benutzername = readUserInputString();
					menu = AMENU_1_2_ROLE;
					showMenu();
					rolle = readUserInputChoice();
					if(rolle == 1 ) {
						rolleString = "admin";
					}else {
						rolleString = "user";
					}
					Benutzer bAdd = new Benutzer(benutzername, vorname, nachname, rolleString);
					bAdd = business.hinzufuegenBenutzer(bAdd);
					if(bAdd != null) {
						System.out.println("Benutzer erfolgreich hinzugefuegt");
					}else {
						System.out.println("Benutzer wurde nicht hinzugefügt. Benutzer bereits registriert.");
					}
					menu = AMENU_1_2_0_0;
					showMenu();
					eingabe = readUserInputChoice();
					
				}else if(eingabe == 2) {
					menu = AMENU_1_2_2_ID;
					showMenu();
					eingabeId = readUserInputInt();
					benutzerAendern = business.findeBenutzerById(eingabeId);
					while(benutzerAendern == null) {
						System.out.print("Benutzer mit id " + eingabeId + " ist in der DB nicht vorhanden. \nEingabe erneut taetigen: ");
						eingabeId = readUserInputInt();
						benutzerAendern = business.findeBenutzerById(eingabeId);
					}
					menu = AMENU_1_2_2_0_EDIT;
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 3) {
					menu = AMENU_1_2_2_ID;
					showMenu();
					eingabeId = readUserInputInt();
					Benutzer benutzerLoeschen = business.findeBenutzerById(eingabeId);
					while(benutzerLoeschen == null) {
						System.out.print("Benutzer mit id " + eingabeId + " ist in der DB nicht vorhanden. \nEingabe erneut taetigen: ");
						eingabeId = readUserInputInt();
						benutzerLoeschen = business.findeBenutzerById(eingabeId);
					}
					Benutzer geloeschterBenutzer = business.loeschenBenutzer(benutzerLoeschen);
					if(geloeschterBenutzer == null) {
						System.err.print("Benutzer konnte nicht geloescht werden. Bitte wende dich an den Administrator.");
					}else {
						System.out.println("Benutzer mit der ID "+ eingabeId + " wurde erfolgreich geloescht.");
					}
					menu = AMENU_1_2_0_0;
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 4) {
					for(Benutzer b : business.alleBenutzer()) {
						System.out.println(b);
					}
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 0) {
					menu = AMENU_1_0_0_0;
					showMenu();
					eingabe = readUserInputChoice();
				}
			}else if(menu.equals(AMENU_1_2_2_0_EDIT)) {
				if(eingabe == 1) {
					menu = AMENU_1_2_2_0_PASSWORD;
					showMenu();
					String updatedPassword = readUserInputString();
					System.out.print("Passwort zur Überpruefung erneut eingeben: ");
					String wiederholung = readUserInputString();
					while(!(updatedPassword.equals(wiederholung))) {
						System.err.print("Die eingegebenen Passwoerter stimmen nicht überein, bitte taetige die eingaben erneut.");
						System.out.print("Passwort eingeben: ");
						updatedPassword = readUserInputString();
						System.out.print("Passwort zur Überpruefung erneut eingeben:");
						wiederholung = readUserInputString();
					}
					benutzerAendern = business.anpassenPasswort(benutzerAendern, updatedPassword);
					if(benutzerAendern == null) {
						System.err.print("Passwort konnte nicht geaendert werden, bitte melde dich an den Administrator.");
					}else System.out.println("Passwort erfolgreich geaendert.");
					menu = AMENU_1_2_2_0_EDIT;
					showMenu();
					eingabe = readUserInputChoice();
					
				}else if(eingabe == 2) {
					menu = AMENU_1_2_2_0_USERNAME;
					showMenu();
					String updatedBenutzername = readUserInputString();
					benutzerAendern = business.anpassenBenutzername(benutzerAendern, updatedBenutzername);
					if(benutzerAendern == null) {
						System.err.print("Benutzername konnte nicht geaendert werden, bitte wende dich an den Administrator.");
					}else System.out.println("Benutzername erfolgreich geaendert.");
					menu = AMENU_1_2_2_0_EDIT;
					showMenu();
					eingabe = readUserInputChoice();
					
				}else if(eingabe == 3) {
					menu = AMENU_1_2_2_0_NAME;
					showMenu();
					String updatedName = readUserInputString();
					benutzerAendern = business.anpassenVorname(benutzerAendern, updatedName);
					if(benutzerAendern == null) {
						System.err.print("Vorname konnte nicht geaendert werden, bitte wende dich an den Administrator.");
					}else System.out.println("Vorname erfolgreich geaendert.");
					menu = AMENU_1_2_2_0_EDIT;
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 4) {
					menu = AMENU_1_2_2_0_SURNAME;
					showMenu();
					String updatedNachname = readUserInputString();
					benutzerAendern = business.anpassenNachname(benutzerAendern, updatedNachname);
					if(benutzerAendern == null) {
						System.err.print("Nachname konnte nicht geaendert werden, bitte wende dich an den Administrator.");
					}else System.out.println("Nachname erfolgreich geaendert.");
					menu = AMENU_1_2_2_0_EDIT;
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 5) {
					menu = AMENU_1_2_2_0_ROLE;
					showMenu();
					int choice = readUserInputChoice();
					String updatedRolle;
					if(choice == 1) {
						updatedRolle = "admin";
					}else {
						updatedRolle = "user";
					}
					benutzerAendern = business.anpassenRolle(benutzerAendern, updatedRolle);
					if(benutzerAendern == null) {
						System.err.print("Rolle konnte nicht geaendert werden, bitte wende dich an den Administrator.");
					}else System.out.println("Rolle erfolgreich geaendert.");
					menu = AMENU_1_2_2_0_EDIT;
					showMenu();
					eingabe = readUserInputChoice();
				}else if(eingabe == 0) {
					menu = AMENU_1_2_0_0;
					showMenu();
					eingabe = readUserInputChoice();
				}
			}
			
			
		}while(true);
	}
	
	
	/**
	 * Method is called during UI executions. Prints out the menu that is currently set. 
	 */
	private void showMenu() {
		System.out.println();
		System.out.print(menu);
	}
	
	/**
	 * Method reads all integer user Inputs of tha user for the according menu. 
	 * @see executeUser or executeAdmin inputs after setting a menu.
	 * Also checks if for the current menu the choice is avilable and if not gives prints out an according feedback 
	 * to the user.
	 * 
	 * @return returns the Input that the user has typed in. 
	 */
	private int readUserInputChoice() {
		Scanner sc = new Scanner(System.in);

		int input = -1;

		List<Integer> values = new ArrayList<>();

		switch (menu) {
		case MENU_AKTUALISIEREN:
			values = Arrays.asList(1, 2, 0);
			break;
			
		case UMENU_1_0_0:
			values = Arrays.asList(1, 2, 0);
			break;

		case UMENU_1_1_0:
			values = Arrays.asList(1, 2, 3, 0);
			break;

		case UMENU_PEZ_0:
			values = Arrays.asList(1, 2, 0);
			break;

		case UMENU_PEZ_1:
			values = Arrays.asList(1, 2, 3, 0);
			break;

		case UMENU_1_2_0:
			values = Arrays.asList(1, 2, 3, 0);
			break;

		case UMENU_1_2_TIME:
			values = Arrays.asList(1, 2);
			break;

		case AMENU_1_0_0_0:
			values = Arrays.asList(1, 2, 3, 0);
			break;

		case AMENU_1_2_0_0:
			values = Arrays.asList(1, 2, 3, 4, 0);
			break;

		case AMENU_1_2_CRITERIA:
			values = Arrays.asList(1, 2, 0);
			break;

		case AMENU_1_2_2_0_EDIT:
			values = Arrays.asList(1, 2, 3, 4, 5, 0);
			break;
		case AMENU_1_2_ROLE:
			values = Arrays.asList(1, 2);
			break;
		case AMENU_1_2_2_0_ROLE:
			values = Arrays.asList(1, 2);
			break;
		case UMENU_1_2_1:
			values = Arrays.asList(1, 2, 3, 0);
		}
		do {
			try {
				input = sc.nextInt();

				if (!values.contains(input)) {
					System.out.println("Ungueltige Auswahl. Eingabe erneut taetigen!");
					showMenu();
				}
			} catch (Exception e) {
				sc.nextLine();
				System.out.println("Upps, das sollte nicht passieren. Bitte erneut versuchen!");
				showMenu();
			}
		} while (!values.contains(input));
		return input;

	}

	/**
	 * Reads all inputs during UI-Execution that are Strings. Also validates the String to not be empty.
	 * @return returns the String the user has given during execution.
	 */
	private String readUserInputString() {

		Scanner sc = new Scanner(System.in);
		String input = "";
		
		do {
		try {
			input = sc.nextLine();
			
			if(input == "") {
				System.out.println("Ungueltige Eingabe, bitte aendern!");
				showMenu();
			}
		} catch (Exception e) {
			sc.nextLine();
			System.out.println("Upps, das sollte nicht passieren. Bitte erneut versuchen!");
			showMenu();
		}
		}while(input == "");
		//sc.close();
		return input;
	}
	
	/**
	 * Reads all inputs during UI-Execution that are Integers and have nothing to do with a menu. Also validates the integer to not be negativ.
	 * @return returns the integer the user has given during execution.
	 */
	private int readUserInputInt() {

		Scanner sc = new Scanner(System.in);
		int input = -10;
		
		do {
		try {
			input = sc.nextInt();
			
			if(input < 0) {
				System.out.println("Ungueltige Eingabe, bitte aendern!");
				showMenu();
			}
		} catch (Exception e) {
			sc.nextInt();
			System.out.println("Upps, das sollte nicht passieren. Bitte erneut versuchen!");
			showMenu();
		}
		}while(input < 0);
		return input;
	}
	
	/**
	 * Method formats a given String into a LocalDate
	 * @param LocalDate
	 * @return String formated into a localDate-Object
	 * 
	 */
	private LocalDate formaterToLocalDate(String localDate) {
		//DateTimeFormatter mit Inspiration von ChatGPT erstellt (kein copy paste) (DM)
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		LocalDate datumFormatiert = LocalDate.parse(localDate, dateFormatter);
		return datumFormatiert;
	}
	/**
	 * Method formats a given String into a LocalTime
	 * @return String formated into a LocalTime-Object
	 */
	private LocalTime formaterToLocalTime(String localTime) {
		//DateTimeFormatter mit Inspiration von ChatGPT erstellt (kein copy paste) (DM)
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime timeFormatiert = LocalTime.parse(localTime, timeFormatter);
		return timeFormatiert;
		
	}
	/**
	 * Method is called in executeUser(). The Method calls the 'abrufen...' and 'export...' method from the proxy class.
	 * Therefore the export will be executed depending on the exportFormat. 
	 * @param exportFormat shows which export format the user wants the data to be saved/printed.
	 */
	private void exportToFormatBasedOnUserInput(int exportFormat) {
		if(exportOrt) { //Boolischer Wert welcher im Menu UMENU_PEZ_0 gesetzt wird, falls True will der user Orte exportieren
			business.ortschaftenExport(business.alleOrtschaften(), exportFormat);
		}else if(exportEinzelwerte) {
				zeitRaum = " Von " + vonDatumParsed.toString() + " " + vonZeitParsed.toString() + " bis " + bisDatumParsed.toString() + " " + bisZeitParsed.toString();
				artDaten = "Einzel";
				List<WetterDaten> wdEinzelwerte = business.abrufenEinzelwerte(ort, vonDatumParsed, bisDatumParsed, vonZeitParsed, bisZeitParsed);
				business.wetterDatenExportMinMaxEinzel(wdEinzelwerte, exportFormat, zeitRaum, artDaten);
			}else if(exportDurchschnitt){
				zeitRaum = " Von " + vonDatumParsed.toString() + " " + vonZeitParsed.toString() + " bis " + bisDatumParsed.toString() + " " + bisZeitParsed.toString();
				WetterDaten wdDurchschnittswerte = business.abrufenDurchschnittswerte(business.abrufenEinzelwerte(ort, vonDatumParsed, bisDatumParsed, vonZeitParsed, bisZeitParsed));
				business.wetterDatenExportDurchschnitt(wdDurchschnittswerte, exportFormat, zeitRaum);
			}else if(exportMinMax) {
				zeitRaum = " Von " + vonDatumParsed.toString() + " " + vonZeitParsed.toString() + " bis " + bisDatumParsed.toString() + " " + bisZeitParsed.toString();
				artDaten = "Min/Max";
				List<WetterDaten> wdListMinMax = business.abrufenMinMax(business.abrufenEinzelwerte(ort, vonDatumParsed, bisDatumParsed, vonZeitParsed, bisZeitParsed));
				business.wetterDatenExportMinMaxEinzel(wdListMinMax, exportFormat, zeitRaum, artDaten);
			}else if(exportHoechstTiefstTemp) {
				zeitRaum = " Am " + datumParsed.toString() + " um " + zeitPunktParsed.toString();
				artDaten = "Temperatur";
				List<WetterDaten> wdListOrtTemp = business.minMaxOrt(datumParsed, zeitPunktParsed, artDaten);
				business.minMaxOrtExport(wdListOrtTemp, zeitRaum, exportFormat, artDaten);
			}else if(exportHoechstTiefstLDruck) {
				zeitRaum = " Am " + datumParsed.toString() + " um " + zeitPunktParsed.toString();
				artDaten = "LuftDruck";
				List<WetterDaten> wdListOrtDruck = business.minMaxOrt(datumParsed, zeitPunktParsed, artDaten);
				business.minMaxOrtExport(wdListOrtDruck, zeitRaum, exportFormat, artDaten);
			}else if(exportHoechstTiefstLFeucht) {
				zeitRaum = " Am " + datumParsed.toString() + " um " + zeitPunktParsed.toString();
				artDaten = "LuftFeuchtigkeit";
				List<WetterDaten> wdListOrtFeucht = business.minMaxOrt(datumParsed, zeitPunktParsed, artDaten);
				business.minMaxOrtExport(wdListOrtFeucht, zeitRaum, exportFormat, artDaten);
			}else {
				System.err.println("Fehler im UI. Es wurden keine Daten Exportiert, bitte melde dich beim Administrator!");
			}
	}
	
	/**
	 * Tries to parse the String that is given into a localDate. The Method formatToLocalDate() is used for parsing.
	 * Depending on the success of the parsing a message will be printed out to the user.
	 * @param datumInString
	 * @return localDate-Object
	 */
	private LocalDate validateLocalDateFormat(String datumInString) {
		boolean umwandlungErfolg = false;
		while(!umwandlungErfolg) {
			try {
				datumParsed = formaterToLocalDate(datumInString);
				if(datumParsed.isAfter(LocalDate.now())) {
					Exception e = new Exception();
					throw e;
				}
				umwandlungErfolg = true;
				return datumParsed;
			}catch(DateTimeParseException e) {
				System.out.println("Falsches Format, bitte eingabe wiederholen. ");
				showMenu();
				datumInString = readUserInputString();
			}catch(Exception e) {
				System.out.println("Das eingegebene Datum ist in der Zukunft, bitte Datum von heute oder der Vergangenheit eintragen. ");
				showMenu();
				datumInString = readUserInputString();
			}
		}
		return datumParsed;
	}
	
	/**
	 * Tries to parse the String that is given into a localTime. The Method formatToLocalTime() is used for parsing.
	 * Depending on the success of the parsing a message will be printed out to the user.
	 * @param zeitPunktInString
	 * @return localTime-Object
	 */
	private LocalTime validateLocalTimeFormat(String zeitPunktInString) {
		boolean umwandlungErfolg = false;
		while(!umwandlungErfolg) {
			try {
				zeitPunktParsed = formaterToLocalTime(zeitPunktInString);
				umwandlungErfolg = true;
				return zeitPunktParsed;
			}catch(DateTimeParseException e) {
				System.out.println("Falsches Format, bitte eingabe wiederholen. ");
				showMenu();
				zeitPunktInString = readUserInputString();
			}
		}
		return zeitPunktParsed;
	}
	
	/**
	 * Calls method in proxy 'ortVorhanden' do validate if the given String is a valid city that is in the DB.
	 * If not user has to input the city again. 
	 * @param ort is the city that has to be checked.
	 * @return the final city as a String that was given by the user
	 */
	private String validiereOrt(String ort) {
		while(!business.ortVorhanden(ort)) {
			System.out.println("Den eingegebenen Ort gibt es nicht. Bitte eingabe anpassen.");
			showMenu();
			ort = readUserInputString();
		}
		return ort;
	}
	
	/**
	 * Method checks if the currentUser (calss variable) is an Administrator.
	 * @return returns true if the user is a administrator
	 */
	private boolean currentUserAdminCheck() {
		return business.validiereRolle(this.currentUser).equals("admin");
	}
	
	private boolean checkTimeInPast(LocalDate datum, LocalTime zeit) {
		if(datum.isEqual(LocalDate.now())) {
			if(zeit.isAfter(LocalTime.now())) {
				return false;
			}else return true;
		}else return true;
	}
	
}
