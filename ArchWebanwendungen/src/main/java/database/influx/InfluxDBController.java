package database.influx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Serie;

import database.DBController;
import database.Log;

public class InfluxDBController implements DBController{
	
	private InfluxDB influxDB;
	
	private static final String dbName = "log_test";

	public InfluxDBController() {
		influxDB = InfluxDBFactory.connect("http://192.168.2.121:8086", "root", "root");
		
		try{
			influxDB.createDatabase(dbName);
		}catch(Exception e){
			System.out.println("kommt halt die exception");
		}
	}

	public void saveLog(Log log) {
		Serie serie = new Serie.Builder("log")
			.columns("time", "level", "message")
			.values(log.getCreateDate().getTime(), log.getLevel(), log.getMessage())
			.build();

		this.influxDB.write(dbName, TimeUnit.MILLISECONDS, serie);
	}

	public List<Log> query(String query) {
		//query = "select time, idle, steal, system, customername from testSeries";
		ArrayList<database.Log> rc = new ArrayList<database.Log>();
		
		List<Serie> result = this.influxDB.query(
				dbName,
				query,
				TimeUnit.MILLISECONDS);
		
		for (int i = 0; i < result.size(); i++) {
			for (Map<String, Object> row : result.get(i).getRows()) {
				Date date = new Date();
				date.setTime((Long) row.get("time"));
				rc.add(new Log(row.get("message").toString(), row.get("level").toString(), date));
			}
		}
		
		return rc;
	}

	public boolean shutdown() {
		return false;
	}

}
