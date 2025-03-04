package ch.hslu.swde.wda.g01.proxy.testing;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ch.hslu.swde.wda.g01.domain.*;
import ch.hslu.swde.wda.g01.persister.api.WetterDatenDao;
import ch.hslu.swde.wda.g01.persister.impl.WetterDatenDaoImpl;
import ch.hslu.wda.g01.business.api.Wda;
import ch.hslu.swde.wda.g01.proxy.*;

public class TestProxy {

	WetterDatenDao wdPersister = new WetterDatenDaoImpl();
	Wda wdaProxy = new WdaProxy();

	@BeforeEach
	public void setUp() throws Exception {
		for (WetterDaten wd : wdPersister.alle(WetterDaten.class)) {
			wdPersister.loeschen(wd);
		}
	}

	@Disabled
	void testAbrufenEinzelwerte() {

		for (WetterDaten w : TestUtil.createWetterDatenListe()) {
			wdPersister.speichern(w);
		}
		List<WetterDaten> wdListDb = wdaProxy.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 6),
				LocalDate.of(2024, 5, 16), LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59));

		System.out.println(wdListDb);
	}
	
	@Disabled
	void testMinMaxOrt() {

		for (WetterDaten w : TestUtil.createWetterDatenListe()) {
			wdPersister.speichern(w);
		}
		List<WetterDaten> wdListDb = wdaProxy.abrufenEinzelwerte("Luzern", LocalDate.of(2024, 5, 6),
				LocalDate.of(2024, 5, 16), LocalTime.of(0, 0, 0), LocalTime.of(23, 59, 59));

		System.out.println(wdListDb);
	}
}
