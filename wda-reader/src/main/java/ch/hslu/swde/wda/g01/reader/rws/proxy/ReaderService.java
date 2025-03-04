package ch.hslu.swde.wda.g01.reader.rws.proxy;

import java.util.List;

import ch.hslu.swde.wda.g01.domain.WetterDaten;
import ch.hslu.swde.wda.g01.domain.Ort;

public interface ReaderService {
	
	List<WetterDaten> holeWetterdaten(int jahr);
	List<Ort> holeOrte();

}
