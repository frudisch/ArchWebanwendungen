package logging;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.Log;

public class LogCreator {
	
	private static final String[] errorLevel = {"note", "warning", "error"};

	public LogCreator() {
		
	}
	
	public List<Log> createAmountLogs(int amount){
		ArrayList<Log> rc = new ArrayList<Log>();
		
		for(int i = 0; i < amount; i++){
			String level = getLevel();
			String message = getMessage();
			
			rc.add(new Log(message, level, new Date()));
		}
		
		return rc;
	}

	private String getMessage() {
		double msgLevel = Math.random();
		return null;
	}

	private String getLevel() {
		double rndLevel = Math.random();
		if(rndLevel < 0.4){
			return errorLevel[0];
		}else if(rndLevel < 0.8){
			return errorLevel[1];
		}else {
			return errorLevel[2];
		}
	}
}
