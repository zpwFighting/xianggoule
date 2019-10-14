package entity;

import java.io.Serializable;

public class Result implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String message;
	
	private boolean success;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Result(String message, boolean success) {
		super();
		this.message = message;
		this.success = success;
	}
	public Result(boolean success,String message) {
		super();
		this.message = message;
		this.success = success;
	}

	public Result() {
		super();
	}
	
	

}
