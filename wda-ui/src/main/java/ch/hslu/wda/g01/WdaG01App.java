package ch.hslu.wda.g01;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.wda.g01.ui.*;
/**
 * Class is the starting-point of the WdaG01-Application and starts the UI.
 * 
 * @author Dario Meyer, Mathis Trautmann, Agata Ciesielska, Nico Graf
 * @version 1.0.0
 */
public class WdaG01App {

	private static final Logger LOG = LogManager.getLogger(WdaG01App.class);

	public static void main(String[] args) {

		try {

			UI startUi = new UI();
			startUi.executeUIStart();

		} catch (Exception e) {
			LOG.error(">> Fehler: ", e);
			System.out.println("Applikation wurde unerwaretet heruntergefahren.");
		}

	}

}