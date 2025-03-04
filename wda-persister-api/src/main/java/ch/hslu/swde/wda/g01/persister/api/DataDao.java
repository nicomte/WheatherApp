package ch.hslu.swde.wda.g01.persister.api;

import java.util.List;

public interface DataDao<T> {
	
	public T speichern(T Entity);
	public T loeschen(T Entity);
	public T aktualisieren(T Entity);
	public T find(Class<T> entityTyp,int id);
	public List<T> alle(Class<T> entityTyp);
	public List<T> alleSpeichern(List<T> entityTyp);
	

}
