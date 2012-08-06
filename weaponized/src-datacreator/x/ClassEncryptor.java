package x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ClassEncryptor {

	public static void main(String[] args) throws Exception {
		File f = new File(args[0]);
		byte[] bytes = new byte[(int) f.length()];
		FileInputStream fin = new FileInputStream(f);
		fin.read(bytes);
		fin.close();
		for( int i=0; i<bytes.length; i++ ) {
			bytes[i] = (byte) (bytes[i] ^ 255); // "Inverse bits encryption": Enough to hide
		}
		File f2 = new File(args[1]);
		FileOutputStream fout = new FileOutputStream(f2);
		fout.write(bytes);
		fout.close();
	}
	
}
