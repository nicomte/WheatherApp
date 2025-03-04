package ch.hslu.swde.wda.g01.persister.api;

import java.util.List;

import ch.hslu.swde.wda.g01.domain.*;

public interface OrtDao extends DataDao<Ort>{
	public List<Ort> findByZip(int zip);
	public List<Ort> findByOrt(String name);

}
