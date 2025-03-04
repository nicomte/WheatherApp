package ch.hslu.swde.wda.g01.persister.api;

import ch.hslu.swde.wda.g01.domain.*;

public interface BenutzerDao extends DataDao<Benutzer>{

	Benutzer findByBenutzername(String benutzerName);
	
}
