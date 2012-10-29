package x;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Encrypt Strings in Java files. This is done by replacing any occurance
 * of "text" with x.Strings.dec("enodedText")
 *
 */
public class StringEncryptor {

	public static void main(String[] args) throws Exception {
//		System.out.println("text");
//		System.out.println(x.Strings.enc("text"));
//		System.out.println(x.Strings.dec("ufyu"));
//		
		encodeFile(args[0],args[1]);


//        System.out.println(x.Strings.dec("ufyu")+2+x.Strings.dec("Qfufs"));
//        System.out.println(encodedContents("System.out.println(\"text\"+2+\"Peter\")"));
//		
	}
	
	public static void encodeFile(String input, String output) throws Exception {
		byte[] bytes = new byte[128*1024]; // large enough
		FileInputStream in = new FileInputStream(input);
		int bytesRead = in.read(bytes);
		in.close();
		String content = new String(bytes,0,bytesRead,"ISO8859-1");
		content = encodedContents(content);
		bytes = content.getBytes("ISO8859-1");
		FileOutputStream fout = new FileOutputStream(output);
	    fout.write(bytes);
	    fout.close();
	}
	
	public static String encodedContents(String contents) {
		Pattern p = Pattern.compile("\"[^\"]+\"");
        Matcher m = p.matcher(contents);
        int delta = 0;
        while( m.find() ) {
        	System.out.println(m.start()+" "+m.end());
        	String start = contents.substring(0,m.start()+delta);
        	String text = contents.substring(m.start()+delta+1,m.end()+delta-1);
        	String end = contents.substring(m.end()+delta);
        	if( !"all".equals(text) && !"unused".equals(text) ) { // Annotations
        	    contents = start + "x.Strings.dec(\""+Strings.enc(text)+"\")" + end;
            	delta += 15;
        	}
        }
        return contents;
	}
	
}
