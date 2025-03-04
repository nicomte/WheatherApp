package ch.hslu.swde.wda.g01.reader.rws.proxy;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.hslu.swde.wda.g01.domain.*;

public class ReaderRWSProxy implements ReaderService {

	public List<Ort> holeOrte() {

		try {
			// HttpClient erstellen
			HttpClient client = HttpClient.newHttpClient();

			// URI definieren
			URI uri = URI
					.create("http://eee-03317.simple.eee.intern:8080/weatherdata-provider/rest/weatherdata/cities");
			// Http-Request formulieren
			HttpRequest req = HttpRequest.newBuilder(uri).GET().header("Accept", "application/json").build();

			// API-Antwort abfangen
			HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

			// Wenn Antowrt ok ausfaellt, JSON ausweiden
			if (res.statusCode() == 200) {
				String json = res.body();

				// Mapper erstellen, um JSON-File auf Java-Objekte zu verteilen
				ObjectMapper objectMapper = new ObjectMapper();

				// Unmarshalling von JSON-Antwort auf JsonMappingObject speichern
				List<Ort> orteJson = Arrays.asList(objectMapper.readValue(json, Ort[].class));
				
				for(Ort o : orteJson) {
					if(o.getName().equals("Lucerne")){
						o.setName("Luzern");
					}
				}

				return orteJson;

			} else {

				throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public List<WetterDaten> holeWetterdaten(int jahr) {
		try {
			// Liste für WetterDaten erstellen
			List<WetterDaten> wetterDaten = new ArrayList<>();

			// HttpClient erstellen
			HttpClient client = HttpClient.newHttpClient();

			// Mapper erstellen, um JSON-File auf Java-Objekte zu verteilen
			ObjectMapper objectMapper = new ObjectMapper();

			// Liste aller Orte
			List<Ort> orte = new ReaderRWSProxy().holeOrte();
			
			for (Ort ort : orte) {
				if(ort.getName().equals("Luzern")) {
					ort.setName("Lucerne");
				} else {
				ort.setName(ort.getName().replaceAll(" ", "%20"));
				}
			}

			for (int j = jahr; j <= jahr; j++) {

				for (int o = 0; o < orte.size(); o++) {

					// URI definieren
					URI uri = URI.create(
							"http://eee-03317.simple.eee.intern:8080/weatherdata-provider/rest/weatherdata/cityandyear?city="
									+ orte.get(o).getName() + "&year=" + j);
					
					// Http-Request formulieren
					HttpRequest req = HttpRequest.newBuilder(uri).GET().header("Accept", "application/json").build();

					// API-Antwort abfangen
					HttpResponse<String> res = client.send(req, BodyHandlers.ofString());

					// Wenn Antowrt ok ausfaellt, JSON ausweiden
					if (res.statusCode() == 200) {

						// Http-Antowrt im JSON-Format auf Variable speichern
						String json = res.body();
						// System.out.println(json);

						// Unmarshalling von JSON-Antwort auf JsonMappingObject speichern
						List<JsonMappingObject> jmo = Arrays
								.asList(objectMapper.readValue(json, JsonMappingObject[].class));
						// System.out.println(jmo.size());

						for (JsonMappingObject i : jmo) {

							// Objekte für wetterDaten und Ort erstellen
							WetterDaten wetterDatenObjekt = new WetterDaten();
							Ort ortObjekt = new Ort();

							// Ort name und plz aus JMO ziehen und wetterDatenObjekt zuordnen
							if(i.getCity().getZip() == 6000) {
								ortObjekt.setName("Luzern");
							} else {
							ortObjekt.setName((i.getCity().getName()).replace("%20", " "));
							}
							ortObjekt.setZip(i.getCity().getZip());
							wetterDatenObjekt.setOrt(ortObjekt);

							// Datum und Zeit sind in einzelner Variable "lastUpdateTime" gespeichert,
							// splitten und WetterDatenObjekt zuordnen
							String[] dateTime = i.getLastUpdateTime().split(" ");

							// Datum dem WetterDatenObjekt zuordnen
							LocalDate date = LocalDate.parse(dateTime[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
							wetterDatenObjekt.setDatum(date);

							// Zeit dem WetterDatenObjekt zuordnen
							LocalTime time = LocalTime.parse(dateTime[1], DateTimeFormatter.ofPattern("HH:mm:ss"));
							wetterDatenObjekt.setUhrZeit(time);

							// Weitere Messungen sind im JMO als ein String "data" gespeichert. String auf
							// mehrere Variabeln aufsplitten
							// CHATGPT Generated
							String[] keyValuePairs = i.getData().split("#");

							for (String pair : keyValuePairs) {
								// Split each pair by ':'
								String[] parts = pair.split(":");
								if (parts.length == 2) {
									String key = parts[0];
									String value = parts[1];
									// Check if the key matches the desired keys and assign value to
									// WetterDatenObjekt
									switch (key) {
									case "CURRENT_TEMPERATURE_CELSIUS":
										wetterDatenObjekt.setTemperatur(Double.parseDouble(value));
										break;
									case "PRESSURE":
										wetterDatenObjekt.setLuftDruck(Integer.parseInt(value));
										break;
									case "HUMIDITY":
										wetterDatenObjekt.setLuftFeuchtigkeit(Integer.parseInt(value));
										break;

									}
								}
							}
							wetterDaten.add(wetterDatenObjekt);
						}

					} else {

						throw new RuntimeException("ERROR: [HTTP-Status-Code: " + res.statusCode() + "]");
					}
				}
			}
			return wetterDaten;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
