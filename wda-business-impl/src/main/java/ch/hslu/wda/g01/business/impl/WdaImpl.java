package ch.hslu.wda.g01.business.impl;

import ch.hslu.wda.g01.business.api.Wda;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import ch.hslu.swde.wda.g01.domain.*;
import ch.hslu.swde.wda.g01.persister.api.*;
import ch.hslu.swde.wda.g01.persister.impl.*;
import ch.hslu.swde.wda.g01.persister.util.JpaUtil;
import ch.hslu.swde.wda.g01.reader.rws.proxy.*;

/**
 * Business Logic of the Wda application.
 * 
 * 
 * @author Dario Meyer, Mathis Trautmann, Agata Ciesielska, Nico Graf
 * @version 1.0.0
 */
public class WdaImpl implements Wda {

	public WdaImpl() {
//		BenutzerDao bPersister = new BenutzerDaoImpl();
//		Benutzer admin = new Benutzer();
//		admin.setBenutzerName("admin");
//		admin.setPasswort("admin");
//		admin.setRolle("admin");
//		bPersister.speichern(admin);
	}

	/**
	 * Method to get all the users From the DB.
	 * @return All the users that are in the db as a List of Benutzer-Objects.
	 */
	public List<Benutzer> alleBenutzer() {

		BenutzerDao bPersister = new BenutzerDaoImpl();

		return bPersister.alle(Benutzer.class);

	}

	/**
	 * Method to add a new User to the DB.
	 * @return returns the created user or null if not created successfully.
	 */
	public Benutzer hinzufuegenBenutzer(Benutzer benutzer) {

		BenutzerDao bPersister = new BenutzerDaoImpl();
		if (bPersister.findByBenutzername(benutzer.getBenutzerName()) == null) {
			Benutzer gespeicherterBenutzer = bPersister.speichern(benutzer);
			return gespeicherterBenutzer;
		} else {
			return null;
		}
	}


	@Override
	public Benutzer loeschenBenutzer(Benutzer benutzer) {
		BenutzerDao bPersister = new BenutzerDaoImpl();

		if (bPersister.find(Benutzer.class, benutzer.getId()).getBenutzerName() != null) {
			Benutzer geloeschterBenutzer = bPersister.loeschen(benutzer);
			return geloeschterBenutzer;
		} else {
			return null;
		}
	}

	@Override
	public Benutzer anpassenBenutzer(Benutzer benutzer) {

		BenutzerDao bPersister = new BenutzerDaoImpl();

		if (bPersister.find(Benutzer.class, benutzer.getId()).getBenutzerName() != null) {
			Benutzer aktualisierterBenutzer = bPersister.aktualisieren(benutzer);
			return aktualisierterBenutzer;
		} else {
			return null;
		}
	}

	@Override
	public Benutzer anpassenBenutzername(Benutzer benutzer, String benutzername) {
		BenutzerDao bPersister = new BenutzerDaoImpl();

		if (bPersister.find(Benutzer.class, benutzer.getId()).getBenutzerName() != null) {
			benutzer.setBenutzerName(benutzername);
			Benutzer aktualisierterBenutzer = bPersister.aktualisieren(benutzer);
			return aktualisierterBenutzer;
		} else {
			return null;
		}
	}

	@Override
	public Benutzer anpassenNachname(Benutzer benutzer, String nachname) {
		BenutzerDao bPersister = new BenutzerDaoImpl();

		if (bPersister.find(Benutzer.class, benutzer.getId()).getBenutzerName() != null) {
			benutzer.setNachName(nachname);
			Benutzer aktualisierterBenutzer = bPersister.aktualisieren(benutzer);
			return aktualisierterBenutzer;
		} else {
			return null;
		}
	}

	@Override
	public Benutzer anpassenVorname(Benutzer benutzer, String vorname) {
		BenutzerDao bPersister = new BenutzerDaoImpl();

		if (bPersister.find(Benutzer.class, benutzer.getId()).getBenutzerName() != null) {
			benutzer.setVorName(vorname);
			Benutzer aktualisierterBenutzer = bPersister.aktualisieren(benutzer);
			return aktualisierterBenutzer;
		} else {
			return null;
		}
	}

	@Override
	public Benutzer anpassenPasswort(Benutzer benutzer, String passwort) {
		BenutzerDao bPersister = new BenutzerDaoImpl();

		if (bPersister.find(Benutzer.class, benutzer.getId()).getBenutzerName() != null) {
			benutzer.setPasswort(passwort);
			Benutzer aktualisierterBenutzer = bPersister.aktualisieren(benutzer);
			return aktualisierterBenutzer;
		} else {
			return null;
		}
	}

	@Override
	public Benutzer anpassenRolle(Benutzer benutzer, String rolle) {

		BenutzerDao bPersister = new BenutzerDaoImpl();

		if (bPersister.find(Benutzer.class, benutzer.getId()).getBenutzerName() != null) {
			benutzer.setRolle(rolle);
			Benutzer aktualisierterBenutzer = bPersister.aktualisieren(benutzer);
			return aktualisierterBenutzer;
		} else {
			return null;
		}
	}

	@Override
	public List<Ort> alleOrtschaften() {

		OrtDao oPersister = new OrtDaoImpl();
		List<Ort> ortListe = oPersister.alle(Ort.class);
		if (!(ortListe.isEmpty())) {
			return ortListe;
		} else {
			return new ArrayList<Ort>();
		}
	}

	@Override
	public void ortschaftenExport(List<Ort> orte, int exportDatentyp) {

		if (!orte.isEmpty()) {
			switch (exportDatentyp) {
			case 4:
				for (Ort o : orte) {
					System.out.println(o.toString());
				}
				break;
			case 2: // CSV
				final char delemiter = ',';

				File fileCSV = new File(System.getProperty("user.home") + File.separator + "ortDaten.csv");

				try (PrintWriter printer = new PrintWriter(fileCSV)) {

					StringBuilder sBuilder = new StringBuilder();

					sBuilder.append("ID").append(delemiter).append("PLZ").append(delemiter).append("NAME");

					printer.println(sBuilder.toString());

					sBuilder.setLength(0);

					for (Ort o : orte) {

						sBuilder.append(o.getId()).append(delemiter).append(o.getZip()).append(delemiter)
								.append(o.getName());

						printer.println(sBuilder.toString());

						sBuilder.setLength(0);
					}

					System.out.println("\nDaten exportiert: " + fileCSV.getAbsolutePath());
				} catch (FileNotFoundException e) {
					// Logging
					System.err.println("ERROR:\nDaten konnten nicht exportiert werden!");
				}

				break;
			case 1: // PDF
				// Der PDF-Export wurde mithilfe von ChatGPT programmiert. Code wurde
				// verstanden.(MT)
				try {

					// Aufsetzen des leeren PDF-Dokuments
					PDDocument document = new PDDocument();

					PDPage page = new PDPage();

					document.addPage(page);

					PDPageContentStream contentStream = new PDPageContentStream(document, page);

					// Erstmalige Formatierung des leeren Dokuments
					float yStart = page.getMediaBox().getHeight() - 50;
					float margin = 50;
					float width = page.getMediaBox().getWidth() - 2 * margin;
					float yPosition = yStart;
					float idWidth = 30;
					float ortWidth = 100;
					float zipWidth = 40;

					// Tabellenkopf schreiben
					contentStream.beginText();
					contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
					contentStream.newLineAtOffset(margin, yPosition);
					contentStream.showText("ID");
					contentStream.newLineAtOffset(idWidth, 0);
					contentStream.showText("ORT");
					contentStream.newLineAtOffset(ortWidth, 0);
					contentStream.showText("PLZ");
					contentStream.newLineAtOffset(zipWidth, 0);
					contentStream.endText();
					yPosition -= 20;

					// Werte einfügen
					contentStream.setFont(PDType1Font.HELVETICA, 12);
					for (Ort o : orte) {
						contentStream.beginText();
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText(String.format("%-8d", o.getId()));
						contentStream.newLineAtOffset(idWidth, 0);
						contentStream.showText(String.format("%-20s", o.getName()));
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText(String.format("%-6d", o.getZip()));
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.endText();
						yPosition -= 15;
					}

					contentStream.close();

					// Dokument in File speichern und exportieren
					File filePDF = new File(System.getProperty("user.home") + File.separator + "ortDaten.pdf");
					document.save(filePDF);
					document.close();

					System.out.println("\nDaten exportiert: " + filePDF.getAbsolutePath());
				} catch (IOException e) {
					// Logging
					System.err.println("ERROR:\nDaten konnten nicht exportiert werden!");
				}
				break;
			case 3: // JSON
				try {
					JsonMapper jsonMapper = JsonMapper.builder().build();
					File fileJSON = new File(System.getProperty("user.home") + File.separator + "ortDaten.json");

					jsonMapper.writeValue(fileJSON, orte);

					System.out.println("\nDaten exportiert: " + fileJSON.getAbsolutePath());
				} catch (IOException e) {
					// Logging
					System.err.println("ERROR: Daten konnten nicht exportiert werden.");
				}

			}

		}

	}

	@Override
	public List<WetterDaten> abrufenEinzelwerte(String ort, LocalDate datumVon, LocalDate datumBis, LocalTime zeitVon,
			LocalTime zeitBis) {

		final EntityManager em = JpaUtil.createEntityManager();
		TypedQuery<WetterDaten> tQry = em.createNamedQuery("WetterDaten.findByPlaceDateTime", WetterDaten.class);
		tQry.setParameter("name", ort);
		if (datumVon.compareTo(datumBis) < 0) {
			tQry.setParameter("datumVon", datumVon);
			tQry.setParameter("datumBis", datumBis);
		} else {
			tQry.setParameter("datumVon", datumBis);
			tQry.setParameter("datumBis", datumVon);
		}
		if (zeitVon.compareTo(zeitBis) < 0) {
			tQry.setParameter("uhrZeitVon", zeitVon);
			tQry.setParameter("uhrZeitBis", zeitBis);
		} else {
			tQry.setParameter("uhrZeitVon", zeitBis);
			tQry.setParameter("uhrZeitBis", zeitVon);
		}

		List<WetterDaten> resListe = tQry.getResultList();

		if (em.isOpen()) {
			em.close();
		}

		return resListe != null ? resListe : new ArrayList<WetterDaten>();
	}

	@Override
	public WetterDaten abrufenDurchschnittswerte(List<WetterDaten> einzelWerte) {

		if (!einzelWerte.isEmpty()) {

			WetterDaten res = new WetterDaten();
			int summeLuftDruck = 0;
			int summeLuftFeuchtigkeit = 0;
			double summeTemperatur = 0;

			for (WetterDaten wd : einzelWerte) {
				summeLuftDruck += wd.getLuftDruck();
				summeLuftFeuchtigkeit += wd.getLuftFeuchtigkeit();
				summeTemperatur += wd.getTemperatur();
				res.setOrt(wd.getOrt());
			}
			res.setLuftDruck(summeLuftDruck / einzelWerte.size());
			res.setLuftFeuchtigkeit(summeLuftFeuchtigkeit / einzelWerte.size());
			res.setTemperatur(summeTemperatur / einzelWerte.size());
			
			return res;

		}
		return null;
	}

	@Override
	public List<WetterDaten> abrufenMinMax(List<WetterDaten> einzelWerte) {

		WetterDaten maxRes = new WetterDaten();
		WetterDaten minRes = new WetterDaten();
		List<WetterDaten> res = new ArrayList<>();

		if (!einzelWerte.isEmpty()) {
			for (WetterDaten wetterDaten : einzelWerte) {
				if (maxRes.getLuftDruck() < wetterDaten.getLuftDruck()) {
					maxRes.setLuftDruck(wetterDaten.getLuftDruck());
				}
				if (maxRes.getLuftFeuchtigkeit() < wetterDaten.getLuftFeuchtigkeit()) {
					maxRes.setLuftFeuchtigkeit(wetterDaten.getLuftFeuchtigkeit());
				}
				if (maxRes.getTemperatur() < wetterDaten.getTemperatur()) {
					maxRes.setTemperatur(wetterDaten.getTemperatur());
				}
				if (minRes.getLuftDruck() > wetterDaten.getLuftDruck() || minRes.getLuftDruck() == 0) {
					minRes.setLuftDruck(wetterDaten.getLuftDruck());
				}
				if (minRes.getLuftFeuchtigkeit() > wetterDaten.getLuftFeuchtigkeit()
						|| minRes.getLuftFeuchtigkeit() == 0) {
					minRes.setLuftFeuchtigkeit(wetterDaten.getLuftFeuchtigkeit());
				}
				if (minRes.getTemperatur() > wetterDaten.getTemperatur() || minRes.getTemperatur() == 0) {
					minRes.setTemperatur(wetterDaten.getTemperatur());
				}
			}

			maxRes.setOrt(einzelWerte.get(0).getOrt());
			minRes.setOrt(einzelWerte.get(0).getOrt());
			res.add(minRes);
			res.add(maxRes);

			return res;
		} else {
			return einzelWerte;
		}
	}

	@Override
	public Benutzer validiereBenutzername(String benutzerName) {

		BenutzerDao bPersister = new BenutzerDaoImpl();

		return bPersister.findByBenutzername(benutzerName) == null ? null : bPersister.findByBenutzername(benutzerName);
	}

	@Override
	public Benutzer findeBenutzerById(int id) {

		BenutzerDao bPersister = new BenutzerDaoImpl();

		return bPersister.find(Benutzer.class, id);
	}

	@Override
	public boolean aktualisierenAPI() {
		WetterDatenDao wdPersister = new WetterDatenDaoImpl();

		ReaderService reader = new ReaderRWSProxy();

		final EntityManager em = JpaUtil.createEntityManager();

		List<WetterDaten> wdListe = new ArrayList<>();
		List<WetterDaten> wdToSafe = new ArrayList<>();
		TypedQuery<LocalDate> tQryDate = em.createNamedQuery("WetterDaten.findMaxDate", LocalDate.class);
		LocalDate maxDatum = tQryDate.getSingleResult();
		TypedQuery<LocalTime> tQryTime = em.createNamedQuery("WetterDaten.findMaxTimeWhereDatum", LocalTime.class);
		tQryTime.setParameter("datum", maxDatum);
		LocalTime maxTime = tQryTime.getSingleResult();

		if (maxDatum != null && maxTime != null) {
			wdListe = reader.holeWetterdaten(maxDatum.getYear());

			for (Iterator<WetterDaten> it = wdListe.iterator(); it.hasNext();) {
				if (maxDatum.compareTo(it.next().getDatum()) < 0 && maxTime.compareTo(it.next().getUhrZeit()) < 0) {
					wdToSafe.add(it.next());
				}
			}
			wdPersister.alleSpeichern(wdToSafe);
		}
		if (em.isOpen()) {
			em.close();
		}

		return wdPersister.alle(WetterDaten.class).size() >= wdListe.size();
	}

	@Override
	public boolean ersterBezugAPI() {

		OrtDao oPersister = new OrtDaoImpl();
		WetterDatenDao wdPersister = new WetterDatenDaoImpl();

		ReaderService reader = new ReaderRWSProxy();

		for (WetterDaten wd : wdPersister.alle(WetterDaten.class)) {
			wdPersister.loeschen(wd);
		}

		for (Ort o : oPersister.alle(Ort.class)) {
			oPersister.loeschen(o);
		}
		oPersister.alleSpeichern(reader.holeOrte());
		wdPersister.alleSpeichern(reader.holeWetterdaten(2023));
		wdPersister.alleSpeichern(reader.holeWetterdaten(2024));

		return !(oPersister.alle(Ort.class).isEmpty() && wdPersister.alle(WetterDaten.class).isEmpty());
	}

	@Override
	public void wetterDatenExportDurchschnitt(WetterDaten wetterDaten, int exportDatentyp, String zeit) {

		if (wetterDaten != null) {
			switch (exportDatentyp) {
			case 4:
				System.out.println("Zeitraum der Wetterdaten: " + zeit);
				System.out.println("Art der Daten: Durchschnittswerte");
				System.out.println("[Ort= " + wetterDaten.getOrt().getName() + ", PLZ= " + wetterDaten.getOrt().getZip()
						+ ", Luftdruck= " + wetterDaten.getLuftDruck() + ", Luftfeuchtigkeit= "
						+ wetterDaten.getLuftFeuchtigkeit() + ", Temperatur= " + wetterDaten.getTemperatur() + "]");
				break;
			case 2:
				final char delemiter = ',';

				File fileCSV = new File(System.getProperty("user.home") + File.separator + "wetterDatenAverage.csv");

				try (PrintWriter printer = new PrintWriter(fileCSV)) {

					StringBuilder sBuilder = new StringBuilder();

					sBuilder.append("ART").append(delemiter).append("Durchschnittswerte");

					printer.println(sBuilder.toString());

					sBuilder.setLength(0);

					sBuilder.append("ZEITRAUM").append(delemiter).append(zeit);

					printer.println(sBuilder.toString());

					sBuilder.setLength(0);

					sBuilder.append("ORT").append(delemiter).append("PLZ").append(delemiter).append("LUFTDRUCK")
							.append(delemiter).append("LUFTFEUCHTIGKEIT").append(delemiter).append("TEMPERATUR");

					printer.println(sBuilder.toString());

					sBuilder.setLength(0);

					sBuilder.append(wetterDaten.getOrt().getName()).append(delemiter)
							.append(wetterDaten.getOrt().getZip()).append(delemiter).append(wetterDaten.getLuftDruck())
							.append(delemiter).append(wetterDaten.getLuftFeuchtigkeit()).append(delemiter)
							.append(wetterDaten.getTemperatur());

					printer.println(sBuilder.toString());

					sBuilder.setLength(0);

					System.out.println("\nDaten exportiert: " + fileCSV.getAbsolutePath());
				} catch (FileNotFoundException e) {
					// Logging
					System.err.println("ERROR:\nDaten konnten nicht exportiert werden!");
				}

				break;
			case 1:
				// Der PDF-Export wurde mithilfe von ChatGPT programmiert. Code wurde
				// verstanden.(MT)
				try {

					// Aufsetzen des leeren PDF-Dokuments
					PDDocument document = new PDDocument();

					PDPage page = new PDPage();

					document.addPage(page);

					PDPageContentStream contentStream = new PDPageContentStream(document, page);

					// Erstmalige Formatierung des leeren Dokuments
					float yStart = page.getMediaBox().getHeight() - 50;
					float margin = 50;
					float width = page.getMediaBox().getWidth() - 2 * margin;
					float yPosition = yStart;
					float ortWidth = 100;
					float zipWidth = 40;
					float luftdruckWidth = 85;
					float luftfeuchtigkeitWidth = 130;

					contentStream.beginText();
					contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
					contentStream.newLineAtOffset(margin, yPosition);
					contentStream.showText(String.format("%-8s %-12s", "ART:", "Durchschnittswerte"));
					contentStream.endText();
					yPosition -= 20;

					contentStream.beginText();
					contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
					contentStream.newLineAtOffset(margin, yPosition);
					contentStream.showText(String.format("%-8s %-20s", "ZEITRAUM:", zeit));
					contentStream.endText();
					yPosition -= 20;

					// Tabellenkopf schreiben
					contentStream.beginText();
					contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
					contentStream.newLineAtOffset(margin, yPosition);
					contentStream.showText("ORT");
					contentStream.newLineAtOffset(ortWidth, 0);
					contentStream.showText("PLZ");
					contentStream.newLineAtOffset(zipWidth, 0);
					contentStream.showText("LUFTDRUCK");
					contentStream.newLineAtOffset(luftdruckWidth, 0);
					contentStream.showText("LUFTFEUCHTIGKEIT");
					contentStream.newLineAtOffset(luftfeuchtigkeitWidth, 0);
					contentStream.showText("TEMPERATUR");
					contentStream.endText();
					yPosition -= 20;

					// Werte einfügen
					contentStream.setFont(PDType1Font.HELVETICA, 12);
					contentStream.beginText();
					contentStream.newLineAtOffset(margin, yPosition);
					contentStream.showText(String.format("%-20s", wetterDaten.getOrt().getName()));
					contentStream.newLineAtOffset(ortWidth, 0);
					contentStream.showText(String.format("%-6d", wetterDaten.getOrt().getZip()));
					contentStream.newLineAtOffset(zipWidth, 0);
					contentStream.showText(String.format("%-11d", wetterDaten.getLuftDruck()));
					contentStream.newLineAtOffset(luftdruckWidth, 0);
					contentStream.showText(String.format("%-18d", wetterDaten.getLuftFeuchtigkeit()));
					contentStream.newLineAtOffset(luftfeuchtigkeitWidth, 0);
					contentStream.showText(String.format("%-10.2f", wetterDaten.getTemperatur()));
					contentStream.endText();
					yPosition -= 15;

					contentStream.close();

					// Dokument in File speichern und exportieren
					File filePDF = new File(
							System.getProperty("user.home") + File.separator + "wetterDatenAverage.pdf");
					document.save(filePDF);
					document.close();

					System.out.println("\nDaten exportiert: " + filePDF.getAbsolutePath());
				} catch (IOException e) {
					System.err.println("ERROR:\nDaten konnten nicht exportiert werden!");
				}
				break;
			case 3:

				String[] ort = { wetterDaten.getOrt().getName() };
				int[] zip = { wetterDaten.getOrt().getZip() };
				int[] luftDruck = { wetterDaten.getLuftDruck() };
				int[] luftFeuchtigkeit = { wetterDaten.getLuftFeuchtigkeit() };
				double[] temperatur = { wetterDaten.getTemperatur() };

				File fileJSON = new File(System.getProperty("user.home") + File.separator + "wetterDatenAverage.json");

				try (FileWriter writer = new FileWriter(fileJSON)) {

					writer.write("[{");
					for (int i = 0; i < ort.length; i++) {
						writer.write("\"art\":\"durchschnittswerte\"");
						writer.write("\"name\":\"" + ort[i] + "\",");
						writer.write("\"plz\":" + zip[i] + ",");
						writer.write("\"zeitraum\":\"" + zeit + "\",");
						writer.write("\"luftdruck\":" + luftDruck[i] + ",");
						writer.write("\"luftfeuchtigkeit\":" + luftFeuchtigkeit[i] + ",");
						writer.write("\"temperatur\":" + temperatur[i]);
						if (i < ort.length - 1) {
							writer.write("},");
						} else {
							writer.write("}");
						}
					}
					writer.write("]");

					writer.close();
					System.out.println("\nDaten exportiert: " + fileJSON.getAbsolutePath());
				} catch (IOException e) {
					System.err.println("ERROR: Daten konnten nicht exportiert werden.");
				}

			}

		}

	}

	@Override
	public void wetterDatenExportMinMaxEinzel(List<WetterDaten> wetterDatenList, int exportDatentyp, String zeit,
			String art) {

		if (!wetterDatenList.isEmpty()) {
			switch (exportDatentyp) {
			case 4:
				System.out.println("Zeitraum der Wetterdaten: " + zeit);
				if (art.equals("Einzel")) {
					System.out.println("Art: Einzelwerte");
					for (WetterDaten wetterDaten : wetterDatenList) {
						System.out.println(
								"[ID= " + wetterDaten.getId() + ", Ort= " + wetterDaten.getOrt().getName() + ", PLZ= "
										+ wetterDaten.getOrt().getZip() + ", Luftdruck= " + wetterDaten.getLuftDruck()
										+ ", Luftfeuchtigkeit= " + wetterDaten.getLuftFeuchtigkeit() + ", Temperatur= "
										+ wetterDaten.getTemperatur() + "]");
					}
				} else if (art.equals("Min/Max")) {
					System.out.println("Min-Element: Ort= " + wetterDatenList.get(0).getOrt().getName() + ", PLZ= "
							+ wetterDatenList.get(0).getOrt().getZip() + ", Luftdruck= "
							+ wetterDatenList.get(0).getLuftDruck() + ", Luftfeuchtigkeit= "
							+ wetterDatenList.get(0).getLuftFeuchtigkeit() + ", Temperatur= "
							+ wetterDatenList.get(0).getTemperatur() + "]");
					System.out.println();
					System.out.println("Max-Element: Ort= " + wetterDatenList.get(1).getOrt().getName() + ", PLZ= "
							+ wetterDatenList.get(1).getOrt().getZip() + ", Luftdruck= "
							+ wetterDatenList.get(1).getLuftDruck() + ", Luftfeuchtigkeit= "
							+ wetterDatenList.get(1).getLuftFeuchtigkeit() + ", Temperatur= "
							+ wetterDatenList.get(1).getTemperatur() + "]");
				}
				break;
			case 2:
				final char delemiter = ',';
				File fileCSV = new File(System.getProperty("user.home") + File.separator + "wetterDaten.csv");
				try (PrintWriter printer = new PrintWriter(fileCSV)) {
					StringBuilder sBuilder = new StringBuilder();

					if (art.equals("Einzel")) {
						sBuilder.append("ART").append(delemiter).append("Einzelwerte");

						printer.println(sBuilder.toString());

						sBuilder.setLength(0);

						sBuilder.append("ZEITRAUM").append(delemiter).append(zeit);

						printer.println(sBuilder.toString());

						sBuilder.setLength(0);

						sBuilder.append("ID").append(delemiter).append("ORT").append(delemiter).append("PLZ")
								.append(delemiter).append("LUFTDRUCK").append(delemiter).append("LUFTFEUCHTIGKEIT")
								.append(delemiter).append("TEMPERATUR");
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);
						for (WetterDaten wetterDaten : wetterDatenList) {
							sBuilder.append(wetterDaten.getId()).append(delemiter)
									.append(wetterDaten.getOrt().getName()).append(delemiter)
									.append(wetterDaten.getOrt().getZip()).append(delemiter)
									.append(wetterDaten.getLuftDruck()).append(delemiter)
									.append(wetterDaten.getLuftFeuchtigkeit()).append(delemiter)
									.append(wetterDaten.getTemperatur());
							printer.println(sBuilder.toString());
							sBuilder.setLength(0);

						}
					} else if (art.equals("Min/Max")) {

						sBuilder.append("ZEITRAUM").append(delemiter).append(zeit);

						printer.println(sBuilder.toString());

						sBuilder.setLength(0);

						sBuilder.append("ART").append(delemiter).append("ORT").append(delemiter).append("PLZ")
								.append(delemiter).append("LUFTDRUCK").append(delemiter).append("LUFTFEUCHTIGKEIT")
								.append(delemiter).append("TEMPERATUR");
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);

						sBuilder.append("Min-Element").append(delemiter)
								.append(wetterDatenList.get(0).getOrt().getName()).append(delemiter)
								.append(wetterDatenList.get(0).getOrt().getZip()).append(delemiter)
								.append(wetterDatenList.get(0).getLuftDruck()).append(delemiter)
								.append(wetterDatenList.get(0).getLuftFeuchtigkeit()).append(delemiter)
								.append(wetterDatenList.get(0).getTemperatur());
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);

						sBuilder.append("Max-Element").append(delemiter)
								.append(wetterDatenList.get(1).getOrt().getName()).append(delemiter)
								.append(wetterDatenList.get(1).getOrt().getZip()).append(delemiter)
								.append(wetterDatenList.get(1).getLuftDruck()).append(delemiter)
								.append(wetterDatenList.get(1).getLuftFeuchtigkeit()).append(delemiter)
								.append(wetterDatenList.get(1).getTemperatur());
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);
					}
					System.out.println("\nDaten exportiert: " + fileCSV.getAbsolutePath());
				} catch (FileNotFoundException e) {
					// Logging
					System.err.println("ERROR:\nDaten konnten nicht exportiert werden!");
				}
				break;
			case 1:
				try {
					// Aufsetzen des leeren PDF-Dokuments
					PDDocument document = new PDDocument();

					PDPage page = new PDPage();

					document.addPage(page);

					PDPageContentStream contentStream = new PDPageContentStream(document, page);

					// Erstmalige Formatierung des leeren Dokuments
					float yStart = page.getMediaBox().getHeight() - 50;
					float margin = 50;
					float width = page.getMediaBox().getWidth() - 2 * margin;
					float yPosition = yStart;
					float idWidth = 30;
					float ortWidth = 100;
					float zipWidth = 40;
					float luftdruckWidth = 85;
					float luftfeuchtigkeitWidth = 130;
					float temperaturWidth = 100;

					if (art.equals("Einzel")) {
						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText(String.format("%-8s %-12s", "ART:", "Einzelwerte"));
						contentStream.endText();
						yPosition -= 20;
					}

					contentStream.beginText();
					contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
					contentStream.newLineAtOffset(margin, yPosition);
					contentStream.showText(String.format("%-8s %-20s", "ZEITRAUM:", zeit));
					contentStream.endText();
					yPosition -= 20;

					if (art.equals("Einzel")) {
						// Tabellenkopf schreiben
						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText("ID");
						contentStream.newLineAtOffset(idWidth, 0);
						contentStream.showText("ORT");
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText("PLZ");
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText("LUFTDRUCK");
						contentStream.newLineAtOffset(luftdruckWidth, 0);
						contentStream.showText("LUFTFEUCHTIGKEIT");
						contentStream.newLineAtOffset(luftfeuchtigkeitWidth, 0);
						contentStream.showText("TEMPERATUR");
						contentStream.endText();
						yPosition -= 20;

						// Werte einfügen
						contentStream.setFont(PDType1Font.HELVETICA, 12);
						for (WetterDaten wetterDaten : wetterDatenList) {
							contentStream.beginText();
							contentStream.newLineAtOffset(margin, yPosition);
							contentStream.showText(String.format("%-8d", wetterDaten.getId()));
							contentStream.newLineAtOffset(idWidth, 0);
							contentStream.showText(String.format("%-20s", wetterDaten.getOrt().getName()));
							contentStream.newLineAtOffset(ortWidth, 0);
							contentStream.showText(String.format("%-6d", wetterDaten.getOrt().getZip()));
							contentStream.newLineAtOffset(zipWidth, 0);
							contentStream.showText(String.format("%-11d", wetterDaten.getLuftDruck()));
							contentStream.newLineAtOffset(luftdruckWidth, 0);
							contentStream.showText(String.format("%-18d", wetterDaten.getLuftFeuchtigkeit()));
							contentStream.newLineAtOffset(luftfeuchtigkeitWidth, 0);
							contentStream.showText(String.format("%-10.2f", wetterDaten.getTemperatur()));
							contentStream.endText();
							yPosition -= 15;
						}
					} else if (art.equals("Min/Max")) {

						float artWidth = 85;

						// Tabellenkopf schreiben
						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText("ART");
						contentStream.newLineAtOffset(idWidth, 0);
						contentStream.showText("ORT");
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText("PLZ");
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText("LUFTDRUCK");
						contentStream.newLineAtOffset(luftdruckWidth, 0);
						contentStream.showText("LUFTFEUCHTIGKEIT");
						contentStream.newLineAtOffset(luftfeuchtigkeitWidth, 0);
						contentStream.showText("TEMPERATUR");
						contentStream.endText();
						yPosition -= 20;

						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA, 12);
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText(String.format("%-13s", "Min-Element"));
						contentStream.newLineAtOffset(artWidth, 0);
						contentStream.showText(String.format("%-20s", wetterDatenList.get(0).getOrt().getName()));
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText(String.format("%-6d", wetterDatenList.get(0).getOrt().getZip()));
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText(String.format("%-11d", wetterDatenList.get(0).getLuftDruck()));
						contentStream.newLineAtOffset(luftdruckWidth, 0);
						contentStream.showText(String.format("%-18d", wetterDatenList.get(0).getLuftFeuchtigkeit()));
						contentStream.newLineAtOffset(luftfeuchtigkeitWidth, 0);
						contentStream.showText(String.format("%-10.2f", wetterDatenList.get(0).getTemperatur()));
						contentStream.endText();
						yPosition -= 15;

						contentStream.beginText();
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText(String.format("%-13s", "Max-Element"));
						contentStream.newLineAtOffset(artWidth, 0);
						contentStream.showText(String.format("%-20s", wetterDatenList.get(1).getOrt().getName()));
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText(String.format("%-6d", wetterDatenList.get(1).getOrt().getZip()));
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText(String.format("%-11d", wetterDatenList.get(1).getLuftDruck()));
						contentStream.newLineAtOffset(luftdruckWidth, 0);
						contentStream.showText(String.format("%-18d", wetterDatenList.get(1).getLuftFeuchtigkeit()));
						contentStream.newLineAtOffset(luftfeuchtigkeitWidth, 0);
						contentStream.showText(String.format("%-10.2f", wetterDatenList.get(1).getTemperatur()));
						contentStream.endText();
						yPosition -= 15;

					}

					contentStream.close();

					// Dokument in File speichern und exportieren
					File filePDF = new File(System.getProperty("user.home") + File.separator + "wetterDaten.pdf");
					document.save(filePDF);
					document.close();

					System.out.println("\nDaten exportiert: " + filePDF.getAbsolutePath());
				} catch (IOException e) {
					System.err.println("ERROR:\nDaten konnten nicht exportiert werden!");
				}
				break;
			case 3:

				int[] id = new int[wetterDatenList.size()];
				String[] ort = new String[wetterDatenList.size()];
				int[] zip = new int[wetterDatenList.size()];
				int[] luftDruck = new int[wetterDatenList.size()];
				int[] luftFeuchtigkeit = new int[wetterDatenList.size()];
				double[] temperatur = new double[wetterDatenList.size()];

				for (int i = 0; i < wetterDatenList.size(); i++) {
					WetterDaten wetterDaten = wetterDatenList.get(i);
					id[i] = wetterDaten.getId();
					ort[i] = wetterDaten.getOrt().getName();
					zip[i] = wetterDaten.getOrt().getZip();
					luftDruck[i] = wetterDaten.getLuftDruck();
					luftFeuchtigkeit[i] = wetterDaten.getLuftFeuchtigkeit();
					temperatur[i] = wetterDaten.getTemperatur();
				}
				File fileJSON = new File(System.getProperty("user.home") + File.separator + "wetterDaten.json");

				try (FileWriter writer = new FileWriter(fileJSON)) {

					if (art.equals("Einzel")) {
						writer.write("[{");
						for (int i = 0; i < id.length; i++) {
							writer.write("\"art\":\"einzelwerte\"");
							writer.write("\"id\":" + id[i] + ",");
							writer.write("\"name\":\"" + ort[i] + "\",");
							writer.write("\"plz\":" + zip[i] + ",");
							writer.write("\"zeitRaum\":\"" + zeit + "\",");
							writer.write("\"luftDruck\":" + luftDruck[i] + ",");
							writer.write("\"luftFeuchtigkeit\":" + luftFeuchtigkeit[i] + ",");
							writer.write("\"temperatur\":" + temperatur[i]);
							if (i < id.length - 1) {
								writer.write("},");
							} else {
								writer.write("}");
							}
						}
						writer.write("]");
					} else if (art.equals("Min/Max")) {
						writer.write("[{");
						writer.write("\"art\":\"min-element\"");
						writer.write(",\"ort\":\"" + wetterDatenList.get(0).getOrt().getName() + "\"");
						writer.write(",\"plz\":" + wetterDatenList.get(0).getOrt().getZip());
						writer.write(",\"luftdruck\":" + wetterDatenList.get(0).getLuftDruck());
						writer.write(",\"luftfeuchtigkeit\":" + wetterDatenList.get(0).getLuftFeuchtigkeit());
						writer.write(",\"temperatur\":" + wetterDatenList.get(0).getTemperatur());
						writer.write("},");
						writer.write("{\"art\":\"max-element\"");
						writer.write(",\"ort\":\"" + wetterDatenList.get(1).getOrt().getName() + "\"");
						writer.write(",\"plz\":" + wetterDatenList.get(1).getOrt().getZip());
						writer.write(",\"luftdruck\":" + wetterDatenList.get(1).getLuftDruck());
						writer.write(",\"luftfeuchtigkeit\":" + wetterDatenList.get(1).getLuftFeuchtigkeit());
						writer.write(",\"temperatur\":" + wetterDatenList.get(1).getTemperatur());
						writer.write("}]");
					}

					writer.close();

					System.out.println("\nDaten exportiert: " + fileJSON.getAbsolutePath());
				} catch (IOException e) {
					// Logging
					System.err.println("ERROR: Daten konnten nicht exportiert werden.");
				}
				break;
			}
		}
	}

	@Override
	public String passwortVorhanden(Benutzer benutzer) {

		BenutzerDao bPersister = new BenutzerDaoImpl();

		String pw = bPersister.findByBenutzername(benutzer.getBenutzerName()).getPasswort();

		return pw;
	}

	@Override
	public boolean passwortSetzen(Benutzer benutzer, String passwort) {

		BenutzerDao bPersister = new BenutzerDaoImpl();

		benutzer.setPasswort(passwort);

		bPersister.aktualisieren(benutzer);

		return bPersister.findByBenutzername(benutzer.getBenutzerName()).getPasswort() == passwort;
	}

	@Override
	public boolean validierePasswort(String passwortEingabe, String passwortDB) {

		return passwortEingabe.equals(passwortDB);
	}

	@Override
	public String validiereRolle(Benutzer benutzer) {

		BenutzerDao bPersister = new BenutzerDaoImpl();

		return bPersister.findByBenutzername(benutzer.getBenutzerName()).getRolle();
	}

	@Override
	public List<WetterDaten> minMaxOrt(LocalDate datum, LocalTime zeit, String artDerAbfrage) {

		List<WetterDaten> res = new ArrayList<>();
		List<WetterDaten> abfrage = new ArrayList<>();
		for (Ort o : this.alleOrtschaften()) {
			if (!(this.abrufenEinzelwerte(o.getName(), datum, datum, zeit, zeit.plusHours(1)).isEmpty())) {
				abfrage.add(this.abrufenEinzelwerte(o.getName(), datum, datum, zeit, zeit.plusHours(1)).get(0));
			}
		}
		if (artDerAbfrage.equals("Temperatur") && !(abfrage.isEmpty())) {
			WetterDaten maxTemp = new WetterDaten();
			WetterDaten minTemp = new WetterDaten();

			for (WetterDaten w : abfrage) {
				if (maxTemp.getTemperatur() < w.getTemperatur()) {
					maxTemp.setOrt(w.getOrt());
					maxTemp.setTemperatur(w.getTemperatur());
					maxTemp.setId(w.getId());
				}
				if (minTemp.getTemperatur() > w.getTemperatur() || minTemp.getTemperatur() == 0) {
					minTemp.setOrt(w.getOrt());
					minTemp.setTemperatur(w.getTemperatur());
					minTemp.setId(w.getId());
				}
			}
			res.add(minTemp);
			res.add(maxTemp);
			return res;
		} else if (artDerAbfrage.equals("LuftFeuchtigkeit") && !(abfrage.isEmpty())) {
			WetterDaten maxHum = new WetterDaten();
			WetterDaten minHum = new WetterDaten();

			for (WetterDaten w : abfrage) {
				if (maxHum.getLuftFeuchtigkeit() < w.getLuftFeuchtigkeit()) {
					maxHum.setOrt(w.getOrt());
					maxHum.setLuftFeuchtigkeit(w.getLuftFeuchtigkeit());
					maxHum.setId(w.getId());
				}
				if (minHum.getLuftFeuchtigkeit() > w.getLuftFeuchtigkeit() || minHum.getLuftFeuchtigkeit() == 0) {
					minHum.setOrt(w.getOrt());
					minHum.setLuftFeuchtigkeit(w.getLuftFeuchtigkeit());
					minHum.setId(w.getId());
				}
			}
			res.add(minHum);
			res.add(maxHum);
			return res;
		} else if (artDerAbfrage.equals("LuftDruck") && !(abfrage.isEmpty())) {
			WetterDaten maxPres = new WetterDaten();
			WetterDaten minPres = new WetterDaten();

			for (WetterDaten w : abfrage) {
				if (maxPres.getLuftDruck() < w.getLuftDruck()) {
					maxPres.setOrt(w.getOrt());
					maxPres.setLuftDruck(w.getLuftDruck());
					maxPres.setId(w.getId());
				}
				if (minPres.getLuftDruck() > w.getLuftDruck() || minPres.getLuftDruck() == 0) {
					minPres.setOrt(w.getOrt());
					minPres.setLuftDruck(w.getLuftDruck());
					minPres.setId(w.getId());
				}
			}
			res.add(minPres);
			res.add(maxPres);
			return res;
		} else {
			return res;
		}
	}

	@Override
	public void minMaxOrtExport(List<WetterDaten> wetterDaten, String zeitPunkt, int exportTyp, String artDerAbfrage) {

		if (!wetterDaten.isEmpty()) {
			switch (exportTyp) {
			case 4:
				System.out.println("Zeitpunkt der Wetterdaten: " + zeitPunkt);

				if (artDerAbfrage.equals("Temperatur")) {
					System.out.println("Min-Element: ID= " + wetterDaten.get(0).getId() + ", Ort= "
							+ wetterDaten.get(0).getOrt().getName() + ", PLZ= " + wetterDaten.get(0).getOrt().getZip()
							+ ", Temperatur= " + wetterDaten.get(0).getTemperatur() + "]");
					System.out.println();
					System.out.println("Max-Element: ID= " + wetterDaten.get(1).getId() + ", Ort= "
							+ wetterDaten.get(1).getOrt().getName() + ", PLZ= " + wetterDaten.get(1).getOrt().getZip()
							+ ", Temperatur= " + wetterDaten.get(1).getTemperatur() + "]");
				} else if (artDerAbfrage.equals("LuftDruck")) {
					System.out.println("Min-Element: ID= " + wetterDaten.get(0).getId() + ", Ort= "
							+ wetterDaten.get(0).getOrt().getName() + ", PLZ= " + wetterDaten.get(0).getOrt().getZip()
							+ ", LuftDruck= " + wetterDaten.get(0).getLuftDruck() + "]");
					System.out.println();
					System.out.println("Max-Element: ID= " + wetterDaten.get(1).getId() + ", Ort= "
							+ wetterDaten.get(1).getOrt().getName() + ", PLZ= " + wetterDaten.get(1).getOrt().getZip()
							+ ", LuftDruck= " + wetterDaten.get(1).getLuftDruck() + "]");
				} else if (artDerAbfrage.equals("LuftFeuchtigkeit")) {
					System.out.println("Min-Element: ID= " + wetterDaten.get(0).getId() + ", Ort= "
							+ wetterDaten.get(0).getOrt().getName() + ", PLZ= " + wetterDaten.get(0).getOrt().getZip()
							+ ", LuftFeuchtigkeit= " + wetterDaten.get(0).getLuftFeuchtigkeit() + "]");
					System.out.println();
					System.out.println("Max-Element: ID= " + wetterDaten.get(1).getId() + ", Ort= "
							+ wetterDaten.get(1).getOrt().getName() + ", PLZ= " + wetterDaten.get(1).getOrt().getZip()
							+ ", LuftFeuchtigkeit= " + wetterDaten.get(1).getLuftFeuchtigkeit() + "]");
				}
				break;
			case 2:
				final char delemiter = ',';
				File fileCSV = new File(
						System.getProperty("user.home") + File.separator + "wetterDaten" + artDerAbfrage + ".csv");
				try (PrintWriter printer = new PrintWriter(fileCSV)) {
					StringBuilder sBuilder = new StringBuilder();

					if (artDerAbfrage.equals("Temperatur")) {
						sBuilder.append("ZEITPUNKT").append(delemiter).append(zeitPunkt);
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);

						sBuilder.append("ART").append(delemiter).append("ID").append(delemiter).append("ORT")
								.append(delemiter).append("PLZ").append(delemiter).append("TEMPERATUR");
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);

						sBuilder.append("Min-Element").append(delemiter).append(wetterDaten.get(0).getId())
								.append(delemiter).append(wetterDaten.get(0).getOrt().getName()).append(delemiter)
								.append(wetterDaten.get(0).getOrt().getZip()).append(delemiter)
								.append(wetterDaten.get(0).getTemperatur());
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);

						sBuilder.append("Max-Element").append(delemiter).append(wetterDaten.get(1).getId())
								.append(delemiter).append(wetterDaten.get(1).getOrt().getName()).append(delemiter)
								.append(wetterDaten.get(1).getOrt().getZip()).append(delemiter)
								.append(wetterDaten.get(1).getTemperatur());
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);
					} else if (artDerAbfrage.equals("LuftDruck")) {
						sBuilder.append("ZEITRAUM").append(delemiter).append(zeitPunkt);
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);

						sBuilder.append("ART").append(delemiter).append("ID").append(delemiter).append("ORT")
								.append(delemiter).append("PLZ").append(delemiter).append("LUFTDRUCK");
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);

						sBuilder.append("Min-Element").append(delemiter).append(wetterDaten.get(0).getId())
								.append(delemiter).append(wetterDaten.get(0).getOrt().getName()).append(delemiter)
								.append(wetterDaten.get(0).getOrt().getZip()).append(delemiter)
								.append(wetterDaten.get(0).getLuftDruck());
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);

						sBuilder.append("Max-Element").append(delemiter).append(wetterDaten.get(1).getId())
								.append(delemiter).append(wetterDaten.get(1).getOrt().getName()).append(delemiter)
								.append(wetterDaten.get(1).getOrt().getZip()).append(delemiter)
								.append(wetterDaten.get(1).getLuftDruck());
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);
					} else if (artDerAbfrage.equals("LuftFeuchtigkeit")) {
						sBuilder.append("ZEITRAUM").append(delemiter).append(zeitPunkt);
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);

						sBuilder.append("ART").append(delemiter).append("ID").append(delemiter).append("ORT")
								.append(delemiter).append("PLZ").append(delemiter).append("LUFTFEUCHTIGKEIT");
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);

						sBuilder.append("Min-Element").append(delemiter).append(wetterDaten.get(0).getId())
								.append(delemiter).append(wetterDaten.get(0).getOrt().getName()).append(delemiter)
								.append(wetterDaten.get(0).getOrt().getZip()).append(delemiter)
								.append(wetterDaten.get(0).getLuftFeuchtigkeit());
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);

						sBuilder.append("Max-Element").append(delemiter).append(wetterDaten.get(1).getId())
								.append(delemiter).append(wetterDaten.get(1).getOrt().getName()).append(delemiter)
								.append(wetterDaten.get(1).getOrt().getZip()).append(delemiter)
								.append(wetterDaten.get(1).getLuftFeuchtigkeit());
						printer.println(sBuilder.toString());
						sBuilder.setLength(0);
					}
					System.out.println("\nDaten exportiert: " + fileCSV.getAbsolutePath());
				} catch (FileNotFoundException e) {
					// Logging
					System.err.println("ERROR:\nDaten konnten nicht exportiert werden!");
				}
				break;
			case 1:
				try {
					// Aufsetzen des leeren PDF-Dokuments
					PDDocument document = new PDDocument();

					PDPage page = new PDPage();

					document.addPage(page);

					PDPageContentStream contentStream = new PDPageContentStream(document, page);

					// Erstmalige Formatierung des leeren Dokuments
					float yStart = page.getMediaBox().getHeight() - 50;
					float margin = 50;
					float width = page.getMediaBox().getWidth() - 2 * margin;
					float yPosition = yStart;
					float idWidth = 30;
					float ortWidth = 100;
					float zipWidth = 40;
					float artWidth = 85;

					contentStream.beginText();
					contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
					contentStream.newLineAtOffset(margin, yPosition);
					contentStream.showText(String.format("%-8s %-20s", "ZEITPUNKT:", zeitPunkt));
					contentStream.endText();
					yPosition -= 20;

					if (artDerAbfrage.equals("Temperatur")) {

						// Tabellenkopf schreiben
						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText("ART");
						contentStream.newLineAtOffset(artWidth, 0);
						contentStream.showText("ID");
						contentStream.newLineAtOffset(idWidth, 0);
						contentStream.showText("ORT");
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText("PLZ");
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText("TEMPERATUR");
						contentStream.endText();
						yPosition -= 20;

						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA, 12);
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText(String.format("%-13s", "Min-Element"));
						contentStream.newLineAtOffset(artWidth, 0);
						contentStream.showText(String.format("%-8d", wetterDaten.get(0).getId()));
						contentStream.newLineAtOffset(idWidth, 0);
						contentStream.showText(String.format("%-20s", wetterDaten.get(0).getOrt().getName()));
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText(String.format("%-6d", wetterDaten.get(0).getOrt().getZip()));
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText(String.format("%-10.2f", wetterDaten.get(0).getTemperatur()));
						contentStream.endText();
						yPosition -= 15;

						contentStream.beginText();
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText(String.format("%-13s", "Max-Element"));
						contentStream.newLineAtOffset(artWidth, 0);
						contentStream.showText(String.format("%-8d", wetterDaten.get(1).getId()));
						contentStream.newLineAtOffset(idWidth, 0);
						contentStream.showText(String.format("%-20s", wetterDaten.get(1).getOrt().getName()));
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText(String.format("%-6d", wetterDaten.get(1).getOrt().getZip()));
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText(String.format("%-10.2f", wetterDaten.get(1).getTemperatur()));
						contentStream.endText();
						yPosition -= 15;

					} else if (artDerAbfrage.equals("LuftDruck")) {

						// Tabellenkopf schreiben
						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText("ART");
						contentStream.newLineAtOffset(artWidth, 0);
						contentStream.showText("ID");
						contentStream.newLineAtOffset(idWidth, 0);
						contentStream.showText("ORT");
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText("PLZ");
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText("LUFTDRUCK");
						contentStream.endText();
						yPosition -= 20;

						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA, 12);
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText(String.format("%-13s", "Min-Element"));
						contentStream.newLineAtOffset(artWidth, 0);
						contentStream.showText(String.format("%-8d", wetterDaten.get(0).getId()));
						contentStream.newLineAtOffset(idWidth, 0);
						contentStream.showText(String.format("%-20s", wetterDaten.get(0).getOrt().getName()));
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText(String.format("%-6d", wetterDaten.get(0).getOrt().getZip()));
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText(String.format("%-11d", wetterDaten.get(0).getLuftDruck()));
						contentStream.endText();
						yPosition -= 15;

						contentStream.beginText();
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText(String.format("%-13s", "Max-Element"));
						contentStream.newLineAtOffset(artWidth, 0);
						contentStream.showText(String.format("%-8d", wetterDaten.get(1).getId()));
						contentStream.newLineAtOffset(idWidth, 0);
						contentStream.showText(String.format("%-20s", wetterDaten.get(1).getOrt().getName()));
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText(String.format("%-6d", wetterDaten.get(1).getOrt().getZip()));
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText(String.format("%-11d", wetterDaten.get(1).getLuftDruck()));
						contentStream.endText();
						yPosition -= 15;

					} else if (artDerAbfrage.equals("LuftFeuchtigkeit")) {

						// Tabellenkopf schreiben
						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText("ART");
						contentStream.newLineAtOffset(artWidth, 0);
						contentStream.showText("ID");
						contentStream.newLineAtOffset(idWidth, 0);
						contentStream.showText("ORT");
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText("PLZ");
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText("LUFTFEUCHTIGKEIT");
						contentStream.endText();
						yPosition -= 20;

						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA, 12);
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText(String.format("%-13s", "Min-Element"));
						contentStream.newLineAtOffset(artWidth, 0);
						contentStream.showText(String.format("%-8d", wetterDaten.get(0).getId()));
						contentStream.newLineAtOffset(idWidth, 0);
						contentStream.showText(String.format("%-20s", wetterDaten.get(0).getOrt().getName()));
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText(String.format("%-6d", wetterDaten.get(0).getOrt().getZip()));
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText(String.format("%-18d", wetterDaten.get(0).getLuftFeuchtigkeit()));
						contentStream.endText();
						yPosition -= 15;

						contentStream.beginText();
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText(String.format("%-13s", "Max-Element"));
						contentStream.newLineAtOffset(artWidth, 0);
						contentStream.showText(String.format("%-8d", wetterDaten.get(1).getId()));
						contentStream.newLineAtOffset(idWidth, 0);
						contentStream.showText(String.format("%-20s", wetterDaten.get(1).getOrt().getName()));
						contentStream.newLineAtOffset(ortWidth, 0);
						contentStream.showText(String.format("%-6d", wetterDaten.get(1).getOrt().getZip()));
						contentStream.newLineAtOffset(zipWidth, 0);
						contentStream.showText(String.format("%-18d", wetterDaten.get(1).getLuftFeuchtigkeit()));
						contentStream.endText();
						yPosition -= 15;
					}

					contentStream.close();

					// Dokument in File speichern und exportieren
					File filePDF = new File(
							System.getProperty("user.home") + File.separator + "wetterDaten" + artDerAbfrage + ".pdf");
					document.save(filePDF);
					document.close();

					System.out.println("\nDaten exportiert: " + filePDF.getAbsolutePath());
				} catch (IOException e) {
					System.err.println("ERROR:\nDaten konnten nicht exportiert werden!");
				}
				break;
			case 3:

				File fileJSON = new File(
						System.getProperty("user.home") + File.separator + "wetterDaten" + artDerAbfrage + ".json");

				try (FileWriter writer = new FileWriter(fileJSON)) {

					if (artDerAbfrage.equals("Temperatur")) {
						writer.write("[{");
						writer.write("\"art\":\"min-element\"");
						writer.write(",\"id\":" + wetterDaten.get(0).getId());
						writer.write(",\"ort\":\"" + wetterDaten.get(0).getOrt().getName() + "\"");
						writer.write(",\"plz\":" + wetterDaten.get(0).getOrt().getZip());
						writer.write(",\"temperatur\":" + wetterDaten.get(0).getTemperatur());
						writer.write("},");
						writer.write("{\"art\":\"max-element\"");
						writer.write(",\"id\":" + wetterDaten.get(1).getId());
						writer.write(",\"ort\":\"" + wetterDaten.get(1).getOrt().getName() + "\"");
						writer.write(",\"plz\":" + wetterDaten.get(1).getOrt().getZip());
						writer.write(",\"temperatur\":" + wetterDaten.get(1).getTemperatur());
						writer.write("}]");
					} else if (artDerAbfrage.equals("LuftDruck")) {
						writer.write("[{");
						writer.write("\"art\":\"min-element\"");
						writer.write(",\"id\":" + wetterDaten.get(0).getId());
						writer.write(",\"ort\":\"" + wetterDaten.get(0).getOrt().getName() + "\"");
						writer.write(",\"plz\":" + wetterDaten.get(0).getOrt().getZip());
						writer.write(",\"luftdruck\":" + wetterDaten.get(0).getLuftDruck());
						writer.write("},");
						writer.write("{\"art\":\"max-element\"");
						writer.write(",\"id\":" + wetterDaten.get(1).getId());
						writer.write(",\"ort\":\"" + wetterDaten.get(1).getOrt().getName() + "\"");
						writer.write(",\"plz\":" + wetterDaten.get(1).getOrt().getZip());
						writer.write(",\"luftdruck\":" + wetterDaten.get(1).getLuftDruck());
						writer.write("}]");
					} else if (artDerAbfrage.equals("LuftFeuchtigkeit")) {
						writer.write("[{");
						writer.write("\"art\":\"min-element\"");
						writer.write(",\"id\":" + wetterDaten.get(0).getId());
						writer.write(",\"ort\":\"" + wetterDaten.get(0).getOrt().getName() + "\"");
						writer.write(",\"plz\":" + wetterDaten.get(0).getOrt().getZip());
						writer.write(",\"luftfeuchtigkeit\":" + wetterDaten.get(0).getLuftFeuchtigkeit());
						writer.write("},");
						writer.write("{\"art\":\"max-element\"");
						writer.write(",\"id\":" + wetterDaten.get(1).getId());
						writer.write(",\"ort\":\"" + wetterDaten.get(1).getOrt().getName() + "\"");
						writer.write(",\"plz\":" + wetterDaten.get(1).getOrt().getZip());
						writer.write(",\"luftfeuchtigkeit\":" + wetterDaten.get(1).getLuftFeuchtigkeit());
						writer.write("}]");
					}

					writer.close();

					System.out.println("\nDaten exportiert: " + fileJSON.getAbsolutePath());
				} catch (IOException e) {
					// Logging
					System.err.println("ERROR: Daten konnten nicht exportiert werden.");
				}
				break;
			}
		}
	}

	@Override
	public boolean ortVorhanden(String ort) {
		OrtDao oPersister = new OrtDaoImpl();
		return (!(oPersister.findByOrt(ort).isEmpty()));
	}
}
