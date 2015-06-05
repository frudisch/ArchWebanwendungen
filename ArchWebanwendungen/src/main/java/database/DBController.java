package database;

import java.util.List;

public interface DBController {

	public void saveLog (Log log);
	
	public List<Log> query(String query);
	
	public boolean shutdown();
}
