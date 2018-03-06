package main.imapserver;

public class Connect {
	/**
	 * Client USER Name and Password
	 */

	private String USERNAME = "java";
	private String PASSWORD = "jav";
	private int PORT = 1143;
	private String HOSTNAME = "127.0.0.1";

	public String getUsername() {
		return this.USERNAME;
	}

	public String getPassword() {

		return this.PASSWORD;
	}

	public int getPort() {
		return this.PORT;
	}

	public String gethostName() {
		return this.HOSTNAME;
	}

}
