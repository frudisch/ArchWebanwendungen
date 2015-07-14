package fill.controller;

import java.util.List;

import logging.LogCreator;
import database.DBController;
import database.Log;
import database.cassandra.CassandraDBController;
import database.elasticsearch.ElasticDBController;
import database.influx.InfluxDBController;
import database.relational.RelDBController;

public class FillController {
	
	public static final int testAmount = 1000000;

	public FillController() {
		
	}
	
	public void startFilling(){
		DBController cassandra = new CassandraDBController();
		DBController elasticsearch = new ElasticDBController();
		DBController influx = new InfluxDBController();
		DBController relation = new RelDBController();
		
		cassandra.clearContent();
		elasticsearch.clearContent();
		influx.clearContent();
		relation.clearContent();
		System.out.println("database truncated");
		
		List<Log> logs = new LogCreator().createAmountLogs(testAmount);
		
		System.out.println("creating threads");
		Thread tCassandra = new Thread(new DatabaseFiller(cassandra, "cassandra_" + testAmount + ".txt", logs));
		Thread tElasticSearch = new Thread(new DatabaseFiller(elasticsearch, "elasticsearch_" + testAmount + ".txt", logs));
		Thread tInflux = new Thread(new DatabaseFiller(influx, "influx_" + testAmount + ".txt", logs));
		Thread tRelation = new Thread(new DatabaseFiller(relation, "relation_" + testAmount + ".txt", logs));
		
		System.out.println("starting threads");
		tCassandra.start();
		tElasticSearch.start();
		tInflux.start();
		tRelation.start();
		
		System.out.println("start joining threads");
		try {
			tCassandra.join();
			cassandra.shutdown();
			
			tElasticSearch.join();
			elasticsearch.shutdown();
			
			tInflux.join();
			influx.shutdown();
			
			tRelation.join();
			relation.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("Yeah alle fertig !");
	}
	
	public static void main(String[] args) {
		new FillController().startFilling();
	}
}
