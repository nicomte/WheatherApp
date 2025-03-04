package ch.hslu.swde.wda.g01.persister.impl;

import java.util.ArrayList;
import java.util.List;

import ch.hslu.swde.wda.g01.persister.api.DataDao;
import ch.hslu.swde.wda.g01.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class DataDaoImpl<T> implements DataDao<T>{

	@Override
	public T speichern(T entity) {
		EntityManager em = JpaUtil.createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
			return entity;
		}catch(Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}throw e;
		}finally {
			if(em.isOpen()) {
				em.close();
			}
		}
	}

	@Override
	public T loeschen(T entity) {
		EntityManager em = JpaUtil.createEntityManager();
		try {
			em.getTransaction().begin();
			em.remove(em.merge(entity));
			em.getTransaction().commit();
			return entity;
		}catch (Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}throw e;
		}finally {
			if(em.isOpen())
				em.close();
		}
	}

	@Override
	public T aktualisieren(T entity) {
		EntityManager em = JpaUtil.createEntityManager();
		try {
			em.getTransaction().begin();
			T newObject = em.merge(entity);
			em.getTransaction().commit();
			return newObject;
		}catch(Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}throw e;
		}finally {
			if(em.isOpen())
				em.close();
		}
	}

	@Override
	public T find(Class<T> entityTyp, int id) {
	    EntityManager em = JpaUtil.createEntityManager();
	    try {
	        T foundObject = em.find(entityTyp, id);
	        return foundObject;
	    } catch (Exception e) {
	        throw e;
	    } finally {
	        if (em.isOpen()) {
	            em.close();
	        }
	    }
	}

	
	public List<T> alleSpeichern(List<T> containerEntity){
		EntityManager em = JpaUtil.createEntityManager();
		try {
			
			em.getTransaction().begin();
			for(T entity: containerEntity) {
				em.persist(entity);
			}
			em.getTransaction().commit();
			return containerEntity;
			
		}catch(Exception e) {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}throw e;
		}finally {
			if(em.isOpen()) {
				em.close();
			}
		}
		
		
		
	}
	
	@Override
	public List<T> alle(Class<T> entityTyp) {
	    EntityManager em = JpaUtil.createEntityManager();
	    try {
	        TypedQuery<T> tQry = em.createQuery("SELECT e FROM " + entityTyp.getSimpleName() + " e", entityTyp);
	        List<T> results = tQry.getResultList();
	        return results != null ? results : new ArrayList<T>();
	    } catch (Exception e) {
	        throw e;
	    } finally {
	        if (em.isOpen()) {
	            em.close();
	        }
	    }
	}


}
