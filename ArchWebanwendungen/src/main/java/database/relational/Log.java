package database.relational;

import java.io.Serializable;
import java.util.Date;

public class Log extends database.Log implements Serializable{

	public Log(String message, String level, Date createDate) {
		super(message, level, createDate);
	}

}
