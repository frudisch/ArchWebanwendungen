package database.relational;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import database.DBController;
import database.Log;

public class RelDBController implements DBController{

	private EntityManager em;

	public RelDBController() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("ArchWebanwendungen");
		em = emf.createEntityManager();
	}
	
	public void saveLog(Log log) {
		database.relational.Log serLog = (database.relational.Log) log;
		em.persist(serLog);
	}

	public List<Log> query(String query) {
		List<Log> rc = new ArrayList<Log>();
		TypedQuery<database.relational.Log> queryResult = em.createNamedQuery(query, database.relational.Log.class);
		
		for(Log log : queryResult.getResultList()){
			rc.add((database.Log) log);
		}
		
		return rc;
	}

	public boolean shutdown() {
		try{
			em.close();
			return true;
		}catch (Exception e){
			return false;
		}
	}

}
