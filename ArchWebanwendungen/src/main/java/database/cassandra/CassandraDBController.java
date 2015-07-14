package database.cassandra;

import java.util.ArrayList;
import java.util.List;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;

import database.DBController;
import database.Log;

public class CassandraDBController implements DBController {

	private Cluster cluster;
	private Session session;

	public CassandraDBController() {
		cluster = Cluster.builder().addContactPoint("192.168.2.121")
				.withPort(9042).build();

		session = cluster.connect();

		try{
			session.execute("CREATE KEYSPACE log_test WITH replication "
					+ "= {'class':'SimpleStrategy', 'replication_factor':3};");
		}catch (AlreadyExistsException e){
			System.out.println("cassandra: keyspace already exists");
		}

		final String createCql = "CREATE TABLE log_test.log (level varchar, message varchar, createDate timestamp, "
				+ " PRIMARY KEY (level, message, createDate))";
		try{
			session.execute(createCql);
		} catch(AlreadyExistsException e){
			System.out.println("cassandra: table already exists");
		}
		
	}

	public void saveLog(Log log) {
		session.execute(
				"INSERT INTO log_test.log (level, message, createDate) VALUES (?, ?, ?)",
				log.getLevel(), log.getMessage(), log.getCreateDate());
	}

	public List<Log> query(String query) {
		List<database.Log> rc = new ArrayList<database.Log>();
		final ResultSet results = session.execute(query);

		for (Row row : results) {
			rc.add(new Log(row.getString("message"), row.getString("level"),
					row.getDate("createDate")));
		}

		return rc;
	}

	public boolean shutdown() {
		try {
			session.close();
			cluster.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void clearContent() {
		final String createCql = "TRUNCATE log_test.log";
		
		session.execute(createCql);
	}

}
