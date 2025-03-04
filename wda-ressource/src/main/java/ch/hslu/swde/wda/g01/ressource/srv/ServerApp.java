package ch.hslu.swde.wda.g01.ressource.srv;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import ch.hslu.swde.wda.g01.ressource.ws.BenutzerRessource;
import ch.hslu.swde.wda.g01.ressource.ws.OrtRessource;
import ch.hslu.swde.wda.g01.ressource.ws.WetterDatenRessource;
/**
 * Class starts the server and sets its base URI.
 * 
 * @author Dario Meyer, Mathis Trautmann, Agata Ciesielska, Nico Graf
 * @version 1.0.0
 */
public class ServerApp {
	public static final String BASE_URI = "http://localhost:9090/";

	// Methode 'main' ...
	public static void main(String[] args) {

		HttpServer srv = startHttpServer();

		System.out.println("Server gestartet: " + "\n" + BASE_URI + "Stopp mit ENTER");
		
		try {
			System.in.read();
		} catch (IOException e) {
			// do nothing
		}
		srv.stop(0);

	}

	// Methode 'startHttpServer' ...
	public static HttpServer startHttpServer() {
		ResourceConfig configWd = new ResourceConfig().register(WetterDatenRessource.class)
	            .register(OrtRessource.class)
	            .register(BenutzerRessource.class);

		HttpServer httpServerWd = JdkHttpServerFactory.createHttpServer(URI.create(BASE_URI), configWd);

		return httpServerWd;
	}
}