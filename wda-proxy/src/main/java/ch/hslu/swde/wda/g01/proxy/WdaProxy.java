package ch.hslu.swde.wda.g01.proxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ch.hslu.swde.wda.g01.domain.Benutzer;
import ch.hslu.swde.wda.g01.domain.Ort;
import ch.hslu.swde.wda.g01.domain.WetterDaten;
import ch.hslu.wda.g01.business.api.Wda;
import jakarta.ws.rs.core.UriBuilder;
/**
 * Implements a proxy class that sends http-requests, gets the response and returns it accordingly.
 * 
 * @author Dario Meyer, Mathis Trautmann, Agata Ciesielska, Nico Graf
 * @version 1.0.0
 */
public class WdaProxy implements Wda {

	private Logger myLogger = LogManager.getLogger(WdaProxy.class);

	private static final String BASE_URI = "http://localhost:9090/";
	private static final String MEDIA_TYPE = "application/json";
	private static ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()); // Unterstuetzung fuer
																									// die neuen Java
																									// 8-Datums- und
																									// Zeittypen welche
																									// Jackson nicht
																									// unterstuetzt
	/**
	 * Constructor that creats an administrator. 
	 */
	public WdaProxy(){
		Benutzer admin = new Benutzer();
		admin.setBenutzerName("admin");
		admin.setPasswort("admin");
		admin.setRolle("admin");
		hinzufuegenBenutzer(admin);
	}

	/**
	 * Formats a HTTP-request, gets the response and returns all the users from the DB
	 * @return A List of type 'Benutzer' of all the users
	 * 
	 */
	@Override
	public List<Benutzer> alleBenutzer() {
		try {

			HttpClient client = HttpClient.newHttpClient();
			URI uri = URI.create(BASE_URI + "benutzer/" + "alleBenutzer");

			HttpRequest req = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				List<Benutzer> benutzer = mapper.readValue(json, new TypeReference<List<Benutzer>>() {
				});
				return benutzer;

			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Formats a HTTP-request to add a user to the db.
	 * @param Benutzer Object
	 * @return The user if successfully added or null if not.
	 * 
	 */
	@Override
	public Benutzer hinzufuegenBenutzer(Benutzer benutzer) {
		try {
			String benutzerJson = mapper.writeValueAsString(benutzer);
			HttpClient client = HttpClient.newHttpClient();
			URI uri = URI.create(BASE_URI + "benutzer/" + "hinzufuegenBenutzer");

			HttpRequest req = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(benutzerJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				Benutzer b = mapper.readValue(json, Benutzer.class);
				return b;

			} else if(res.statusCode() == 204){	
				return null;
			}	
				else {
			
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formats a HTTP-request to delete a user from the db.
	 * @param Benutzer Object
	 * @return The user if successfully deleted or null if not.
	 * 
	 */
	@Override
	public Benutzer loeschenBenutzer(Benutzer benutzer) {
		try {

			HttpClient client = HttpClient.newHttpClient();

			// Leider muss von den RESTful Prinzipien abgewichen werden, und POST anstatt
			// DELETE verwendet wurden, da die Methode loeschenBenutzer nicht mit der ID als
			// Parameter implementiert wurde
			URI uri = URI.create(BASE_URI + "benutzer/" + "loeschenBenutzer");

			String benutzerJson = mapper.writeValueAsString(benutzer);

			HttpRequest req = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(benutzerJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				Benutzer b = mapper.readValue(json, Benutzer.class);
				return b;

			} else if(res.statusCode() == 404){
				return null;
			}else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formats a HTTP-request to adjust data of a user from the db.
	 * @param Benutzer Object
	 * @return The user if successfully adjusted or null if not.
	 * 
	 */
	@Override
	public Benutzer anpassenBenutzer(Benutzer benutzer) {
		try {

			HttpClient client = HttpClient.newHttpClient();
			URI uri = URI.create(BASE_URI + "benutzer/" + "anpassen/" + "benutzer");

			String benutzerJson = mapper.writeValueAsString(benutzer);

			HttpRequest req = HttpRequest.newBuilder(uri).PUT(BodyPublishers.ofString(benutzerJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				Benutzer b = mapper.readValue(json, Benutzer.class);
				return b;

			} else if(res.statusCode() == 404){
				return null;
				
			}else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formats a HTTP-request to adjust the userName of a user from the db.
	 * @param Benutzer Object, benutzername as String
	 * @return The user if successfully adjusted or null if not.
	 * 
	 */
	@Override
	public Benutzer anpassenBenutzername(Benutzer benutzer, String benutzername) {
		try {

			HttpClient client = HttpClient.newHttpClient();
			URI uri = URI.create(BASE_URI + "benutzer/" + "anpassenBenutzername/" + benutzername);

			String benutzerJson = mapper.writeValueAsString(benutzer);

			HttpRequest req = HttpRequest.newBuilder(uri).PUT(BodyPublishers.ofString(benutzerJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				Benutzer b = mapper.readValue(json, Benutzer.class);
				return b;

			} else if(res.statusCode() == 404){
				return null;
			}else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formats a HTTP-request to adjust the lastName of a user from the db.
	 * @param Benutzer Object, nachname as String
	 * @return The user if successfully adjusted or null if not.
	 * 
	 */
	@Override
	public Benutzer anpassenNachname(Benutzer benutzer, String nachname) {
		try {

			HttpClient client = HttpClient.newHttpClient();
			URI uri = URI.create(BASE_URI + "benutzer/" + "anpassenNachname/" + nachname);

			String benutzerJson = mapper.writeValueAsString(benutzer);

			HttpRequest req = HttpRequest.newBuilder(uri).PUT(BodyPublishers.ofString(benutzerJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				Benutzer b = mapper.readValue(json, Benutzer.class);
				return b;

			} else if(res.statusCode() == 404){
				return null;
			}else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formats a HTTP-request to adjust the firstName of a user from the db.
	 * @param Benutzer Object, vorname as String
	 * @return The user if successfully adjusted or null if not.
	 * 
	 */
	@Override
	public Benutzer anpassenVorname(Benutzer benutzer, String vorname) {
		try {

			HttpClient client = HttpClient.newHttpClient();
			URI uri = URI.create(BASE_URI + "benutzer/" + "anpassenVorname/" + vorname);

			String benutzerJson = mapper.writeValueAsString(benutzer);

			HttpRequest req = HttpRequest.newBuilder(uri).PUT(BodyPublishers.ofString(benutzerJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				Benutzer b = mapper.readValue(json, Benutzer.class);
				return b;

			} else if(res.statusCode() == 404){
				return null;
			}else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formats a HTTP-request to adjust the password of a user from the db.
	 * @param Benutzer Object, password as String
	 * @return The user if successfully adjusted or null if not.
	 * 
	 */
	@Override
	public Benutzer anpassenPasswort(Benutzer benutzer, String passwort) {
		try {

			HttpClient client = HttpClient.newHttpClient();
			URI uri = URI.create(BASE_URI + "benutzer/" + "anpassenPasswort");

			String benutzerJson = mapper.writeValueAsString(benutzer);

			String[] userPw = new String[2];

			userPw[0] = benutzerJson;
			userPw[1] = passwort;

			String userPwJson = mapper.writeValueAsString(userPw);

			HttpRequest req = HttpRequest.newBuilder(uri).PUT(BodyPublishers.ofString(userPwJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				Benutzer b = mapper.readValue(json, Benutzer.class);
				return b;

			} else if(res.statusCode() == 404){
				return null;
			}else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formats a HTTP-request to adjust the rule of a user from the db.
	 * @param Benutzer Object, role as String
	 * @return The user if successfully adjusted or null if not.
	 * 
	 */
	@Override
	public Benutzer anpassenRolle(Benutzer benutzer, String rolle) {
		try {

			HttpClient client = HttpClient.newHttpClient();
			URI uri = URI.create(BASE_URI + "benutzer/" + "anpassenRolle/" + rolle);

			String benutzerJson = mapper.writeValueAsString(benutzer);

			HttpRequest req = HttpRequest.newBuilder(uri).PUT(BodyPublishers.ofString(benutzerJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				Benutzer b = mapper.readValue(json, Benutzer.class);
				return b;

			} else if(res.statusCode() == 404){
				return null;
			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formats a HTTP-request to get a list of all cities in the db.
	 * @return List of all cities.
	 * 
	 */
	@Override
	public List<Ort> alleOrtschaften() {
		try {
			HttpClient client = HttpClient.newHttpClient();

			// URIBuilder created by ChatGPT
			URI uri = URI.create(BASE_URI + "ort" + "/alleOrtschaften");

			// String uriStr = uri.toString().replace("%3A", ":");

			// URI uriKorr = URI.create(uriStr);

			HttpRequest req = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				List<Ort> orte = mapper.readValue(json, new TypeReference<List<Ort>>() {
				});
				return orte;

			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Method to export all cities dependend on the parameters( print, csv, pdf)
	 * @param orte (list of cities), exportDatentyp (the export the user wishes - print, csv, pdf)
	 * 
	 */
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
					myLogger.error(e.getMessage(), e);
					System.err.println("ERROR:\nDaten konnten nicht exportiert werden!");
				}

				break;
			case 1: //PDF
				try { //PDF Export wurde mithilfe von ChatGPT aufgesetzt. Code wurde verstanden.

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

					// Werte einfuegen
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
					myLogger.error(e.getMessage(), e);
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
					myLogger.error(e.getMessage(), e);
					System.err.println("ERROR: Daten konnten nicht exportiert werden.");
				}

			}

		} else {
			System.out.println("Zur Anfrage stehen keine Daten zur Verfuegung.");
		}

	}

	/**
	 * Formulates a http request to get the average values out of the given list. 
	 * @param einzelWerte (list of all the Data that should be calculated)
	 * @return returns a WetterData-Object which represents the average of all the data given in the parameter. 
	 * 
	 */
	@Override
	public WetterDaten abrufenDurchschnittswerte(List<WetterDaten> einzelWerte) {
		try {
			HttpClient client = HttpClient.newHttpClient();

			// URIBuilder created by ChatGPT
			URI uri = URI.create(BASE_URI + "wetterdaten" + "/abrufenDurchschnittswerte");

			String einzelWerteJson = mapper.writeValueAsString(einzelWerte);

			// String uriStr = uri.toString().replace("%3A", ":");

			// URI uriKorr = URI.create(uriStr);

			HttpRequest req = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(einzelWerteJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				WetterDaten einzelwerte = mapper.readValue(json, WetterDaten.class);
				return einzelwerte;

			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formulates a http request to get  the highest and lowest values out of the given list. 
	 * @param einzelWerte (list of all the Data that should be calculated)
	 * @return returns a List of WetterData-Objects which represents highest and lowest data. 
	 * 
	 */
	@Override
	public List<WetterDaten> abrufenMinMax(List<WetterDaten> einzelWerte) {
		try {
			HttpClient client = HttpClient.newHttpClient();

			// URIBuilder created by ChatGPT
			URI uri = URI.create(BASE_URI + "wetterdaten" + "/abrufenMinMax");

			String einzelWerteJson = mapper.writeValueAsString(einzelWerte);

			// String uriStr = uri.toString().replace("%3A", ":");

			// URI uriKorr = URI.create(uriStr);

			HttpRequest req = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(einzelWerteJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				List<WetterDaten> einzelwerte = mapper.readValue(json, new TypeReference<List<WetterDaten>>() {
				});
				return einzelwerte;

			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formulates a http request to validate if the userName is in the db. 
	 * @param benutzerName as a String
	 * @return returns the user if the userName is valid. Else returns null. 
	 * 
	 */
	@Override
	public Benutzer validiereBenutzername(String benutzerName) {
		try {

			HttpClient client = HttpClient.newHttpClient();
			URI uri = URI.create(BASE_URI + "benutzer/" + "validiereBenutzername/" + benutzerName);

			HttpRequest req = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				Benutzer b = mapper.readValue(json, Benutzer.class);
				return b;

			}else if(res.statusCode() == 404){
				return null;
			}else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formulates a http request to validate if the user has a passwort set in the db. 
	 * @param benutzer as Benutzer-Object
	 * @return returns the password as a String if the password is in the db. Else returns null. 
	 * 
	 */
	@Override
	public String passwortVorhanden(Benutzer benutzer) {
		try {
			HttpClient client = HttpClient.newHttpClient();

			// INFO FÜR RESSOURCE!! Benutzer-Objekt ist im JSON-Format
			String benutzerJson = mapper.writeValueAsString(benutzer);

			// URIBuilder created by ChatGPT
			URI uri = UriBuilder.fromUri(BASE_URI + "benutzer" + "/passwortVorhanden").build();

			// String uriStr = uri.toString().replace("%3A", ":");

			// URI uriKorr = URI.create(uriStr);

			HttpRequest req = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(benutzerJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				return res.body();

			} else if(res.statusCode() == 404){
				return null;
			}else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formulates a http request to set the password of a user in the db.
	 * @param benutzer as Benutzer-Object, password as String
	 * @return returns true if set successfully else returns false. 
	 * 
	 */
	@Override
	public boolean passwortSetzen(Benutzer benutzer, String passwort) {

		try {
			HttpClient client = HttpClient.newHttpClient();

			String benutzerJson = mapper.writeValueAsString(benutzer);

			String[] userXPassword = new String[2];
			userXPassword[0] = benutzerJson;
			userXPassword[1] = passwort;

			// URIBuilder created by ChatGPT
			URI uri = UriBuilder.fromUri(BASE_URI).path("benutzer" + "/passwortSetzen").build();

			String UserXPasswordJson = mapper.writeValueAsString(userXPassword);

			HttpRequest req = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(UserXPasswordJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				return Boolean.parseBoolean(res.body());

			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Validates if the given password is the same as in the db via a http request.
	 * @param passwortEingeben (given password) passwortDB (password in db)
	 * @return returns true if Password is valid else returns false. 
	 * 
	 */
	@Override
	public boolean validierePasswort(String passwortEingabe, String passwortDB) {
		try {
			HttpClient client = HttpClient.newHttpClient();

			String[] passwords = new String[2];
			passwords[0] = passwortEingabe;
			passwords[1] = passwortDB;

			// URIBuilder created by ChatGPT
			URI uri = UriBuilder.fromUri(BASE_URI).path("benutzer" + "/validierePasswort").build();

			String passwordsJson = mapper.writeValueAsString(passwords);

			HttpRequest req = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(passwordsJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				return Boolean.parseBoolean(res.body());

			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * makes a http request to validate which roll the user has.
	 * @param benutzer as Benutzer-Object
	 * @return the role that the user has "user" or "admin"
	 * 
	 */
	@Override
	public String validiereRolle(Benutzer benutzer) {
		try {
			HttpClient client = HttpClient.newHttpClient();

			// INFO FÜR RESSOURCE!! Benutzer-Objekt ist im JSON-Format
			String benutzerJson = mapper.writeValueAsString(benutzer);

			// URIBuilder created by ChatGPT
			URI uri = UriBuilder.fromUri(BASE_URI + "benutzer" + "/validiereRolle").build();

			// String uriStr = uri.toString().replace("%3A", ":");

			// URI uriKorr = URI.create(uriStr);

			HttpRequest req = HttpRequest.newBuilder(uri).POST(BodyPublishers.ofString(benutzerJson))
					.header("Content-Type", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				return res.body();

			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formulates a HTTP-Request to find a user by its id in the db.
	 * @param id (id of the user)
	 * @return returns the found user as a Benutzer-Object if found. Else returns null.
	 * 
	 */
	@Override
	public Benutzer findeBenutzerById(int id) {
		try {

			HttpClient client = HttpClient.newHttpClient();
			URI uri = URI.create(BASE_URI + "benutzer/findeBenutzerById/" + id);

			HttpRequest req = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				Benutzer b = mapper.readValue(json, Benutzer.class);
				return b;

			} else if(res.statusCode() == 404) {
				return null;
				
			}else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}


	/**
	 * Formulates a HTTP-Request to update the db with all the newest data from the API.
	 * @return rturns true if the update was a success else returns false. 
	 * 
	 */
	@Override
	public boolean aktualisierenAPI() {
		try {
			HttpClient client = HttpClient.newHttpClient();
			URI uri = URI.create(BASE_URI + "wetterdaten" + "/aktualisierenAPI");
			// POST-Body generated with Chat-GPT
			HttpRequest req = HttpRequest.newBuilder(uri).POST(HttpRequest.BodyPublishers.noBody())
					.header("Accept", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			// If/else von Chat GPT vorgeschlagen
			if (res.statusCode() == 200) {

				return true;
			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				return false;
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Formulates a HTTP-Request to call the method in the ressource class which get all the data from the first time of the api.
	 * @return true if the method executed successfully in the backend else returns false.  
	 * 
	 */
	@Override
	public boolean ersterBezugAPI() {

		try {
			HttpClient client = HttpClient.newHttpClient();
			URI uri = URI.create(BASE_URI + "wetterdaten" + "/ersterBezugAPI");

			// POST-Body generated with Chat-GPT
			HttpRequest req = HttpRequest.newBuilder(uri).POST(HttpRequest.BodyPublishers.noBody())
					.header("Accept", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			// If/else von Chat GPT vorgeschlagen
			if (res.statusCode() == 200) {

				return true;
			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				return false;
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Exports one weather data to pdf, xml or printing it into the console. (used for exporting average values)
	 * @param wetterDaten-Object, exportDatentyp (integer to choose which export type should be chosen, zeit (String to show 
	 * which timespan the method is called upon)
	 * 
	 */
	@Override
	public void wetterDatenExportDurchschnitt(WetterDaten wetterDaten, int exportDatentyp, String zeit) {

		if (wetterDaten != null) {
			switch (exportDatentyp) {
			case 4:
				System.out.println("Zeitraum der Wetterdaten: " + zeit);
				System.out.println("Art der Daten: Durchschnittswerte");
				System.out.println("[Ort= " + wetterDaten.getOrt().getName() + ", PLZ= " + wetterDaten.getOrt().getZip()
						+ ", Luftdruck= " + wetterDaten.getLuftDruck() + ", Luftfeuchtigkeit= "
						+ wetterDaten.getLuftFeuchtigkeit() + ", Temperatur= "
						+ String.format("%.2f", wetterDaten.getTemperatur()) + "]");
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
							.append(String.format("%.2f", wetterDaten.getTemperatur()));

					printer.println(sBuilder.toString());

					sBuilder.setLength(0);

					System.out.println("\nDaten exportiert: " + fileCSV.getAbsolutePath());
				} catch (FileNotFoundException e) {
					// Logging
					System.err.println("ERROR:\nDaten konnten nicht exportiert werden!");
				}

				break;
			case 1:
				try {//PDF Export wurde mithilfe von ChatGPT aufgesetzt. Code wurde verstanden.
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

					// Werte einfuegen
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
						writer.write("\"art\":\"durchschnittswerte\",");
						writer.write("\"name\":\"" + ort[i] + "\",");
						writer.write("\"plz\":" + zip[i] + ",");
						writer.write("\"zeitraum\":\"" + zeit + "\",");
						writer.write("\"luftdruck\":" + luftDruck[i] + ",");
						writer.write("\"luftfeuchtigkeit\":" + luftFeuchtigkeit[i] + ",");
						writer.write("\"temperatur\":" + String.format("%.2f", temperatur[i]));
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

		} else {
			System.out.println("Zum eingegebenen Zeitpunkt stehen keine Daten zur Verfuegung.");
		}
	}

	/**
	 * Exports one weather data to pdf, xml or printing it into the console. (used for exporting MinMax and single data given as lists)
	 * @param wetterDatenList List of wetterDaten-Object, exportDatentyp (integer to choose which export type should be chosen, zeit (String to show 
	 * which timespan the method is called upon)
	 * 
	 */
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
				try { //PDF Export wurde mithilfe von ChatGPT aufgesetzt. Code wurde verstanden.
					// Aufsetzen des leeren PDF-Dokuments
					PDDocument document = new PDDocument();

					int maxEntriesPerPage = 45;
					int entriesOnPage = 0;

					PDPage page = new PDPage();

					document.addPage(page);

					PDPageContentStream contentStream = new PDPageContentStream(document, page);

					// Erstmalige Formatierung des leeren Dokuments
					float yStart = page.getMediaBox().getHeight() - 50;
					float margin = 50;
					float width = page.getMediaBox().getWidth() - 2 * margin;
					float yPosition = yStart;
					float idWidth = 50;
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

						// Werte einfuegen
						for (WetterDaten wetterDaten : wetterDatenList) {
							if (entriesOnPage >= maxEntriesPerPage) {

								contentStream.close();

								page = new PDPage();
								document.addPage(page);
								contentStream = new PDPageContentStream(document, page);
								yPosition = yStart;

								// TabellenKopf fuer neue Seiten schreiben
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

								// Zuruecksetzen des Counters
								entriesOnPage = 0;

							}
							contentStream.setFont(PDType1Font.HELVETICA, 12);
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

							entriesOnPage++;
						}
					} else if (art.equals("Min/Max")) {

						float artWidth = 85;

						// Tabellenkopf schreiben
						contentStream.beginText();
						contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
						contentStream.newLineAtOffset(margin, yPosition);
						contentStream.showText("ART");
						contentStream.newLineAtOffset(artWidth, 0);
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
							writer.write("\"art\":\"einzelwerte\",");
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
		} else {
			System.out.println("Zum eingegebenen Zeitpunkt stehen keine Daten zur Verfuegung.");
		}
	}

	/**
	 * Formulates an HTTP Request to call method abrufenEinzelwerte() in ressource class.
	 * @param ort as String, datumVon as LocalDate, datumBis as LocalDate, zeitVon as LocalTime. 
	 * @return returns a list of all the Values as WetterDaten-Object that fit the values given in the parameters.
	 * 
	 */
	@Override
	public List<WetterDaten> abrufenEinzelwerte(String ort, LocalDate datumVon, LocalDate datumBis, LocalTime zeitVon,
			LocalTime zeitBis) {

		try {
			HttpClient client = HttpClient.newHttpClient();

			// URIBuilder created by ChatGPT

			URI uri = UriBuilder.fromUri(BASE_URI + "wetterdaten/abrufenEinzelwerte").queryParam("ort", ort)
					.queryParam("datumVon", datumVon.toString()).queryParam("datumBis", datumBis.toString())
					.queryParam("zeitVon", zeitVon.toString()).queryParam("zeitBis", zeitBis.toString()).build();



			HttpRequest req = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				List<WetterDaten> einzelwerte = mapper.readValue(json, new TypeReference<List<WetterDaten>>() {
				});
				return einzelwerte;

			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Formulates an HTTP Request to call method minMaxOrt() in ressource class.
	 * @param datum as LocalDate, zeit as LocalTime, artDerAbfrage as String.
	 * @return returns a list of all the Values as WetterDaten-Object that fit the values given in the parameters.
	 * 
	 */
	@Override
	public List<WetterDaten> minMaxOrt(LocalDate datum, LocalTime zeit, String artDerAbfrage) {
		try {
			HttpClient client = HttpClient.newHttpClient();

			// URIBuilder created by ChatGPT

			URI uri = UriBuilder.fromUri(BASE_URI + "wetterdaten/minMaxOrt").queryParam("datum", datum.toString())
					.queryParam("zeit", zeit.toString()).queryParam("artDerAbfrage", artDerAbfrage).build();



			HttpRequest req = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				String json = res.body();
				List<WetterDaten> einzelwerte = mapper.readValue(json, new TypeReference<List<WetterDaten>>() {
				});
				return einzelwerte;

			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Exports a cities to pdf, xml or printing it into the console. (used for exporting the lowest and highest values of the given kind.)
	 *  
	 * @param wetterDaten (List of weather data), zeitPunkt (the time where the data is from), exportTyp (integer to choose which export format), artDerAbfrage (the kind of data that will be exported.)
	 * which timespan the method is called upon)
	 * 
	 */
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
				try { //PDF Export wurde mithilfe von ChatGPT aufgesetzt. Code wurde verstanden.
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
					float idWidth = 50;
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
		} else {
			System.out.println("Zum eingegebenen Zeitpunkt stehen keine Daten zur Verfuegung.");
		}

	}

	/**
	 * formulates an http-request to the ressource class and calls the method ortVorhanden to check if a city is in the db or not.
	 * @param ort (String of the city)
	 * @return returns true if the city is avilable else returns false. 
	 * 
	 */
	@Override
	public boolean ortVorhanden(String ort) {
		try {
			HttpClient client = HttpClient.newHttpClient();

			// URIBuilder created by ChatGPT
			
			String korrekturString = ort.replace(" ", "_");

			URI uri = URI.create(BASE_URI + "ort/ortVorhanden/" + korrekturString);
			
			HttpRequest req = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();

			//HttpRequest req = HttpRequest.newBuilder(uri).GET().header("Accept", MEDIA_TYPE).build();

			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			if (res.statusCode() == 200) {

				return Boolean.parseBoolean(res.body());

			} else {
				myLogger.error("Aktion misslungen : ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			myLogger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

}
