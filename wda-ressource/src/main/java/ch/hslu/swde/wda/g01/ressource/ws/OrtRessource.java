package ch.hslu.swde.wda.g01.ressource.ws;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.hslu.swde.wda.g01.domain.Ort;
import ch.hslu.wda.g01.business.api.Wda;
import ch.hslu.wda.g01.business.impl.WdaImpl;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/ort")
public class OrtRessource {
	@Context
	UriInfo uriInfo;

	private Logger myLogger = LogManager.getLogger(OrtRessource.class);
	private ObjectMapper mapper = new ObjectMapper();

	private Wda wda = new WdaImpl();

	@GET
	@Path("/alleOrtschaften")
	@Produces(MediaType.APPLICATION_JSON)
	public Response alleOrtschaften() {
		try {
			myLogger.info("alleOrtschaften called");

			List<Ort> alleOrte = wda.alleOrtschaften();

			String alleOrteJson = mapper.writeValueAsString(alleOrte);

			return Response.ok(alleOrteJson).build();

		} catch (JsonProcessingException p) {
			myLogger.error("Error processing alleOrteJson: " + p.getMessage(), p);
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

	}

	@GET
	@Path("/ortVorhanden/{ort}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response ortVorhanden(@PathParam("ort") String ort) {
		
		String fixedOrt = ort.replace("_", " ");

		myLogger.info("ortVorhanden (" + fixedOrt + ") called");

		boolean ortVorhanden = wda.ortVorhanden(fixedOrt);

		return Response.ok(ortVorhanden).build();

	}

}
