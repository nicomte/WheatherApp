package ch.hslu.swde.wda.g01.persister.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
	
	private JpaUtil() {
		
	}
	
	private static Logger logger = LogManager.getLogger(JpaUtil.class);
	
	private static EntityManagerFactory entityManagerFactory = null;
	
	static {
		try {
			entityManagerFactory = Persistence.createEntityManagerFactory("PostgresPUOff");
		}catch(Exception e) {
			logger.error("ERROR: ", e);
			throw new RuntimeException(e);
		}
	}
	
	public static EntityManager createEntityManager() {
		return entityManagerFactory.createEntityManager();
	}
	
	

}
