package main.imapclient;

import java.io.File;

public class TestDirectory {
	public static void main(String args[]) {
		String workDir = System.getProperty("user.dir");
		System.out.println(workDir);
		new File(workDir + "/directory").mkdir();
	}
}
