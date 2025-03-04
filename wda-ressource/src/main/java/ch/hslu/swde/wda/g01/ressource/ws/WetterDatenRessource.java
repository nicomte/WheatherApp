package ch.hslu.swde.wda.g01.ressource.ws;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ch.hslu.swde.wda.g01.domain.WetterDaten;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import ch.hslu.wda.g01.business.api.*;
import ch.hslu.wda.g01.business.impl.*;

@Path("/wetterdaten")
public class WetterDatenRessource {
	@Context
	UriInfo uriInfo;

	private Logger myLogger = LogManager.getLogger(WetterDatenRessource.class);
	private static ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule()); // Unterstützung für
																									// die neuen Java
																									// 8-Datums- und
																									// Zeittypen welche
																									// Jackson nicht
																									// unterstützt

	private Wda wda = new WdaImpl();

	@POST
	@Path("/abrufenDurchschnittswerte")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response abrufenDurchschnittswerte(String listEinzelwerteWd) {

		try {
			myLogger.info("abrufenDurchschnittswerte(" + listEinzelwerteWd + ") called");

			List<WetterDaten> einzelwerteWd = mapper.readValue(listEinzelwerteWd,
					new TypeReference<List<WetterDaten>>() {
					});

			WetterDaten wetterDatenObjekt = wda.abrufenDurchschnittswerte(einzelwerteWd);

			String wetterDatenObjektJson = mapper.writeValueAsString(wetterDatenObjekt);

			return Response.ok(wetterDatenObjektJson).build();

		} catch (JsonMappingException m) {
			myLogger.error("Error mapping ListWetterdaten: " + m.getMessage(), m);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (JsonProcessingException p) {
			myLogger.error("Error processing ListWetterdaten: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("/abrufenMinMax")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response abrufenMinMax(String listEinzelwerteWd) {

		try {
			myLogger.info("abrufenMinMax(" + listEinzelwerteWd + ") called");

			List<WetterDaten> einzelwerteWd = mapper.readValue(listEinzelwerteWd,
					new TypeReference<List<WetterDaten>>() {
					});

			List<WetterDaten> wetterDaten = wda.abrufenMinMax(einzelwerteWd);

			GenericEntity<List<WetterDaten>> entity = new GenericEntity<>(wetterDaten) {
			};

			return Response.ok(entity).build();

		} catch (JsonMappingException m) {
			myLogger.error("Error mapping ListWetterdaten: " + m.getMessage(), m);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (JsonProcessingException p) {
			myLogger.error("Error processing ListWetterdaten: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("/aktualisierenAPI")
	@Produces(MediaType.APPLICATION_JSON)
	public Response aktualisierenAPI() {
		myLogger.info("ersterBezugAPI called");

		boolean result = wda.aktualisierenAPI();

		if (result) {
			return Response.ok(result).build();
		} else {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("/ersterBezugAPI")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ersterBezugAPI() {
		myLogger.info("ersterBezugAPI called");

		boolean result = wda.ersterBezugAPI();

		if (result) {
			return Response.ok(result).build();
		} else {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("/abrufenEinzelwerte")
	@Produces(MediaType.APPLICATION_JSON)
	public Response abrufenEinzelwerte(@QueryParam("ort") String ort, @QueryParam("datumVon") String datumVonStr,
			@QueryParam("datumBis") String datumBisStr, @QueryParam("zeitVon") String zeitVonStr,
			@QueryParam("zeitBis") String zeitBisStr) {

		LocalDate datumVon = LocalDate.parse(datumVonStr);
		LocalDate datumBis = LocalDate.parse(datumBisStr);
		LocalTime zeitVon = LocalTime.parse(zeitVonStr);
		LocalTime zeitBis = LocalTime.parse(zeitBisStr);

		myLogger.info("abrufenEinzelwerte(" + ort + ", " + datumVon + ", " + datumBis + ", " + zeitVon + ", " + zeitBis
				+ ") called");

		List<WetterDaten> wetterDaten = wda.abrufenEinzelwerte(ort, datumVon, datumBis, zeitVon, zeitBis);

		GenericEntity<List<WetterDaten>> entity = new GenericEntity<>(wetterDaten) {
		};

		return Response.ok(entity).build();
	}

	@GET
	@Path("/minMaxOrt")
	@Produces(MediaType.APPLICATION_JSON)
	public Response minMaxOrt(@QueryParam("datum") String datumStr, @QueryParam("zeit") String zeitStr,
			@QueryParam("artDerAbfrage") String artDerAbfrage) {

		LocalDate datum = LocalDate.parse(datumStr);
		LocalTime zeit = LocalTime.parse(zeitStr);

		System.out.println(datum + " - " + zeit);

		myLogger.info("minMaxOrt(" + datumStr + ", " + zeitStr + ", " + artDerAbfrage + ") called");

		List<WetterDaten> wetterDaten = wda.minMaxOrt(datum, zeit, artDerAbfrage);

		GenericEntity<List<WetterDaten>> entity = new GenericEntity<>(wetterDaten) {
		};

		return Response.ok(entity).build();
	}

	@GET
	@Path("helloworld")
	@Produces(MediaType.APPLICATION_JSON)
	public Response helloWorld() {
		return Response.ok("Hello World").build();
	}

}