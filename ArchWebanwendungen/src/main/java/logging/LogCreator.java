package logging;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import database.Log;

public class LogCreator {
	
	private static final String[] errorLevel = {"note", "warning", "error"};
	private static final String[] messages = {"Bad Request", "Unauthorized", "Forbidden", "Not Found", "Method Not Allowed", "Not Acceptable", "Proxy Authentication Required", "Request Timeout", "Conflict", "Gone", "Length Required", "Precondition Failed", "Request Entity Too Large", "Request-URI Too Long", "Unsupported Media Type", "Requested Range Not Satisfiable", "Expectation Failed"};

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
		double msg = Math.random()*16;
		int msgSelection = (int) msg;
		return messages[msgSelection];
	}

	private String getLevel() {
		double rndLevel = Math.random();
		if(rndLevel < 0.5){
			return errorLevel[0];
		}else if(rndLevel < 0.8){
			return errorLevel[1];
		}else {
			return errorLevel[2];
		}
	}
	
	public static void main(String[] args) {
		List<Log> logs = new LogCreator().createAmountLogs(100);
		for (Log log : logs) {
			System.out.println("Level: " + log.getLevel() + " ; message: " + log.getMessage() + " ; time: " + log.getCreateDate());
		}
	}
}
