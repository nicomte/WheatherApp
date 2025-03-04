package ch.hslu.swde.wda.g01.reader.rws.proxy;

import java.util.List;

import ch.hslu.swde.wda.g01.domain.*;

public class TestMain {

	public static void main(String[] args) {
		ReaderRWSProxy test = new ReaderRWSProxy();
		List<Ort> ort = test.holeOrte();
		System.out.println(ort.size());
		List<WetterDaten> wetterDaten = test.holeWetterdaten(2023);
		System.out.println(wetterDaten.size());
		System.out.println(wetterDaten.get(0));
		System.out.println(wetterDaten.get(1));
		System.out.println(wetterDaten.get(2));

	}

}
