package fill.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import database.DBController;
import database.Log;

public class DatabaseFiller implements Runnable{
	
	private DBController controller;
	private BufferedWriter out;
	private List<Log> logs;

	public DatabaseFiller(DBController controller, String logFileName, List<Log> logs) {
		this.controller = controller;
		this.logs = logs;
		try {
			this.out = new BufferedWriter(new FileWriter(new File(logFileName)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			int counter = 0;
			out.write(new Date() + " start writing " + logs.size() + " Logs zu Database \n");
			System.out.println("start writing " + controller.getClass().getName());
			
			double startTime = System.currentTimeMillis();
			for (int i = 0; i< logs.size(); i++) {
				double startTimeWriting = System.currentTimeMillis();
				
				controller.saveLog(logs.get(i));
				
				double endTimeWriting = System.currentTimeMillis();
				
				counter += (endTimeWriting - startTimeWriting);
				
				if((i % 50000) == 0){
					out.write(i + "; " + (endTimeWriting - startTime) + "; " + (50000.0/counter) + "\n");
					out.flush();
					counter = 0;
					System.out.println(controller.getClass().getName() + " bei " + i);
				}
			}
			double endTime = System.currentTimeMillis();
			
			out.write("ingesamt: " + (endTime - startTime) + " durchschnitt: " + (logs.size()/(endTime - startTime)));
			System.out.println("fertig: " + controller.getClass().getName() + " ingesamt: " + (endTime - startTime) + " durchschnitt: " + (logs.size()/(endTime - startTime)));
			
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
