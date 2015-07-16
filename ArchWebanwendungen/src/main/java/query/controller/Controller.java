package query.controller;

import database.DBController;
import database.Log;
import database.cassandra.CassandraDBController;
import database.elasticsearch.ElasticDBController;
import database.influx.InfluxDBController;
import database.relational.RelDBController;

public class Controller {
	
	DBController cassandra = new CassandraDBController();
	DBController elasticsearch = new ElasticDBController();
	DBController influx = new InfluxDBController();
	DBController relation = new RelDBController();
	
	public Controller() {		
		
	}
	
	public void performaceSelection(){
		String cassandraQuery = "SELECT * FROM log_test.log where level = '' and message = '' and createDate <= '' and createDate >= ''";
		System.out.println("Cassandra: " + cassandra.query(cassandraQuery).size());
		/*for(Log log : cassandra.query(cassandraQuery)){
			System.out.println(log);
		}*/
		
		String elasticsearchQuery = "SELECT * FROM log_test.log";
		System.out.println("ElasticSearch: " + elasticsearch.query(elasticsearchQuery).size());
		/*for(Log log : elasticsearch.query(elasticsearchQuery)){
			System.out.println(log);
		}*/
		
		String rationalQuery = "SELECT l FROM Log l where l.message = '' and l.note = 'error' and l.createDate between now() and now()";
		System.out.println("Relational: " + relation.query(rationalQuery).size());
		/*for(Log log : relation.query(rationalQuery)){
			System.out.println(log);
		}*/
		
		String influxQuery = "select time, level, message from log";
		System.out.println("Influx: " + influx.query(influxQuery).size());
		/*for(Log log : influx.query(influxQuery)){
			System.out.println(log);
		}*/
	}
	
	public void selectAllSize(){
		String cassandraQuery = "SELECT * FROM log_test.log";
		System.out.println("Cassandra: " + cassandra.query(cassandraQuery).size());
		for(Log log : cassandra.query(cassandraQuery)){
			System.out.println("level: " + log.getLevel() + " message: " + log.getMessage() + " date: " + log.getCreateDate());
			break;
		}
		
		String elasticsearchQuery = "SELECT * FROM log_test.log";
		System.out.println("ElasticSearch: " + elasticsearch.query(elasticsearchQuery).size());
		for(Log log : elasticsearch.query(elasticsearchQuery)){
			System.out.println("level: " + log.getLevel() + " message: " + log.getMessage() + " date: " + log.getCreateDate());
			break;
		}
		
		String rationalQuery = "SELECT l FROM Log l";
		System.out.println("Relational: " + relation.query(rationalQuery).size());
		for(Log log : relation.query(rationalQuery)){
			System.out.println("level: " + log.getLevel() + " message: " + log.getMessage() + " date: " + log.getCreateDate());
			break;
		}
		
		String influxQuery = "select time, level, message from log";
		System.out.println("Influx: " + influx.query(influxQuery).size());
		for(Log log : influx.query(influxQuery)){
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
