package query.controller;

import java.util.List;

import database.DBController;
import database.Log;
import database.cassandra.CassandraDBController;
import database.elasticsearch.ElasticDBController;
import database.influx.InfluxDBController;
import database.relational.RelDBController;

public class Controller {
	
	DBController cassandra;
	DBController elasticsearch;
	DBController influx;
	DBController relation;
	
	public Controller() {		
		cassandra = new CassandraDBController();
		elasticsearch = new ElasticDBController();
		influx = new InfluxDBController();
		relation = new RelDBController();
	}
	
	public void performaceSelection(){
		double timeStart, timeEnd;
		
		// http://stackoverflow.com/questions/18697725/cassandra-get-all-records-in-time-range  and createDate <= '2015-07-16 11:23:10' and createDate >= '2015-07-16 11:23:00'
		String cassandraQuery = "SELECT * FROM log_test.log where level = 'error' and message = 'Gone' and createDate <= '2015-07-16 05:23:10-0400' and createDate >= '2015-07-16 05:23:00-0400' ALLOW FILTERING";
		timeStart = System.currentTimeMillis();
		List<Log> cassandraRC = cassandra.query(cassandraQuery);
		timeEnd = System.currentTimeMillis();
		System.out.println("Cassandra: " + cassandraRC.size() + " Zeit: " + (timeEnd - timeStart));
		for(Log log : cassandraRC){
			System.out.println("level: " + log.getLevel() + " message: " + log.getMessage() + " date: " + log.getCreateDate());
			break;
		}
		
		// http://stackoverflow.com/questions/27243698/searching-multiple-strings-in-all-fields-in-elasticsearch-using-java-api
		String elasticsearchQuery = "level:error;message:Gone;createDateFrom/2015-07-16T05:23:00;createDateTo/2015-07-16T05:23:10";
		timeStart = System.currentTimeMillis();
		List<Log> elasticSearchRC = elasticsearch.query(elasticsearchQuery);
		timeEnd = System.currentTimeMillis();
		System.out.println("ElasticSearch: " + elasticSearchRC.size() + " Zeit: " + (timeEnd - timeStart));
		for(Log log : elasticSearchRC){
			System.out.println("level: " + log.getLevel() + " message: " + log.getMessage() + " date: " + log.getCreateDate());
			break;
		}
		
		String rationalQuery = "SELECT l FROM Log l where l.message = 'Gone' and l.level = 'error' and l.createDate >= '2015-07-16 05:23:00' and l.createDate <= '2015-07-16 05:23:10'";
		timeStart = System.currentTimeMillis();
		List<Log> rationalRC = relation.query(rationalQuery);
		timeEnd = System.currentTimeMillis();
		System.out.println("Relational: " + rationalRC.size() + " Zeit: " + (timeEnd - timeStart));
		for(Log log : rationalRC){
			System.out.println("level: " + log.getLevel() + " message: " + log.getMessage() + " date: " + log.getCreateDate());
			break;
		}
		
		// https://influxdb.com/docs/v0.9/query_language/querying_data.html
		String influxQuery = "select time, level, message from log where level = 'error' and message = 'Gone' and time > '2015-07-16 05:23:00' and time < '2015-07-17' - 14h - 36m - 49s";
		timeStart = System.currentTimeMillis();
		List<Log> influxRC = influx.query(influxQuery);
		timeEnd = System.currentTimeMillis();
		System.out.println("Influx: " + influxRC.size() + " Zeit: " + (timeEnd - timeStart));
		for(Log log : influxRC){
			System.out.println("level: " + log.getLevel() + " message: " + log.getMessage() + " date: " + log.getCreateDate());
			break;
		}
	}
	
	public static void main(String[] args) {
		Controller c = new Controller();
		c.performaceSelection();
		c.shutdown();
	}

	private void shutdown() {
		cassandra.shutdown();
		elasticsearch.shutdown();
		influx.shutdown();
		relation.shutdown();
		
		System.exit(0);
	}

}
