package database;

import java.util.Date;

public class Log {
	
	private String message;
	
	private String level;
	
	private Date createDate;

	public Log(String message, String level, Date createDate){
		this.message = message;
		this.level = level;
		this.createDate = createDate;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
