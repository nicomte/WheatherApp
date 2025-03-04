package ch.hslu.swde.wda.g01.ressource.ws;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Response.Status;
import ch.hslu.wda.g01.business.impl.*;
import ch.hslu.swde.wda.g01.domain.Benutzer;
import ch.hslu.wda.g01.business.api.*;

@Path("/benutzer")
public class BenutzerRessource {
	@Context
	UriInfo uriInfo;

	private Logger myLogger = LogManager.getLogger(BenutzerRessource.class);
	private ObjectMapper mapper = new ObjectMapper();

	private Wda wda = new WdaImpl();

	@GET
	@Path("/alleBenutzer")
	@Produces(MediaType.APPLICATION_JSON)
	public Response alleBenutzer() {

		try {
			myLogger.info("alleOrtschaften called");

			List<Benutzer> alleBenutzer = wda.alleBenutzer();

			String alleBenutzerJson = mapper.writeValueAsString(alleBenutzer);

			return Response.ok(alleBenutzerJson).build();

		} catch (JsonProcessingException p) {
			myLogger.error("Error processing alleOrteJson: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@POST
	@Path("/hinzufuegenBenutzer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response hinzufuegenBenutzer(String benutzerJson) {
		try {
			myLogger.info("hinzufuegenBenutzer(" + benutzerJson + ") called");

			Benutzer benutzerToAdd = mapper.readValue(benutzerJson, Benutzer.class);

			Benutzer addedUser = wda.hinzufuegenBenutzer(benutzerToAdd);
			if(addedUser != null) {
				return Response.ok(addedUser).build();
			}else {
				return Response.noContent().build();
			}
			

		} catch (JsonMappingException m) {
			myLogger.error("Error mapping Benutzer: " + m.getMessage(), m);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (JsonProcessingException p) {
			myLogger.error("Error processing Benutzer: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path("/loeschenBenutzer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loeschenBenutzer(String benutzerJson) {
		try {
			myLogger.info("loeschenBenutzer(" + benutzerJson + ") called");

			Benutzer benutzerToDelete = mapper.readValue(benutzerJson, Benutzer.class);

			if (benutzerToDelete != null) {
				Benutzer deletedUser = wda.loeschenBenutzer(benutzerToDelete);
				return Response.ok(deletedUser).build();
			} else {
				myLogger.info("No entity of Type \'Benutzer\' " + benutzerToDelete + " found.");
				return Response.status(Status.NOT_FOUND).build();
			}
		} catch (JsonMappingException m) {
			myLogger.error("Error mapping Benutzer: " + m.getMessage(), m);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (JsonProcessingException p) {
			myLogger.error("Error processing Benutzer: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}

	@PUT
	@Path("/anpassenBenutzer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response anpassenBenutzer(String benutzerJson) {

		try {
			myLogger.info("anpassenBenutzer: called");

			Benutzer benutzer = mapper.readValue(benutzerJson, Benutzer.class);

			Benutzer updatedBenutzer = wda.anpassenBenutzer(benutzer);

			String updatedBenutzerJson = mapper.writeValueAsString(updatedBenutzer);

			if(updatedBenutzer != null) {
				return Response.ok(updatedBenutzerJson).build();
			}else {
				return Response.status(Status.NOT_FOUND).build();
			}
			

			// Return Responses in Catch-Bloecken by ChatGPT
		} catch (JsonMappingException m) {
			myLogger.error("Error mapping Benutzer: " + m.getMessage(), m);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (JsonProcessingException p) {
			myLogger.error("Error processing Benutzer: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@PUT
	@Path("/anpassenBenutzername/{benutzername}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response anpassenBenutzername(String benutzerJson, @PathParam("benutzername") String benutzername) {

		try {
			myLogger.info("anpassenBenutzername: " + benutzername + " called");

			Benutzer benutzer = mapper.readValue(benutzerJson, Benutzer.class);

			Benutzer updatedBenutzer = wda.anpassenBenutzername(benutzer, benutzername);

			String updatedBenutzerJson = mapper.writeValueAsString(updatedBenutzer);

			if(updatedBenutzer != null) {
			return Response.ok(updatedBenutzerJson).build();
			}else {
				return Response.status(Status.NOT_FOUND).build();
			}
			// Return Responses in Catch-Bloecken by ChatGPT
		} catch (JsonMappingException m) {
			myLogger.error("Error mapping Benutzer: " + m.getMessage(), m);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (JsonProcessingException p) {
			myLogger.error("Error processing Benutzer: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@PUT
	@Path("/anpassenNachname/{nachname}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response anpassenNachname(String benutzerJson, @PathParam("nachname") String nachname) {

		try {
			myLogger.info("anpassenNachname: " + nachname + " called");

			Benutzer benutzer = mapper.readValue(benutzerJson, Benutzer.class);

			Benutzer updatedBenutzer = wda.anpassenNachname(benutzer, nachname);

			String updatedBenutzerJson = mapper.writeValueAsString(updatedBenutzer);

			if(updatedBenutzer != null) {
				return Response.ok(updatedBenutzerJson).build();
			}else {
					return Response.status(Status.NOT_FOUND).build();
			}

			// Return Responses in Catch-Bloecken by ChatGPT
		} catch (JsonMappingException m) {
			myLogger.error("Error mapping Benutzer: " + m.getMessage(), m);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (JsonProcessingException p) {
			myLogger.error("Error processing Benutzer: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@PUT
	@Path("/anpassenVorname/{vorname}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response anpassenVorname(String benutzerJson, @PathParam("vorname") String vorname) {

		try {
			myLogger.info("anpassenVorname: " + vorname + " called");

			Benutzer benutzer = mapper.readValue(benutzerJson, Benutzer.class);

			Benutzer updatedBenutzer = wda.anpassenVorname(benutzer, vorname);

			String updatedBenutzerJson = mapper.writeValueAsString(updatedBenutzer);

			if(updatedBenutzer != null) {
				return Response.ok(updatedBenutzerJson).build();
			}else {
					return Response.status(Status.NOT_FOUND).build();
			}

			// Return Responses in Catch-Bloecken by ChatGPT
		} catch (JsonMappingException m) {
			myLogger.error("Error mapping Benutzer: " + m.getMessage(), m);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (JsonProcessingException p) {
			myLogger.error("Error processing Benutzer: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@PUT
	@Path("/anpassenPasswort")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response anpassenPasswort(String userPwJson) {

		try {
			myLogger.info("anpassenPasswort called");

			String[] userPw = mapper.readValue(userPwJson, String[].class);
			Benutzer userToUpdate = mapper.readValue(userPw[0], Benutzer.class);

			Benutzer updatedBenutzer = wda.anpassenPasswort(userToUpdate, userPw[1]);

			String updatedBenutzerJson = mapper.writeValueAsString(updatedBenutzer);

			if(updatedBenutzer != null) {
				return Response.ok(updatedBenutzerJson).build();
			}else {
					return Response.status(Status.NOT_FOUND).build();
			}

			// Return Responses in Catch-Bloecken by ChatGPT
		} catch (JsonMappingException m) {
			myLogger.error("Error mapping Benutzer: " + m.getMessage(), m);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (JsonProcessingException p) {
			myLogger.error("Error processing Benutzer: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@PUT
	@Path("/anpassenRolle/{rolle}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response anpassenRolle(String benutzerJson, @PathParam("rolle") String rolle) {

		try {
			myLogger.info("anpassenRolle: " + rolle + " called");

			Benutzer benutzer = mapper.readValue(benutzerJson, Benutzer.class);

			Benutzer updatedBenutzer = wda.anpassenRolle(benutzer, rolle);

			String updatedBenutzerJson = mapper.writeValueAsString(updatedBenutzer);

			if(updatedBenutzer != null) {
				return Response.ok(updatedBenutzerJson).build();
			}else {
					return Response.status(Status.NOT_FOUND).build();
			}

			// Return Responses in Catch-Bloecken by ChatGPT
		} catch (JsonMappingException m) {
			myLogger.error("Error mapping Benutzer: " + m.getMessage(), m);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (JsonProcessingException p) {
			myLogger.error("Error processing Benutzer: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@GET
	@Path("validiereBenutzername/{benutzerName}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response validiereBenutzername(@PathParam("benutzerName") String benutzerName) {

		myLogger.info("validiereBenutzername(" + benutzerName + ") called");

		Benutzer benutzerById = wda.validiereBenutzername(benutzerName);

		if (benutzerById != null) {
			return Response.ok(benutzerById).build();
		} else {
			myLogger.info("No entity of Type \'Benutzer\' for benutzername=" + benutzerName + " found.");
			return Response.status(Status.NOT_FOUND).build();
		}

	}

	@POST
	@Path("/passwortVorhanden")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response passwortVorhanden(String benutzerJson) {

		try {
			myLogger.info("passwortVorhanden called");

			Benutzer benutzer = mapper.readValue(benutzerJson, Benutzer.class);

			String passwort = wda.passwortVorhanden(benutzer);
			
			if(passwort != null) {
				return Response.ok(passwort).build();
			}else {
					return Response.status(Status.NOT_FOUND).build();
			}

			// Return Responses in Catch-Bloecken by ChatGPT
		} catch (JsonMappingException m) {
			myLogger.error("Error mapping Benutzer: " + m.getMessage(), m);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (JsonProcessingException p) {
			myLogger.error("Error processing Benutzer: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@POST
	@Path("/passwortSetzen")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response passwortSetzen(String userXPassword) {
		try {
			myLogger.info("passwortSetzen called");

			String[] userAndPassword = mapper.readValue(userXPassword, String[].class);
			Benutzer benutzer = mapper.readValue(userAndPassword[0], Benutzer.class);

			Boolean check = wda.passwortSetzen(benutzer, userAndPassword[1]);

			return Response.ok(check).build();

			// Return Responses in Catch-Bloecken by ChatGPT
		} catch (JsonMappingException m) {
			myLogger.error("Error mapping Benutzer: " + m.getMessage(), m);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (JsonProcessingException p) {
			myLogger.error("Error processing Benutzer: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@POST
	@Path("/validierePasswort")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validierePasswort(String passwordsJson) {

		// Error-Handling by Chat-GPT
		try {
			myLogger.info("validierePasswort called");

			String[] passwords = mapper.readValue(passwordsJson, String[].class);

			Boolean pwPruefung = wda.validierePasswort(passwords[0], passwords[1]);

			return Response.ok(pwPruefung).build();

		} catch (IOException e) {
			myLogger.error("Error deserializing Benutzer JSON: " + e.getMessage(), e);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e) {
			myLogger.error("Error validating role: " + e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@POST
	@Path("/validiereRolle")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response validiereRolle(String benutzerJson) {

		// Error-Handling by Chat-GPT
		try {
			myLogger.info("validiereRolle called");

			Benutzer benutzer = mapper.readValue(benutzerJson, Benutzer.class);
			String rolle = wda.validiereRolle(benutzer);

			return Response.ok(rolle).build();

		} catch (IOException e) {
			myLogger.error("Error deserializing Benutzer JSON: " + e.getMessage(), e);
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (Exception e) {
			myLogger.error("Error validating role: " + e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GET
	@Path("findeBenutzerById/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") int id) {

		myLogger.info("findeBenutzerById(" + id + ") called");

		Benutzer benutzerById = wda.findeBenutzerById(id);

		if (benutzerById != null) {
			return Response.ok(benutzerById).build();
		} else {
			myLogger.info("No entity of Type \'Benutzer\' for id=" + id + " found.");
			return Response.status(Status.NOT_FOUND).build();
		}

	}
}
