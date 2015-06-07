package database.cassandra;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import database.DBController;
import database.Log;

public class CassandraDBController implements DBController{
	
	private Cluster cluster;
	private Session session;

	public CassandraDBController() {
		cluster = Cluster.builder().addContactPoint("192.168.2.121").build();
		session = cluster.connect();
		
		final String createCql =
				"CREATE TABLE log_test.log (level varchar, message varchar, createDate Date, "
				+ " PRIMARY KEY (level, message, createDate))";
		session.execute(createCql);
	}

	public void saveLog(Log log) {
		session.execute("INSERT INTO log_test.log (level, message, createDate) VALUES (?, ?, ?)", log.getLevel(), log.getMessage(), log.getCreateDate() );
	}

	public List<Log> query(String query) {
		List<database.Log> rc = new ArrayList<database.Log>();
		final ResultSet results = session.execute(query);
		
		for(Row row : results){
			rc.add(new Log(row.getString("message"), row.getString("level"), row.getDate("createDate")));
		}
		
		return rc;
	}

	public boolean shutdown() {
		try{
			session.close();
			return true;
		}catch (Exception e){
			return false;
		}
	}

}
