package x;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * This tool can add the interface for Map.Entry to an arbitrary class.
 * Required, because the standard compilers won't compile our
 * ListSpinnerModel without this class.
 */
public class InterfaceAdder {

	final static Class<?> INTERFACE = Iterator.class;
	
    static File file;

    public static void main(String[] args) throws Exception {
    	file = new File(args[0]);
    	File outFile = new File(args[0]);

    	FileInputStream in = new FileInputStream(file);
    	
        // Parse input
        ClassReader cr = new ClassReader(in);
        ClassWriter cw = new ClassWriter(cr, 0);
        ClassVisitor cv = cw;

        // Our own visitor
        MigexClassAdapter aspectacular = new MigexClassAdapter(cv);
        cr.accept(aspectacular, 0);
        in.close();

        FileOutputStream out = new FileOutputStream(outFile);
        byte[] outputBytes = cw.toByteArray();
        out.write(outputBytes);
        out.close();
    }

    static class MigexClassAdapter extends ClassAdapter {

    	private static String[] addString(String[] strings, String string) {
    		if( strings == null || strings.length == 0 ) {
    			return new String[] { string };
    		}
    		String[] newStrings = new String[strings.length+1];
    		System.arraycopy(strings, 0, newStrings, 0, strings.length);
    		newStrings[strings.length] = string;
    		return newStrings;
    	}
    	
    	public MigexClassAdapter(ClassVisitor cv) {
    		super(cv);
    	}
    	
    	@Override
    	public void visit(int version, int access, String name, String signature,
                String superName, String[] interfaces) {
    		super.visit(version,access,name,signature,superName,
    			addString(interfaces,INTERFACE.getName().replace('.', '/')));
    	}
    	
    }
    
}

