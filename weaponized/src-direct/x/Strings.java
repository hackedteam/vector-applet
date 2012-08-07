package x;

import java.io.UnsupportedEncodingException;

// TODO: Provide better encoding... 
public class Strings {
	
	public static String enc(String s) {
		try {
			byte[] bytes = s.getBytes("ISO8859-1");
			for( int i=0; i<bytes.length; i++ ) {
				bytes[i] = (byte) (bytes[i]+1);
			}
			return new String(bytes,"ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("");
		}
	}
	
	public static String dec(String s) {
		try {
			byte[] bytes = s.getBytes("ISO8859-1");
			for( int i=0; i<bytes.length; i++ ) {
				bytes[i] = (byte) (bytes[i]-1);
			}
			return new String(bytes,"ISO8859-1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("");
		}
	}
	
}
